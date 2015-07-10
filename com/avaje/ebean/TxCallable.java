package com.avaje.ebean;

public abstract interface TxCallable<T>
{
  public abstract T call();
}
