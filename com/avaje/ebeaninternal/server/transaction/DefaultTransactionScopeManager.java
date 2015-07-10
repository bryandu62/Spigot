package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.api.SpiTransaction;

public class DefaultTransactionScopeManager
  extends TransactionScopeManager
{
  public DefaultTransactionScopeManager(TransactionManager transactionManager)
  {
    super(transactionManager);
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
    SpiTransaction t = DefaultTransactionThreadLocal.get(this.serverName);
    if ((t == null) || (!t.isActive())) {
      return null;
    }
    return t;
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
