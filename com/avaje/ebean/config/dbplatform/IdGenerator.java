package com.avaje.ebean.config.dbplatform;

import com.avaje.ebean.Transaction;

public abstract interface IdGenerator
{
  public static final String AUTO_UUID = "auto.uuid";
  
  public abstract String getName();
  
  public abstract boolean isDbSequence();
  
  public abstract Object nextId(Transaction paramTransaction);
  
  public abstract void preAllocateIds(int paramInt);
}
