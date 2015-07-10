package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebean.LogLevel;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.api.DerivedRelationshipData;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.api.TransactionEvent;
import com.avaje.ebeaninternal.server.persist.BatchControl;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

public class JdbcTransaction
  implements SpiTransaction
{
  private static final Logger logger = Logger.getLogger(JdbcTransaction.class.getName());
  private static final String illegalStateMessage = "Transaction is Inactive";
  protected final TransactionManager manager;
  final String id;
  final boolean explicit;
  final boolean autoCommit;
  final TransactionManager.OnQueryOnly onQueryOnly;
  boolean active;
  Connection connection;
  BatchControl batchControl;
  TransactionEvent event;
  PersistenceContext persistenceContext;
  boolean persistCascade = true;
  boolean queryOnly = true;
  boolean localReadOnly;
  LogLevel logLevel;
  boolean batchMode;
  int batchSize = -1;
  boolean batchFlushOnQuery = true;
  Boolean batchGetGeneratedKeys;
  Boolean batchFlushOnMixed;
  int depth = 0;
  HashSet<Object> persistingBeans = new HashSet();
  HashSet<Integer> deletingBeansHash;
  TransactionLogBuffer logBuffer;
  HashMap<Integer, List<DerivedRelationshipData>> derivedRelMap;
  private final Map<String, Object> userObjects = new ConcurrentHashMap();
  
  public JdbcTransaction(String id, boolean explicit, LogLevel logLevel, Connection connection, TransactionManager manager)
  {
    try
    {
      this.active = true;
      this.id = id;
      this.explicit = explicit;
      this.logLevel = logLevel;
      this.manager = manager;
      this.connection = connection;
      this.autoCommit = connection.getAutoCommit();
      if (this.autoCommit) {
        connection.setAutoCommit(false);
      }
      this.onQueryOnly = (manager == null ? TransactionManager.OnQueryOnly.ROLLBACK : manager.getOnQueryOnly());
      this.persistenceContext = new DefaultPersistenceContext();
      
      this.logBuffer = new TransactionLogBuffer(50, id);
    }
    catch (Exception e)
    {
      throw new PersistenceException(e);
    }
  }
  
  public String toString()
  {
    return "Trans[" + this.id + "]";
  }
  
  public List<DerivedRelationshipData> getDerivedRelationship(Object bean)
  {
    if (this.derivedRelMap == null) {
      return null;
    }
    Integer key = Integer.valueOf(System.identityHashCode(bean));
    return (List)this.derivedRelMap.get(key);
  }
  
  public void registerDerivedRelationship(DerivedRelationshipData derivedRelationship)
  {
    if (this.derivedRelMap == null) {
      this.derivedRelMap = new HashMap();
    }
    Integer key = Integer.valueOf(System.identityHashCode(derivedRelationship.getAssocBean()));
    
    List<DerivedRelationshipData> list = (List)this.derivedRelMap.get(key);
    if (list == null)
    {
      list = new ArrayList();
      this.derivedRelMap.put(key, list);
    }
    list.add(derivedRelationship);
  }
  
  public void registerDeleteBean(Integer persistingBean)
  {
    if (this.deletingBeansHash == null) {
      this.deletingBeansHash = new HashSet();
    }
    this.deletingBeansHash.add(persistingBean);
  }
  
  public void unregisterDeleteBean(Integer persistedBean)
  {
    if (this.deletingBeansHash != null) {
      this.deletingBeansHash.remove(persistedBean);
    }
  }
  
  public boolean isRegisteredDeleteBean(Integer persistingBean)
  {
    if (this.deletingBeansHash == null) {
      return false;
    }
    return this.deletingBeansHash.contains(persistingBean);
  }
  
  public void unregisterBean(Object bean)
  {
    this.persistingBeans.remove(bean);
  }
  
  public boolean isRegisteredBean(Object bean)
  {
    return !this.persistingBeans.add(bean);
  }
  
  public int depth(int diff)
  {
    this.depth += diff;
    return this.depth;
  }
  
  public boolean isReadOnly()
  {
    if (!isActive()) {
      throw new IllegalStateException("Transaction is Inactive");
    }
    try
    {
      return this.connection.isReadOnly();
    }
    catch (SQLException e)
    {
      throw new PersistenceException(e);
    }
  }
  
  public void setReadOnly(boolean readOnly)
  {
    if (!isActive()) {
      throw new IllegalStateException("Transaction is Inactive");
    }
    try
    {
      this.localReadOnly = readOnly;
      this.connection.setReadOnly(readOnly);
    }
    catch (SQLException e)
    {
      throw new PersistenceException(e);
    }
  }
  
  public void setBatchMode(boolean batchMode)
  {
    if (!isActive()) {
      throw new IllegalStateException("Transaction is Inactive");
    }
    this.batchMode = batchMode;
  }
  
  public void setBatchGetGeneratedKeys(boolean getGeneratedKeys)
  {
    this.batchGetGeneratedKeys = Boolean.valueOf(getGeneratedKeys);
    if (this.batchControl != null) {
      this.batchControl.setGetGeneratedKeys(Boolean.valueOf(getGeneratedKeys));
    }
  }
  
  public void setBatchFlushOnMixed(boolean batchFlushOnMixed)
  {
    this.batchFlushOnMixed = Boolean.valueOf(batchFlushOnMixed);
    if (this.batchControl != null) {
      this.batchControl.setBatchFlushOnMixed(batchFlushOnMixed);
    }
  }
  
  public int getBatchSize()
  {
    return this.batchSize;
  }
  
  public void setBatchSize(int batchSize)
  {
    this.batchSize = batchSize;
    if (this.batchControl != null) {
      this.batchControl.setBatchSize(batchSize);
    }
  }
  
  public boolean isBatchFlushOnQuery()
  {
    return this.batchFlushOnQuery;
  }
  
  public void setBatchFlushOnQuery(boolean batchFlushOnQuery)
  {
    this.batchFlushOnQuery = batchFlushOnQuery;
  }
  
  public boolean isBatchThisRequest()
  {
    if ((!this.explicit) && (this.depth <= 0)) {
      return false;
    }
    return this.batchMode;
  }
  
  public BatchControl getBatchControl()
  {
    return this.batchControl;
  }
  
  public void setBatchControl(BatchControl batchControl)
  {
    this.queryOnly = false;
    this.batchControl = batchControl;
    if (this.batchGetGeneratedKeys != null) {
      batchControl.setGetGeneratedKeys(this.batchGetGeneratedKeys);
    }
    if (this.batchSize != -1) {
      batchControl.setBatchSize(this.batchSize);
    }
    if (this.batchFlushOnMixed != null) {
      batchControl.setBatchFlushOnMixed(this.batchFlushOnMixed.booleanValue());
    }
  }
  
  public void flushBatch()
  {
    if (!isActive()) {
      throw new IllegalStateException("Transaction is Inactive");
    }
    if (this.batchControl != null) {
      this.batchControl.flush();
    }
  }
  
  public void batchFlush()
  {
    flushBatch();
  }
  
  public PersistenceContext getPersistenceContext()
  {
    return this.persistenceContext;
  }
  
  public void setPersistenceContext(PersistenceContext context)
  {
    if (!isActive()) {
      throw new IllegalStateException("Transaction is Inactive");
    }
    this.persistenceContext = context;
  }
  
  public TransactionEvent getEvent()
  {
    this.queryOnly = false;
    if (this.event == null) {
      this.event = new TransactionEvent();
    }
    return this.event;
  }
  
  public void setLoggingOn(boolean loggingOn)
  {
    if (loggingOn) {
      this.logLevel = LogLevel.SQL;
    } else {
      this.logLevel = LogLevel.NONE;
    }
  }
  
  public boolean isExplicit()
  {
    return this.explicit;
  }
  
  public boolean isLogSql()
  {
    return this.logLevel.ordinal() >= LogLevel.SQL.ordinal();
  }
  
  public boolean isLogSummary()
  {
    return this.logLevel.ordinal() >= LogLevel.SUMMARY.ordinal();
  }
  
  public LogLevel getLogLevel()
  {
    return this.logLevel;
  }
  
  public void setLogLevel(LogLevel logLevel)
  {
    this.logLevel = logLevel;
  }
  
  public void log(String msg)
  {
    if (isLogSummary()) {
      logInternal(msg);
    }
  }
  
  public void logInternal(String msg)
  {
    if ((this.manager != null) && 
      (this.logBuffer.add(msg)))
    {
      this.manager.log(this.logBuffer);
      this.logBuffer = this.logBuffer.newBuffer();
    }
  }
  
  public String getId()
  {
    return this.id;
  }
  
  public Connection getInternalConnection()
  {
    if (!isActive()) {
      throw new IllegalStateException("Transaction is Inactive");
    }
    return this.connection;
  }
  
  public Connection getConnection()
  {
    this.queryOnly = false;
    return getInternalConnection();
  }
  
  protected void deactivate()
  {
    try
    {
      if (this.localReadOnly) {
        this.connection.setReadOnly(false);
      }
    }
    catch (SQLException e)
    {
      logger.log(Level.SEVERE, "Error setting to readOnly?", e);
    }
    try
    {
      if (this.autoCommit) {
        this.connection.setAutoCommit(true);
      }
    }
    catch (SQLException e)
    {
      logger.log(Level.SEVERE, "Error setting to readOnly?", e);
    }
    try
    {
      this.connection.close();
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, "Error closing connection", ex);
    }
    this.connection = null;
    this.active = false;
  }
  
  public TransactionLogBuffer getLogBuffer()
  {
    return this.logBuffer;
  }
  
  protected void notifyCommit()
  {
    if (this.manager == null) {
      return;
    }
    if (this.queryOnly) {
      this.manager.notifyOfQueryOnly(true, this, null);
    } else {
      this.manager.notifyOfCommit(this);
    }
  }
  
  private void commitQueryOnly()
  {
    try
    {
      switch (this.onQueryOnly)
      {
      case ROLLBACK: 
        this.connection.rollback();
        break;
      case COMMIT: 
        this.connection.commit();
        break;
      case CLOSE_ON_READCOMMITTED: 
        break;
      default: 
        this.connection.rollback();
      }
    }
    catch (SQLException e)
    {
      String m = "Error when ending a query only transaction via " + this.onQueryOnly;
      logger.log(Level.SEVERE, m, e);
    }
  }
  
  public void commit()
    throws RollbackException
  {
    if (!isActive()) {
      throw new IllegalStateException("Transaction is Inactive");
    }
    try
    {
      if (this.queryOnly)
      {
        commitQueryOnly();
      }
      else
      {
        if ((this.batchControl != null) && (!this.batchControl.isEmpty())) {
          this.batchControl.flush();
        }
        this.connection.commit();
      }
      deactivate();
      notifyCommit();
    }
    catch (Exception e)
    {
      throw new RollbackException(e);
    }
  }
  
  protected void notifyRollback(Throwable cause)
  {
    if (this.manager == null) {
      return;
    }
    if (this.queryOnly) {
      this.manager.notifyOfQueryOnly(false, this, cause);
    } else {
      this.manager.notifyOfRollback(this, cause);
    }
  }
  
  public void rollback()
    throws PersistenceException
  {
    rollback(null);
  }
  
  public void rollback(Throwable cause)
    throws PersistenceException
  {
    if (!isActive()) {
      throw new IllegalStateException("Transaction is Inactive");
    }
    try
    {
      this.connection.rollback();
      
      deactivate();
      notifyRollback(cause);
    }
    catch (Exception ex)
    {
      throw new PersistenceException(ex);
    }
  }
  
  public void end()
    throws PersistenceException
  {
    if (isActive()) {
      rollback();
    }
  }
  
  public boolean isActive()
  {
    return this.active;
  }
  
  public boolean isPersistCascade()
  {
    return this.persistCascade;
  }
  
  public void setPersistCascade(boolean persistCascade)
  {
    this.persistCascade = persistCascade;
  }
  
  public void addModification(String tableName, boolean inserts, boolean updates, boolean deletes)
  {
    getEvent().add(tableName, inserts, updates, deletes);
  }
  
  public void putUserObject(String name, Object value)
  {
    this.userObjects.put(name, value);
  }
  
  public Object getUserObject(String name)
  {
    return this.userObjects.get(name);
  }
  
  public final TransactionManager getTransactionManger()
  {
    return this.manager;
  }
}
