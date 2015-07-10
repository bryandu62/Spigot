package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.api.SpiTransactionScopeManager;

public abstract class TransactionScopeManager
  implements SpiTransactionScopeManager
{
  protected final TransactionManager transactionManager;
  protected final String serverName;
  
  public TransactionScopeManager(TransactionManager transactionManager)
  {
    this.transactionManager = transactionManager;
    this.serverName = transactionManager.getServerName();
  }
  
  public abstract SpiTransaction get();
  
  public abstract void set(SpiTransaction paramSpiTransaction);
  
  public abstract void commit();
  
  public abstract void rollback();
  
  public abstract void end();
  
  public abstract void replace(SpiTransaction paramSpiTransaction);
}
