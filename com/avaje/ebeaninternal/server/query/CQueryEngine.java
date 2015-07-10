package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.BackgroundExecutor;
import com.avaje.ebean.QueryIterator;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.BeanCollectionTouched;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.bean.ObjectGraphOrigin;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebeaninternal.api.BeanIdList;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.core.SpiOrmQueryRequest;
import com.avaje.ebeaninternal.server.jmx.MAdminLogging;
import com.avaje.ebeaninternal.server.persist.Binder;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

public class CQueryEngine
{
  private static final Logger logger = Logger.getLogger(CQueryEngine.class.getName());
  private final CQueryBuilder queryBuilder;
  private final MAdminLogging logControl;
  private final BackgroundExecutor backgroundExecutor;
  private final int defaultSecondaryQueryBatchSize = 100;
  
  public CQueryEngine(DatabasePlatform dbPlatform, MAdminLogging logControl, Binder binder, BackgroundExecutor backgroundExecutor)
  {
    this.logControl = logControl;
    this.backgroundExecutor = backgroundExecutor;
    this.queryBuilder = new CQueryBuilder(backgroundExecutor, dbPlatform, binder);
  }
  
  public <T> CQuery<T> buildQuery(OrmQueryRequest<T> request)
  {
    return this.queryBuilder.buildQuery(request);
  }
  
  public <T> BeanIdList findIds(OrmQueryRequest<T> request)
  {
    CQueryFetchIds rcQuery = this.queryBuilder.buildFetchIdsQuery(request);
    try
    {
      String sql = rcQuery.getGeneratedSql();
      sql = sql.replace('\n', ' ');
      if (this.logControl.isDebugGeneratedSql()) {
        System.out.println(sql);
      }
      request.logSql(sql);
      
      BeanIdList list = rcQuery.findIds();
      if (request.isLogSummary()) {
        request.getTransaction().logInternal(rcQuery.getSummary());
      }
      if ((!list.isFetchingInBackground()) && (request.getQuery().isFutureFetch()))
      {
        logger.fine("Future findIds completed!");
        request.getTransaction().end();
      }
      return list;
    }
    catch (SQLException e)
    {
      throw CQuery.createPersistenceException(e, request.getTransaction(), rcQuery.getBindLog(), rcQuery.getGeneratedSql());
    }
  }
  
  public <T> int findRowCount(OrmQueryRequest<T> request)
  {
    CQueryRowCount rcQuery = this.queryBuilder.buildRowCountQuery(request);
    try
    {
      String sql = rcQuery.getGeneratedSql();
      sql = sql.replace('\n', ' ');
      if (this.logControl.isDebugGeneratedSql()) {
        System.out.println(sql);
      }
      request.logSql(sql);
      
      int rowCount = rcQuery.findRowCount();
      if (request.isLogSummary()) {
        request.getTransaction().logInternal(rcQuery.getSummary());
      }
      if (request.getQuery().isFutureFetch())
      {
        logger.fine("Future findRowCount completed!");
        request.getTransaction().end();
      }
      return rowCount;
    }
    catch (SQLException e)
    {
      throw CQuery.createPersistenceException(e, request.getTransaction(), rcQuery.getBindLog(), rcQuery.getGeneratedSql());
    }
  }
  
  public <T> QueryIterator<T> findIterate(OrmQueryRequest<T> request)
  {
    CQuery<T> cquery = this.queryBuilder.buildQuery(request);
    request.setCancelableQuery(cquery);
    try
    {
      if (this.logControl.isDebugGeneratedSql()) {
        logSqlToConsole(cquery);
      }
      if (request.isLogSql()) {
        logSql(cquery);
      }
      if (!cquery.prepareBindExecuteQuery())
      {
        logger.finest("Future fetch already cancelled");
        return null;
      }
      int iterateBufferSize = request.getSecondaryQueriesMinBatchSize(100);
      
      QueryIterator<T> readIterate = cquery.readIterate(iterateBufferSize, request);
      if (request.isLogSummary()) {
        logFindManySummary(cquery);
      }
      return readIterate;
    }
    catch (SQLException e)
    {
      throw cquery.createPersistenceException(e);
    }
  }
  
  public <T> BeanCollection<T> findMany(OrmQueryRequest<T> request)
  {
    boolean useBackgroundToContinueFetch = false;
    
    CQuery<T> cquery = this.queryBuilder.buildQuery(request);
    request.setCancelableQuery(cquery);
    try
    {
      if (this.logControl.isDebugGeneratedSql()) {
        logSqlToConsole(cquery);
      }
      if (request.isLogSql()) {
        logSql(cquery);
      }
      if (!cquery.prepareBindExecuteQuery())
      {
        logger.finest("Future fetch already cancelled");
        return null;
      }
      Object beanCollection = cquery.readCollection();
      
      BeanCollectionTouched collectionTouched = request.getQuery().getBeanCollectionTouched();
      if (collectionTouched != null) {
        ((BeanCollection)beanCollection).setBeanCollectionTouched(collectionTouched);
      }
      BackgroundFetch fetch;
      if (cquery.useBackgroundToContinueFetch())
      {
        request.setBackgroundFetching();
        useBackgroundToContinueFetch = true;
        fetch = new BackgroundFetch(cquery);
        
        FutureTask<Integer> future = new FutureTask(fetch);
        ((BeanCollection)beanCollection).setBackgroundFetch(future);
        this.backgroundExecutor.execute(future);
      }
      if (request.isLogSummary()) {
        logFindManySummary(cquery);
      }
      request.executeSecondaryQueries(100);
      
      return (BackgroundFetch)beanCollection;
    }
    catch (SQLException e)
    {
      throw cquery.createPersistenceException(e);
    }
    finally
    {
      if (!useBackgroundToContinueFetch)
      {
        if (cquery != null) {
          cquery.close();
        }
        if (request.getQuery().isFutureFetch())
        {
          logger.fine("Future fetch completed!");
          request.getTransaction().end();
        }
      }
    }
  }
  
