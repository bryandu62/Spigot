package com.avaje.ebeaninternal.api;

import com.avaje.ebean.CallableSql;

public abstract interface SpiCallableSql
  extends CallableSql
{
  public abstract BindParams getBindParams();
  
  public abstract TransactionEventTable getTransactionEventTable();
}
