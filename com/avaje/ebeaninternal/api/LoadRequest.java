package com.avaje.ebeaninternal.api;

import com.avaje.ebean.Transaction;

public abstract class LoadRequest
{
  protected final boolean lazy;
  protected final int batchSize;
  protected final Transaction transaction;
  
  public LoadRequest(Transaction transaction, int batchSize, boolean lazy)
  {
    this.transaction = transaction;
    this.batchSize = batchSize;
    this.lazy = lazy;
  }
  
  public boolean isLazy()
  {
    return this.lazy;
  }
  
  public int getBatchSize()
  {
    return this.batchSize;
  }
  
  public Transaction getTransaction()
  {
    return this.transaction;
  }
}
