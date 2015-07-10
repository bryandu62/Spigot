package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebean.config.ExternalTransactionManager;
import com.avaje.ebeaninternal.api.SpiTransaction;

public class ExternalTransactionScopeManager
  extends TransactionScopeManager
{
  final ExternalTransactionManager externalManager;
  
  public ExternalTransactionScopeManager(TransactionManager transactionManager, ExternalTransactionManager externalManager)
  {
    super(transactionManager);
    this.externalManager = externalManager;
  }
  
  public void commit()
  {
    DefaultTransactionThreadLocal.commit(this.serverName);
  }
  
  public void end()
  {
    DefaultTransactionThreadLocal.end(this.serverName);
  }
  
  public SpiTransaction get()
  {
    return (SpiTransaction)this.externalManager.getCurrentTransaction();
  }
  
  public void replace(SpiTransaction trans)
  {
    DefaultTransactionThreadLocal.replace(this.serverName, trans);
  }
  
  public void rollback()
  {
    DefaultTransactionThreadLocal.rollback(this.serverName);
  }
  
  public void set(SpiTransaction trans)
  {
    DefaultTransactionThreadLocal.set(this.serverName, trans);
  }
}
