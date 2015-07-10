package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebeaninternal.server.core.PstmtBatch;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BatchedPstmt
{
  private PreparedStatement pstmt;
  private final boolean isGenKeys;
  private final ArrayList<BatchPostExecute> list = new ArrayList();
  private final String sql;
  private final PstmtBatch pstmtBatch;
  private final boolean occCheck;
  
  public BatchedPstmt(PreparedStatement pstmt, boolean isGenKeys, String sql, PstmtBatch pstmtBatch, boolean occCheck)
  {
    this.pstmt = pstmt;
    this.isGenKeys = isGenKeys;
    this.sql = sql;
    this.pstmtBatch = pstmtBatch;
    this.occCheck = occCheck;
  }
  
  public int size()
  {
    return this.list.size();
  }
  
  public String getSql()
  {
    return this.sql;
  }
  
  public PreparedStatement getStatement()
  {
    return this.pstmt;
  }
  
  public void add(BatchPostExecute batchExecute)
  {
    this.list.add(batchExecute);
  }
  
  public void executeBatch(boolean getGeneratedKeys)
    throws SQLException
  {
    executeAndCheckRowCounts();
    if ((this.isGenKeys) && (getGeneratedKeys)) {
      getGeneratedKeys();
    }
    postExecute();
    close();
  }
  
  public void close()
    throws SQLException
  {
    if (this.pstmt != null)
    {
      this.pstmt.close();
      this.pstmt = null;
    }
  }
  
  private void postExecute()
    throws SQLException
  {
    for (int i = 0; i < this.list.size(); i++) {
      ((BatchPostExecute)this.list.get(i)).postExecute();
    }
  }
  
  private void executeAndCheckRowCounts()
    throws SQLException
  {
    if (this.pstmtBatch != null)
    {
      int rc = this.pstmtBatch.executeBatch(this.pstmt, this.list.size(), this.sql, this.occCheck);
      if (this.list.size() == 1) {
        ((BatchPostExecute)this.list.get(0)).checkRowCount(rc);
      }
      return;
    }
    int[] results = this.pstmt.executeBatch();
    if (results.length != this.list.size())
    {
      String s = "results array error " + results.length + " " + this.list.size();
      throw new SQLException(s);
    }
    for (int i = 0; i < results.length; i++) {
      ((BatchPostExecute)this.list.get(i)).checkRowCount(results[i]);
    }
  }
  
  private void getGeneratedKeys()
    throws SQLException
  {
    int index = 0;
    ResultSet rset = this.pstmt.getGeneratedKeys();
    try
    {
      while (rset.next())
      {
        Object idValue = rset.getObject(1);
        ((BatchPostExecute)this.list.get(index)).setGeneratedKey(idValue);
        index++;
      }
    }
    finally
    {
      if (rset != null) {
        rset.close();
      }
    }
  }
}
