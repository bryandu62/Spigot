package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebean.BackgroundExecutor;
import com.avaje.ebean.LogLevel;
import com.avaje.ebean.TxIsolation;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.event.TransactionEventListener;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.api.TransactionEvent;
import com.avaje.ebeaninternal.api.TransactionEventTable;
import com.avaje.ebeaninternal.api.TransactionEventTable.TableIUD;
import com.avaje.ebeaninternal.server.cluster.ClusterManager;
import com.avaje.ebeaninternal.server.core.BootupClasses;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptorManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;

public class TransactionManager
{
  private static final Logger logger = Logger.getLogger(TransactionManager.class.getName());
  private final BeanDescriptorManager beanDescriptorManager;
  private LogLevel logLevel;
  private final TransactionLogManager transLogger;
  private final String prefix;
  private final String externalTransPrefix;
  private final DataSource dataSource;
  private final OnQueryOnly onQueryOnly;
  private final boolean defaultBatchMode;
  private final BackgroundExecutor backgroundExecutor;
  private final ClusterManager clusterManager;
  private final int commitDebugLevel;
  private final String serverName;
  
  public static enum OnQueryOnly
  {
    ROLLBACK,  CLOSE_ON_READCOMMITTED,  COMMIT;
    
    private OnQueryOnly() {}
  }
  
  private AtomicLong transactionCounter = new AtomicLong(1000L);
  private int clusterDebugLevel;
  private final BulkEventListenerMap bulkEventListenerMap;
  private TransactionEventListener[] transactionEventListeners;
  
  public TransactionManager(ClusterManager clusterManager, BackgroundExecutor backgroundExecutor, ServerConfig config, BeanDescriptorManager descMgr, BootupClasses bootupClasses)
  {
    this.beanDescriptorManager = descMgr;
    this.clusterManager = clusterManager;
    this.serverName = config.getName();
    
    this.logLevel = config.getLoggingLevel();
    this.transLogger = new TransactionLogManager(config);
    this.backgroundExecutor = backgroundExecutor;
    this.dataSource = config.getDataSource();
    this.bulkEventListenerMap = new BulkEventListenerMap(config.getBulkTableEventListeners());
    
    List<TransactionEventListener> transactionEventListeners = bootupClasses.getTransactionEventListeners();
    this.transactionEventListeners = ((TransactionEventListener[])transactionEventListeners.toArray(new TransactionEventListener[transactionEventListeners.size()]));
    
    this.commitDebugLevel = GlobalProperties.getInt("ebean.commit.debuglevel", 0);
    this.clusterDebugLevel = GlobalProperties.getInt("ebean.cluster.debuglevel", 0);
    
    this.defaultBatchMode = config.isPersistBatching();
    
    this.prefix = GlobalProperties.get("transaction.prefix", "");
    this.externalTransPrefix = GlobalProperties.get("transaction.prefix", "e");
    
    String value = GlobalProperties.get("transaction.onqueryonly", "ROLLBACK").toUpperCase().trim();
    this.onQueryOnly = getOnQueryOnly(value, this.dataSource);
  }
  
  public void shutdown()
  {
    this.transLogger.shutdown();
  }
  
  public BeanDescriptorManager getBeanDescriptorManager()
  {
    return this.beanDescriptorManager;
  }
  
  public BulkEventListenerMap getBulkEventListenerMap()
  {
    return this.bulkEventListenerMap;
  }
  
  public LogLevel getTransactionLogLevel()
  {
    return this.logLevel;
  }
  
  public void setTransactionLogLevel(LogLevel logLevel)
  {
    this.logLevel = logLevel;
  }
  
  private OnQueryOnly getOnQueryOnly(String onQueryOnly, DataSource ds)
  {
    if (onQueryOnly.equals("COMMIT")) {
      return OnQueryOnly.COMMIT;
    }
    if (onQueryOnly.startsWith("CLOSE"))
    {
      if (!isReadCommitedIsolation(ds))
      {
        String m = "transaction.queryonlyclose is true but the transaction Isolation Level is not READ_COMMITTED";
        throw new PersistenceException(m);
      }
      return OnQueryOnly.CLOSE_ON_READCOMMITTED;
    }
    return OnQueryOnly.ROLLBACK;
  }
  
