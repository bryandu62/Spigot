package com.avaje.ebeaninternal.server.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public class BatchedPstmtHolder
{
  private static final Logger logger = Logger.getLogger(BatchedPstmtHolder.class.getName());
  private LinkedHashMap<String, BatchedPstmt> stmtMap = new LinkedHashMap();
  private int maxSize;
  
  public PreparedStatement getStmt(String stmtKey, BatchPostExecute postExecute)
  {
    BatchedPstmt bs = (BatchedPstmt)this.stmtMap.get(stmtKey);
    if (bs == null) {
      return null;
    }
    bs.add(postExecute);
    
    int bsSize = bs.size();
    if (bsSize > this.maxSize) {
      this.maxSize = bsSize;
    }
    return bs.getStatement();
  }
  
  public void addStmt(BatchedPstmt bs, BatchPostExecute postExecute)
  {
    bs.add(postExecute);
    
    this.stmtMap.put(bs.getSql(), bs);
  }
  
  public boolean isEmpty()
  {
    return this.stmtMap.isEmpty();
  }
  
  public void flush(boolean getGeneratedKeys)
    throws PersistenceException
  {
    SQLException firstError = null;
    String errorSql = null;
    
    boolean isError = false;
    
    Iterator<BatchedPstmt> it = this.stmtMap.values().iterator();
    for (;;)
    {
      if (it.hasNext())
      {
        BatchedPstmt bs = (BatchedPstmt)it.next();
        try
        {
          if (!isError) {
            bs.executeBatch(getGeneratedKeys);
          }
          try
          {
            bs.close();
          }
          catch (SQLException ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }
          SQLException next;
        }
        catch (SQLException ex)
        {
          next = ex.getNextException();
          while (next != null)
          {
            logger.log(Level.SEVERE, "Next Exception during batch execution", next);
            next = next.getNextException();
          }
          if (firstError == null)
          {
            firstError = ex;
            errorSql = bs.getSql();
          }
          else
          {
            logger.log(Level.SEVERE, null, ex);
          }
          isError = true;
        }
        finally
        {
          try
          {
            bs.close();
          }
          catch (SQLException ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }
        }
      }
    }
    this.stmtMap.clear();
    this.maxSize = 0;
    if (firstError != null)
    {
      String msg = "Error when batch flush on sql: " + errorSql;
      throw new PersistenceException(msg, firstError);
    }
  }
  
  public int getMaxSize()
  {
    return this.maxSize;
  }
}
