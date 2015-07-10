package com.avaje.ebean.event;

import com.avaje.ebean.Transaction;

public abstract class TransactionEventListenerAdapter
  implements TransactionEventListener
{
  public void postTransactionCommit(Transaction tx) {}
  
  public void postTransactionRollback(Transaction tx, Throwable cause) {}
}