  public <T> T find(OrmQueryRequest<T> request)
  {
    T bean = null;
    
    CQuery<T> cquery = this.queryBuilder.buildQuery(request);
    try
    {
      if (this.logControl.isDebugGeneratedSql()) {
        logSqlToConsole(cquery);
      }
      if (request.isLogSql()) {
        logSql(cquery);
      }
      cquery.prepareBindExecuteQuery();
      if (cquery.readBean()) {
        bean = cquery.getLoadedBean();
      }
      if (request.isLogSummary()) {
        logFindBeanSummary(cquery);
      }
      request.executeSecondaryQueries(100);
      
      return bean;
    }
    catch (SQLException e)
    {
      throw cquery.createPersistenceException(e);
    }
    finally
    {
      cquery.close();
    }
  }
  
  private void logSqlToConsole(CQuery<?> cquery)
  {
    SpiQuery<?> query = cquery.getQueryRequest().getQuery();
    String loadMode = query.getLoadMode();
    String loadDesc = query.getLoadDescription();
    
    String sql = cquery.getGeneratedSql();
    String summary = cquery.getSummary();
    
    StringBuilder sb = new StringBuilder(1000);
    sb.append("<sql ");
    if (query.isAutofetchTuned()) {
      sb.append("tuned='true' ");
    }
    if (loadMode != null) {
      sb.append("mode='").append(loadMode).append("' ");
    }
    sb.append("summary='").append(summary);
    if (loadDesc != null) {
      sb.append("' load='").append(loadDesc);
    }
    sb.append("' >");
    sb.append('\n');
    sb.append(sql);
    sb.append('\n').append("</sql>");
    
    System.out.println(sb.toString());
  }
  
  private void logSql(CQuery<?> query)
  {
    String sql = query.getGeneratedSql();
    sql = sql.replace('\n', ' ');
    query.getTransaction().logInternal(sql);
  }
  
  private void logFindBeanSummary(CQuery<?> q)
  {
    SpiQuery<?> query = q.getQueryRequest().getQuery();
    String loadMode = query.getLoadMode();
    String loadDesc = query.getLoadDescription();
    String lazyLoadProp = query.getLazyLoadProperty();
    ObjectGraphNode node = query.getParentNode();
    String originKey;
    String originKey;
    if ((node == null) || (node.getOriginQueryPoint() == null)) {
      originKey = null;
    } else {
      originKey = node.getOriginQueryPoint().getKey();
    }
    StringBuilder msg = new StringBuilder(200);
    msg.append("FindBean ");
    if (loadMode != null) {
      msg.append("mode[").append(loadMode).append("] ");
    }
    msg.append("type[").append(q.getBeanName()).append("] ");
    if (query.isAutofetchTuned()) {
      msg.append("tuned[true] ");
    }
    if (originKey != null) {
      msg.append("origin[").append(originKey).append("] ");
    }
    if (lazyLoadProp != null) {
      msg.append("lazyLoadProp[").append(lazyLoadProp).append("] ");
    }
    if (loadDesc != null) {
      msg.append("load[").append(loadDesc).append("] ");
    }
    msg.append("exeMicros[").append(q.getQueryExecutionTimeMicros());
    msg.append("] rows[").append(q.getLoadedRowDetail());
    msg.append("] bind[").append(q.getBindLog()).append("]");
    
    q.getTransaction().logInternal(msg.toString());
  }
  
  private void logFindManySummary(CQuery<?> q)
  {
    SpiQuery<?> query = q.getQueryRequest().getQuery();
    String loadMode = query.getLoadMode();
    String loadDesc = query.getLoadDescription();
    String lazyLoadProp = query.getLazyLoadProperty();
    ObjectGraphNode node = query.getParentNode();
    String originKey;
    String originKey;
    if ((node == null) || (node.getOriginQueryPoint() == null)) {
      originKey = null;
    } else {
      originKey = node.getOriginQueryPoint().getKey();
    }
    StringBuilder msg = new StringBuilder(200);
    msg.append("FindMany ");
    if (loadMode != null) {
      msg.append("mode[").append(loadMode).append("] ");
    }
    msg.append("type[").append(q.getBeanName()).append("] ");
    if (query.isAutofetchTuned()) {
      msg.append("tuned[true] ");
    }
    if (originKey != null) {
      msg.append("origin[").append(originKey).append("] ");
    }
    if (lazyLoadProp != null) {
      msg.append("lazyLoadProp[").append(lazyLoadProp).append("] ");
    }
    if (loadDesc != null) {
      msg.append("load[").append(loadDesc).append("] ");
    }
    msg.append("exeMicros[").append(q.getQueryExecutionTimeMicros());
    msg.append("] rows[").append(q.getLoadedRowDetail());
    msg.append("] name[").append(q.getName());
    msg.append("] predicates[").append(q.getLogWhereSql());
    msg.append("] bind[").append(q.getBindLog()).append("]");
    
    q.getTransaction().logInternal(msg.toString());
  }
}
