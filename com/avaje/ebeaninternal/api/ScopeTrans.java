package com.avaje.ebeaninternal.api;

import com.avaje.ebean.TxScope;
import java.util.ArrayList;

public class ScopeTrans
  implements Thread.UncaughtExceptionHandler
{
  private static final int OPCODE_ATHROW = 191;
  private final SpiTransactionScopeManager scopeMgr;
  private final SpiTransaction suspendedTransaction;
  private final SpiTransaction transaction;
  private final boolean rollbackOnChecked;
  private final boolean created;
  private final ArrayList<Class<? extends Throwable>> noRollbackFor;
  private final ArrayList<Class<? extends Throwable>> rollbackFor;
  private final Thread.UncaughtExceptionHandler originalUncaughtHandler;
  private boolean rolledBack;
  
  public ScopeTrans(boolean rollbackOnChecked, boolean created, SpiTransaction transaction, TxScope txScope, SpiTransaction suspendedTransaction, SpiTransactionScopeManager scopeMgr)
  {
    this.rollbackOnChecked = rollbackOnChecked;
    this.created = created;
    this.transaction = transaction;
    this.suspendedTransaction = suspendedTransaction;
    this.scopeMgr = scopeMgr;
    
    this.noRollbackFor = txScope.getNoRollbackFor();
    this.rollbackFor = txScope.getRollbackFor();
    
    Thread t = Thread.currentThread();
    this.originalUncaughtHandler = t.getUncaughtExceptionHandler();
    
    t.setUncaughtExceptionHandler(this);
  }
  
  public void uncaughtException(Thread thread, Throwable e)
  {
    caughtThrowable(e);
    
    onFinally();
    if (this.originalUncaughtHandler != null) {
      this.originalUncaughtHandler.uncaughtException(thread, e);
    }
  }
  
  public void onExit(Object returnOrThrowable, int opCode)
  {
    if (opCode == 191) {
      caughtThrowable((Throwable)returnOrThrowable);
    }
    onFinally();
  }
  
  public void onFinally()
  {
    try
    {
      if (this.originalUncaughtHandler != null) {
        Thread.currentThread().setUncaughtExceptionHandler(this.originalUncaughtHandler);
      }
      if ((!this.rolledBack) && (this.created)) {
        this.transaction.commit();
      }
    }
    finally
    {
      if (this.suspendedTransaction != null) {
        this.scopeMgr.replace(this.suspendedTransaction);
      }
    }
  }
  
  public Error caughtError(Error e)
  {
    rollback(e);
    return e;
  }
  
  public <T extends Throwable> T caughtThrowable(T e)
  {
    if (isRollbackThrowable(e)) {
      rollback(e);
    }
    return e;
  }
  
  private void rollback(Throwable e)
  {
    if ((this.transaction != null) && (this.transaction.isActive())) {
      this.transaction.rollback(e);
    }
    this.rolledBack = true;
  }
  
  private boolean isRollbackThrowable(Throwable e)
  {
    if ((e instanceof Error)) {
      return true;
    }
    if (this.noRollbackFor != null) {
      for (int i = 0; i < this.noRollbackFor.size(); i++) {
        if (((Class)this.noRollbackFor.get(i)).equals(e.getClass())) {
          return false;
        }
      }
    }
    if (this.rollbackFor != null) {
      for (int i = 0; i < this.rollbackFor.size(); i++) {
        if (((Class)this.rollbackFor.get(i)).equals(e.getClass())) {
          return true;
        }
      }
    }
    if ((e instanceof RuntimeException)) {
      return true;
    }
    return this.rollbackOnChecked;
  }
}
