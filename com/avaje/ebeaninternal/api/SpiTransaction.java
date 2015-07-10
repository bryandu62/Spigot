package com.avaje.ebeaninternal.api;

import com.avaje.ebean.Transaction;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.server.persist.BatchControl;
import com.avaje.ebeaninternal.server.transaction.TransactionLogBuffer;
import java.sql.Connection;
import java.util.List;

public abstract interface SpiTransaction
  extends Transaction
{
  public abstract boolean isLogSql();
  
  public abstract boolean isLogSummary();
  
  public abstract void logInternal(String paramString);
  
  public abstract TransactionLogBuffer getLogBuffer();
  
  public abstract void registerDerivedRelationship(DerivedRelationshipData paramDerivedRelationshipData);
  
  public abstract List<DerivedRelationshipData> getDerivedRelationship(Object paramObject);
  
  public abstract void registerDeleteBean(Integer paramInteger);
  
  public abstract void unregisterDeleteBean(Integer paramInteger);
  
  public abstract boolean isRegisteredDeleteBean(Integer paramInteger);
  
  public abstract void unregisterBean(Object paramObject);
  
  public abstract boolean isRegisteredBean(Object paramObject);
  
  public abstract String getId();
  
  public abstract int getBatchSize();
  
  public abstract int depth(int paramInt);
  
  public abstract boolean isExplicit();
  
  public abstract TransactionEvent getEvent();
  
  public abstract boolean isPersistCascade();
  
  public abstract boolean isBatchThisRequest();
  
  public abstract BatchControl getBatchControl();
  
  public abstract void setBatchControl(BatchControl paramBatchControl);
  
  public abstract PersistenceContext getPersistenceContext();
  
  public abstract void setPersistenceContext(PersistenceContext paramPersistenceContext);
  
  public abstract Connection getInternalConnection();
}