  private boolean isReadCommitedIsolation(DataSource ds)
  {
    Connection c = null;
    try
    {
      c = ds.getConnection();
      
      int isolationLevel = c.getTransactionIsolation();
      return isolationLevel == 2;
    }
    catch (SQLException ex)
    {
      String m = "Errored trying to determine the default Isolation Level";
      throw new PersistenceException(m, ex);
    }
    finally
    {
      try
      {
        if (c != null) {
          c.close();
        }
      }
      catch (SQLException ex)
      {
        logger.log(Level.SEVERE, "closing connection", ex);
      }
    }
  }
  
  public String getServerName()
  {
    return this.serverName;
  }
  
  public DataSource getDataSource()
  {
    return this.dataSource;
  }
  
  public int getClusterDebugLevel()
  {
    return this.clusterDebugLevel;
  }
  
  public void setClusterDebugLevel(int clusterDebugLevel)
  {
    this.clusterDebugLevel = clusterDebugLevel;
  }
  
  public OnQueryOnly getOnQueryOnly()
  {
    return this.onQueryOnly;
  }
  
  public TransactionLogManager getLogger()
  {
    return this.transLogger;
  }
  
  public void log(TransactionLogBuffer logBuffer)
  {
    if (!logBuffer.isEmpty()) {
      this.transLogger.log(logBuffer);
    }
  }
  
  public SpiTransaction wrapExternalConnection(Connection c)
  {
    return wrapExternalConnection(this.externalTransPrefix + c.hashCode(), c);
  }
  
  public SpiTransaction wrapExternalConnection(String id, Connection c)
  {
    ExternalJdbcTransaction t = new ExternalJdbcTransaction(id, true, this.logLevel, c, this);
    if (this.defaultBatchMode) {
      t.setBatchMode(true);
    }
    return t;
  }
  
  public SpiTransaction createTransaction(boolean explicit, int isolationLevel)
  {
    Connection c = null;
    try
    {
      c = this.dataSource.getConnection();
      long id = this.transactionCounter.incrementAndGet();
      
      JdbcTransaction t = new JdbcTransaction(this.prefix + id, explicit, this.logLevel, c, this);
      if (this.defaultBatchMode) {
        t.setBatchMode(true);
      }
      if (isolationLevel > -1) {
        c.setTransactionIsolation(isolationLevel);
      }
      if (this.commitDebugLevel >= 3)
      {
        String msg = "Transaction [" + t.getId() + "] begin";
        if (isolationLevel > -1)
        {
          TxIsolation txi = TxIsolation.fromLevel(isolationLevel);
          msg = msg + " isolationLevel[" + txi + "]";
        }
        logger.info(msg);
      }
      return t;
    }
    catch (SQLException ex)
    {
      try
      {
        if (c != null) {
          c.close();
        }
      }
      catch (SQLException e)
      {
        logger.log(Level.SEVERE, "Error closing failed connection", e);
      }
      throw new PersistenceException(ex);
    }
  }
  
  public SpiTransaction createQueryTransaction()
  {
    Connection c = null;
    try
    {
      c = this.dataSource.getConnection();
      long id = this.transactionCounter.incrementAndGet();
      
      JdbcTransaction t = new JdbcTransaction(this.prefix + id, false, this.logLevel, c, this);
      if (this.defaultBatchMode) {
        t.setBatchMode(true);
      }
      if (this.commitDebugLevel >= 3) {
        logger.info("Transaction [" + t.getId() + "] begin - queryOnly");
      }
      return t;
    }
    catch (PersistenceException ex)
    {
      try
      {
        if (c != null) {
          c.close();
        }
      }
      catch (SQLException e)
      {
        logger.log(Level.SEVERE, "Error closing failed connection", e);
      }
      throw ex;
    }
    catch (SQLException ex)
    {
      throw new PersistenceException(ex);
    }
  }
  
