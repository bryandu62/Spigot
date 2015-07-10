package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.core.PstmtBatch;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PstmtFactory
{
  private final PstmtBatch pstmtBatch;
  
  public PstmtFactory(PstmtBatch pstmtBatch)
  {
    this.pstmtBatch = pstmtBatch;
  }
  
  public CallableStatement getCstmt(SpiTransaction t, String sql)
    throws SQLException
  {
    Connection conn = t.getInternalConnection();
    return conn.prepareCall(sql);
  }
  
  public PreparedStatement getPstmt(SpiTransaction t, String sql)
    throws SQLException
  {
    Connection conn = t.getInternalConnection();
    return conn.prepareStatement(sql);
  }
  
  public PreparedStatement getPstmt(SpiTransaction t, boolean logSql, String sql, BatchPostExecute batchExe)
    throws SQLException
  {
    BatchedPstmtHolder batch = t.getBatchControl().getPstmtHolder();
    PreparedStatement stmt = batch.getStmt(sql, batchExe);
    if (stmt != null) {
      return stmt;
    }
    if (logSql) {
      t.logInternal(sql);
    }
    Connection conn = t.getInternalConnection();
    stmt = conn.prepareStatement(sql);
    if (this.pstmtBatch != null) {
      this.pstmtBatch.setBatchSize(stmt, t.getBatchControl().getBatchSize());
    }
    BatchedPstmt bs = new BatchedPstmt(stmt, false, sql, this.pstmtBatch, false);
    batch.addStmt(bs, batchExe);
    return stmt;
  }
  
  public CallableStatement getCstmt(SpiTransaction t, boolean logSql, String sql, BatchPostExecute batchExe)
    throws SQLException
  {
    BatchedPstmtHolder batch = t.getBatchControl().getPstmtHolder();
    CallableStatement stmt = (CallableStatement)batch.getStmt(sql, batchExe);
    if (stmt != null) {
      return stmt;
    }
    if (logSql) {
      t.logInternal(sql);
    }
    Connection conn = t.getInternalConnection();
    stmt = conn.prepareCall(sql);
    
    BatchedPstmt bs = new BatchedPstmt(stmt, false, sql, this.pstmtBatch, false);
    batch.addStmt(bs, batchExe);
    return stmt;
  }
}
