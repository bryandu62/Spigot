package com.avaje.ebean.event;

import com.avaje.ebean.Transaction;

public abstract interface TransactionEventListener
{
  public abstract void postTransactionCommit(Transaction paramTransaction);
  
  public abstract void postTransactionRollback(Transaction paramTransaction, Throwable paramThrowable);
}
