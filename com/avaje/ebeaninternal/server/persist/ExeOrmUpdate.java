package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebeaninternal.api.BindParams;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.api.SpiUpdate;
import com.avaje.ebeaninternal.server.core.PersistRequestOrmUpdate;
import com.avaje.ebeaninternal.server.core.PstmtBatch;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.type.DataBind;
import com.avaje.ebeaninternal.server.util.BindParamsParser;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public class ExeOrmUpdate
{
  private static final Logger logger = Logger.getLogger(ExeOrmUpdate.class.getName());
  private final Binder binder;
  private final PstmtFactory pstmtFactory;
  
  public ExeOrmUpdate(Binder binder, PstmtBatch pstmtBatch)
  {
    this.pstmtFactory = new PstmtFactory(pstmtBatch);
    this.binder = binder;
  }
  
  public int execute(PersistRequestOrmUpdate request)
  {
    SpiTransaction t = request.getTransaction();
    
    boolean batchThisRequest = t.isBatchThisRequest();
    
    PreparedStatement pstmt = null;
    try
    {
      pstmt = bindStmt(request, batchThisRequest);
      if (batchThisRequest)
      {
        PstmtBatch pstmtBatch = request.getPstmtBatch();
        if (pstmtBatch != null) {
          pstmtBatch.addBatch(pstmt);
        } else {
          pstmt.addBatch();
        }
        return -1;
      }
      SpiUpdate<?> ormUpdate = request.getOrmUpdate();
      if (ormUpdate.getTimeout() > 0) {
        pstmt.setQueryTimeout(ormUpdate.getTimeout());
      }
      int rowCount = pstmt.executeUpdate();
      request.checkRowCount(rowCount);
      request.postExecute();
      return rowCount;
    }
    catch (SQLException ex)
    {
      Object ormUpdate = request.getOrmUpdate();
      String msg = "Error executing: " + ((SpiUpdate)ormUpdate).getGeneratedSql();
      throw new PersistenceException(msg, ex);
    }
    finally
    {
      if ((!batchThisRequest) && (pstmt != null)) {
        try
        {
          pstmt.close();
        }
        catch (SQLException e)
        {
          logger.log(Level.SEVERE, null, e);
        }
      }
    }
  }
  
  private String translate(PersistRequestOrmUpdate request, String sql)
  {
    BeanDescriptor<?> descriptor = request.getBeanDescriptor();
    return descriptor.convertOrmUpdateToSql(sql);
  }
  
  private PreparedStatement bindStmt(PersistRequestOrmUpdate request, boolean batchThisRequest)
    throws SQLException
  {
    SpiUpdate<?> ormUpdate = request.getOrmUpdate();
    SpiTransaction t = request.getTransaction();
    
    String sql = ormUpdate.getUpdateStatement();
    
    sql = translate(request, sql);
    
    BindParams bindParams = ormUpdate.getBindParams();
    
    sql = BindParamsParser.parse(bindParams, sql);
    
    ormUpdate.setGeneratedSql(sql);
    
    boolean logSql = request.isLogSql();
    PreparedStatement pstmt;
    PreparedStatement pstmt;
    if (batchThisRequest)
    {
      pstmt = this.pstmtFactory.getPstmt(t, logSql, sql, request);
    }
    else
    {
      if (logSql) {
        t.logInternal(sql);
      }
      pstmt = this.pstmtFactory.getPstmt(t, sql);
    }
    String bindLog = null;
    if (!bindParams.isEmpty()) {
      bindLog = this.binder.bind(bindParams, new DataBind(pstmt));
    }
    request.setBindLog(bindLog);
    
    return pstmt;
  }
}
