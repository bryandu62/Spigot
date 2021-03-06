package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.SqlQueryListener;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.api.BindParams;
import com.avaje.ebeaninternal.api.SpiSqlQuery;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.core.Message;
import com.avaje.ebeaninternal.server.core.RelationalQueryEngine;
import com.avaje.ebeaninternal.server.core.RelationalQueryRequest;
import com.avaje.ebeaninternal.server.jmx.MAdminLogging;
import com.avaje.ebeaninternal.server.persist.Binder;
import com.avaje.ebeaninternal.server.type.DataBind;
import com.avaje.ebeaninternal.server.util.BindParamsParser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public class DefaultRelationalQueryEngine
  implements RelationalQueryEngine
{
  private static final Logger logger = Logger.getLogger(DefaultRelationalQueryEngine.class.getName());
  private final int defaultMaxRows;
  private final Binder binder;
  private final String dbTrueValue;
  
  public DefaultRelationalQueryEngine(MAdminLogging logControl, Binder binder, String dbTrueValue)
  {
    this.binder = binder;
    this.defaultMaxRows = GlobalProperties.getInt("nativesql.defaultmaxrows", 100000);
    this.dbTrueValue = (dbTrueValue == null ? "true" : dbTrueValue);
  }
  
  public Object findMany(RelationalQueryRequest request)
  {
    SpiSqlQuery query = request.getQuery();
    
    long startTime = System.currentTimeMillis();
    
    SpiTransaction t = request.getTransaction();
    Connection conn = t.getInternalConnection();
    ResultSet rset = null;
    PreparedStatement pstmt = null;
    
    boolean useBackgroundToContinueFetch = false;
    
    String sql = query.getQuery();
    
    BindParams bindParams = query.getBindParams();
    if (!bindParams.isEmpty()) {
      sql = BindParamsParser.parse(bindParams, sql);
    }
    try
    {
      String bindLog = "";
      String[] propNames = null;
      synchronized (query)
      {
        if (query.isCancelled())
        {
          logger.finest("Query already cancelled");
          return null;
        }
        pstmt = conn.prepareStatement(sql);
        if (query.getTimeout() > 0) {
          pstmt.setQueryTimeout(query.getTimeout());
        }
        if (query.getBufferFetchSizeHint() > 0) {
          pstmt.setFetchSize(query.getBufferFetchSizeHint());
        }
        if (!bindParams.isEmpty()) {
          bindLog = this.binder.bind(bindParams, new DataBind(pstmt));
        }
        if (request.isLogSql())
        {
          String sOut = sql.replace('\n', ' ');
          sOut = sOut.replace('\r', ' ');
          t.logInternal(sOut);
        }
        rset = pstmt.executeQuery();
        
        propNames = getPropertyNames(rset);
      }
      float initCap = propNames.length / 0.7F;
      int estimateCapacity = (int)initCap + 1;
      
      int maxRows = this.defaultMaxRows;
      if (query.getMaxRows() >= 1) {
        maxRows = query.getMaxRows();
      }
      boolean hasHitMaxRows = false;
      
      int loadRowCount = 0;
      
      SqlQueryListener listener = query.getListener();
      
      BeanCollectionWrapper wrapper = new BeanCollectionWrapper(request);
      boolean isMap = wrapper.isMap();
      String mapKey = query.getMapKey();
      
      SqlRow bean = null;
      while (rset.next())
      {
        synchronized (query)
        {
          if (!query.isCancelled()) {
            bean = readRow(request, rset, propNames, estimateCapacity);
          }
        }
        if (bean != null)
        {
          if (listener != null)
          {
            listener.process(bean);
          }
          else if (isMap)
          {
            Object keyValue = bean.get(mapKey);
            wrapper.addToMap(bean, keyValue);
          }
          else
          {
            wrapper.addToCollection(bean);
          }
          loadRowCount++;
          if (loadRowCount == maxRows) {
            hasHitMaxRows = true;
          }
        }
      }
      BeanCollection<?> beanColl = wrapper.getBeanCollection();
      if ((hasHitMaxRows) && 
        (rset.next())) {
        beanColl.setHasMoreRows(true);
      }
      if (!useBackgroundToContinueFetch) {
        beanColl.setFinishedFetch(true);
      }
      if (request.isLogSummary())
      {
        long exeTime = System.currentTimeMillis() - startTime;
        
        String msg = "SqlQuery  rows[" + loadRowCount + "] time[" + exeTime + "] bind[" + bindLog + "] finished[" + beanColl.isFinishedFetch() + "]";
        
        t.logInternal(msg);
      }
      if (query.isCancelled()) {
        logger.fine("Query was cancelled during execution rows:" + loadRowCount);
      }
      return beanColl;
    }
    catch (Exception e)
    {
      String m = Message.msg("fetch.error", e.getMessage(), sql);
      throw new PersistenceException(m, e);
    }
    finally
    {
      if (!useBackgroundToContinueFetch)
      {
        try
        {
          if (rset != null) {
            rset.close();
          }
        }
        catch (SQLException e)
        {
          logger.log(Level.SEVERE, null, e);
        }
        try
        {
          if (pstmt != null) {
            pstmt.close();
          }
        }
        catch (SQLException e)
        {
          logger.log(Level.SEVERE, null, e);
        }
      }
    }
  }
  
  protected String[] getPropertyNames(ResultSet rset)
    throws SQLException
  {
    ArrayList<String> propNames = new ArrayList();
    
    ResultSetMetaData rsmd = rset.getMetaData();
    
    int columnsPlusOne = rsmd.getColumnCount() + 1;
    for (int i = 1; i < columnsPlusOne; i++)
    {
      String columnName = rsmd.getColumnLabel(i);
      
      propNames.add(columnName);
    }
    return (String[])propNames.toArray(new String[propNames.size()]);
  }
  
  protected SqlRow readRow(RelationalQueryRequest request, ResultSet rset, String[] propNames, int initialCapacity)
    throws SQLException
  {
    SqlRow bean = new DefaultSqlRow(initialCapacity, 0.75F, this.dbTrueValue);
    
    int index = 0;
    for (int i = 0; i < propNames.length; i++)
    {
      index++;
      Object value = rset.getObject(index);
      bean.set(propNames[i], value);
    }
    return bean;
  }
}
