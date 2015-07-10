package com.avaje.ebean;

import java.sql.Connection;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

public abstract interface Transaction
{
  public static final int READ_COMMITTED = 2;
  public static final int READ_UNCOMMITTED = 1;
  public static final int REPEATABLE_READ = 4;
  public static final int SERIALIZABLE = 8;
  
  public abstract boolean isReadOnly();
  
  public abstract void setReadOnly(boolean paramBoolean);
  
  public abstract void log(String paramString);
  
  public abstract void setLogLevel(LogLevel paramLogLevel);
  
  public abstract LogLevel getLogLevel();
  
  /**
   * @deprecated
   */
  public abstract void setLoggingOn(boolean paramBoolean);
  
  public abstract void commit()
    throws RollbackException;
  
  public abstract void rollback()
    throws PersistenceException;
  
  public abstract void rollback(Throwable paramThrowable)
    throws PersistenceException;
  
  public abstract void end()
    throws PersistenceException;
  
  public abstract boolean isActive();
  
  public abstract void setPersistCascade(boolean paramBoolean);
  
  public abstract void setBatchMode(boolean paramBoolean);
  
  public abstract void setBatchSize(int paramInt);
  
  public abstract void setBatchGetGeneratedKeys(boolean paramBoolean);
  
  public abstract void setBatchFlushOnMixed(boolean paramBoolean);
  
  public abstract void setBatchFlushOnQuery(boolean paramBoolean);
  
  public abstract boolean isBatchFlushOnQuery();
  
  public abstract void flushBatch()
    throws PersistenceException, OptimisticLockException;
  
  /**
   * @deprecated
   */
  public abstract void batchFlush()
    throws PersistenceException, OptimisticLockException;
  
  public abstract Connection getConnection();
  
  public abstract void addModification(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);
  
  public abstract void putUserObject(String paramString, Object paramObject);
  
  public abstract Object getUserObject(String paramString);
}
