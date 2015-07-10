package com.avaje.ebeaninternal.server.transaction;

public abstract interface TransactionLogWriter
{
  public abstract void log(TransactionLogBuffer paramTransactionLogBuffer);
  
  public abstract void shutdown();
}
