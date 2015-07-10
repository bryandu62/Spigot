package com.avaje.ebean.config.dbplatform;

import com.avaje.ebean.BackgroundExecutor;
import com.avaje.ebean.Transaction;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;

public abstract class SequenceIdGenerator
  implements IdGenerator
{
  protected static final Logger logger = Logger.getLogger(SequenceIdGenerator.class.getName());
  protected final Object monitor = new Object();
  protected final Object backgroundLoadMonitor = new Object();
  protected final String seqName;
  protected final DataSource dataSource;
  protected final BackgroundExecutor backgroundExecutor;
  protected final ArrayList<Integer> idList = new ArrayList(50);
  protected int batchSize;
  protected int currentlyBackgroundLoading;
  
  public SequenceIdGenerator(BackgroundExecutor be, DataSource ds, String seqName, int batchSize)
  {
    this.backgroundExecutor = be;
    this.dataSource = ds;
    this.seqName = seqName;
    this.batchSize = batchSize;
  }
  
  public abstract String getSql(int paramInt);
  
  public String getName()
  {
    return this.seqName;
  }
  
  public boolean isDbSequence()
  {
    return true;
  }
  
  public void preAllocateIds(int allocateSize)
  {
    if ((this.batchSize > 1) && (allocateSize > this.batchSize))
    {
      if (allocateSize > 100) {
        allocateSize = 100;
      }
      loadLargeAllocation(allocateSize);
    }
  }
  
  protected void loadLargeAllocation(final int allocateSize)
  {
    this.backgroundExecutor.execute(new Runnable()
    {
      public void run()
      {
        SequenceIdGenerator.this.loadMoreIds(allocateSize, null);
      }
    });
  }
  
  public Object nextId(Transaction t)
  {
    synchronized (this.monitor)
    {
      if (this.idList.size() == 0) {
        loadMoreIds(this.batchSize, t);
      }
      Integer nextId = (Integer)this.idList.remove(0);
      if ((this.batchSize > 1) && 
        (this.idList.size() <= this.batchSize / 2)) {
        loadBatchInBackground();
      }
      return nextId;
    }
  }
  
  protected void loadBatchInBackground()
  {
    synchronized (this.backgroundLoadMonitor)
    {
      if (this.currentlyBackgroundLoading > 0)
      {
        if (logger.isLoggable(Level.FINE)) {
          logger.log(Level.FINE, "... skip background sequence load (another load in progress)");
        }
        return;
      }
      this.currentlyBackgroundLoading = this.batchSize;
      
      this.backgroundExecutor.execute(new Runnable()
      {
        public void run()
        {
          SequenceIdGenerator.this.loadMoreIds(SequenceIdGenerator.this.batchSize, null);
          synchronized (SequenceIdGenerator.this.backgroundLoadMonitor)
          {
            SequenceIdGenerator.this.currentlyBackgroundLoading = 0;
          }
        }
      });
    }
  }
  
  protected void loadMoreIds(int numberToLoad, Transaction t)
  {
    ArrayList<Integer> newIds = getMoreIds(numberToLoad, t);
    if (logger.isLoggable(Level.FINE)) {
      logger.log(Level.FINE, "... seq:" + this.seqName + " loaded:" + numberToLoad + " ids:" + newIds);
    }
    synchronized (this.monitor)
    {
      for (int i = 0; i < newIds.size(); i++) {
        this.idList.add(newIds.get(i));
      }
    }
  }
  
  protected ArrayList<Integer> getMoreIds(int loadSize, Transaction t)
  {
    String sql = getSql(loadSize);
    
    ArrayList<Integer> newIds = new ArrayList(loadSize);
    
    boolean useTxnConnection = t != null;
    
    Connection c = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try
    {
      c = useTxnConnection ? t.getConnection() : this.dataSource.getConnection();
      
      pstmt = c.prepareStatement(sql);
      rset = pstmt.executeQuery();
      while (rset.next())
      {
        int val = rset.getInt(1);
        newIds.add(Integer.valueOf(val));
      }
      String m;
      if (newIds.size() == 0)
      {
        m = "Always expecting more than 1 row from " + sql;
        throw new PersistenceException(m);
      }
      return newIds;
    }
    catch (SQLException e)
    {
      if (e.getMessage().contains("Database is already closed"))
      {
        String msg = "Error getting SEQ when DB shutting down " + e.getMessage();
        logger.info(msg);
        System.out.println(msg);
        return newIds;
      }
      throw new PersistenceException("Error getting sequence nextval", e);
    }
    finally
    {
      if (useTxnConnection) {
        closeResources(null, pstmt, rset);
      } else {
        closeResources(c, pstmt, rset);
      }
    }
  }
  
  protected void closeResources(Connection c, PreparedStatement pstmt, ResultSet rset)
  {
    try
    {
      if (rset != null) {
        rset.close();
      }
    }
    catch (SQLException e)
    {
      logger.log(Level.SEVERE, "Error closing ResultSet", e);
    }
    try
    {
      if (pstmt != null) {
        pstmt.close();
      }
    }
    catch (SQLException e)
    {
      logger.log(Level.SEVERE, "Error closing PreparedStatement", e);
    }
    try
    {
      if (c != null) {
        c.close();
      }
    }
    catch (SQLException e)
    {
      logger.log(Level.SEVERE, "Error closing Connection", e);
    }
  }
}