  public void notifyOfRollback(SpiTransaction transaction, Throwable cause)
  {
    try
    {
      for (TransactionEventListener listener : this.transactionEventListeners) {
        listener.postTransactionRollback(transaction, cause);
      }
      if ((transaction.isLogSummary()) || (this.commitDebugLevel >= 1))
      {
        String msg = "Rollback";
        if (cause != null) {
          msg = msg + " error: " + formatThrowable(cause);
        }
        if (transaction.isLogSummary()) {
          transaction.logInternal(msg);
        }
        if (this.commitDebugLevel >= 1) {
          logger.info("Transaction [" + transaction.getId() + "] " + msg);
        }
      }
      log(transaction.getLogBuffer());
    }
    catch (Exception ex)
    {
      String m = "Potentially Transaction Log incomplete due to error:";
      logger.log(Level.SEVERE, m, ex);
    }
  }
  
  public void notifyOfQueryOnly(boolean onCommit, SpiTransaction transaction, Throwable cause)
  {
    try
    {
      if (this.commitDebugLevel >= 2)
      {
        String msg;
        String msg;
        if (onCommit)
        {
          msg = "Commit queryOnly";
        }
        else
        {
          msg = "Rollback queryOnly";
          if (cause != null) {
            msg = msg + " error: " + formatThrowable(cause);
          }
        }
        if (transaction.isLogSummary()) {
          transaction.logInternal(msg);
        }
        logger.info("Transaction [" + transaction.getId() + "] " + msg);
      }
      log(transaction.getLogBuffer());
    }
    catch (Exception ex)
    {
      String m = "Potentially Transaction Log incomplete due to error:";
      logger.log(Level.SEVERE, m, ex);
    }
  }
  
  private String formatThrowable(Throwable e)
  {
    if (e == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    formatThrowable(e, sb);
    return sb.toString();
  }
  
  private void formatThrowable(Throwable e, StringBuilder sb)
  {
    sb.append(e.toString());
    StackTraceElement[] stackTrace = e.getStackTrace();
    if (stackTrace.length > 0)
    {
      sb.append(" stack0: ");
      sb.append(stackTrace[0]);
    }
    Throwable cause = e.getCause();
    if (cause != null)
    {
      sb.append(" cause: ");
      formatThrowable(cause, sb);
    }
  }
  
  public void notifyOfCommit(SpiTransaction transaction)
  {
    try
    {
      log(transaction.getLogBuffer());
      
      PostCommitProcessing postCommit = new PostCommitProcessing(this.clusterManager, this, transaction, transaction.getEvent());
      
      postCommit.notifyLocalCacheIndex();
      postCommit.notifyCluster();
      
      this.backgroundExecutor.execute(postCommit.notifyPersistListeners());
      for (TransactionEventListener listener : this.transactionEventListeners) {
        listener.postTransactionCommit(transaction);
      }
      if (this.commitDebugLevel >= 1) {
        logger.info("Transaction [" + transaction.getId() + "] commit");
      }
    }
    catch (Exception ex)
    {
      String m = "NotifyOfCommit failed. Cache/Lucene potentially not notified.";
      logger.log(Level.SEVERE, m, ex);
    }
  }
  
  public void externalModification(TransactionEventTable tableEvents)
  {
    TransactionEvent event = new TransactionEvent();
    event.add(tableEvents);
    
    PostCommitProcessing postCommit = new PostCommitProcessing(this.clusterManager, this, null, event);
    
    postCommit.notifyLocalCacheIndex();
    
    this.backgroundExecutor.execute(postCommit.notifyPersistListeners());
  }
  
  public void remoteTransactionEvent(RemoteTransactionEvent remoteEvent)
  {
    if ((this.clusterDebugLevel > 0) || (logger.isLoggable(Level.FINE))) {
      logger.info("Cluster Received: " + remoteEvent.toString());
    }
    List<TransactionEventTable.TableIUD> tableIUDList = remoteEvent.getTableIUDList();
    if (tableIUDList != null) {
      for (int i = 0; i < tableIUDList.size(); i++)
      {
        TransactionEventTable.TableIUD tableIUD = (TransactionEventTable.TableIUD)tableIUDList.get(i);
        this.beanDescriptorManager.cacheNotify(tableIUD);
      }
    }
    List<BeanPersistIds> beanPersistList = remoteEvent.getBeanPersistList();
    if (beanPersistList != null) {
      for (int i = 0; i < beanPersistList.size(); i++)
      {
        BeanPersistIds beanPersist = (BeanPersistIds)beanPersistList.get(i);
        beanPersist.notifyCacheAndListener();
      }
    }
  }
}
