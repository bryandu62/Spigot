package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.api.SpiTransaction;

public final class DefaultTransactionThreadLocal
{
  private static ThreadLocal<TransactionMap> local = new ThreadLocal()
  {
    protected synchronized TransactionMap initialValue()
    {
      return new TransactionMap();
    }
  };
  
  private static TransactionMap.State getState(String serverName)
  {
    return ((TransactionMap)local.get()).getStateWithCreate(serverName);
  }
  
  public static void set(String serverName, SpiTransaction trans)
  {
    getState(serverName).set(trans);
  }
  
  public static void replace(String serverName, SpiTransaction trans)
  {
    getState(serverName).replace(trans);
  }
  
  public static SpiTransaction get(String serverName)
  {
    TransactionMap map = (TransactionMap)local.get();
    TransactionMap.State state = map.getState(serverName);
    SpiTransaction t = state == null ? null : state.transaction;
    if (map.isEmpty()) {
      local.remove();
    }
    return t;
  }
  
  public static void commit(String serverName)
  {
    TransactionMap map = (TransactionMap)local.get();
    TransactionMap.State state = map.removeState(serverName);
    if (state == null) {
      throw new IllegalStateException("No current transaction for [" + serverName + "]");
    }
    state.commit();
    if (map.isEmpty()) {
      local.remove();
    }
  }
  
  public static void rollback(String serverName)
  {
    TransactionMap map = (TransactionMap)local.get();
    TransactionMap.State state = map.removeState(serverName);
    if (state == null) {
      throw new IllegalStateException("No current transaction for [" + serverName + "]");
    }
    state.rollback();
    if (map.isEmpty()) {
      local.remove();
    }
  }
  
  public static void end(String serverName)
  {
    TransactionMap map = (TransactionMap)local.get();
    TransactionMap.State state = map.removeState(serverName);
    if (state != null) {
      state.end();
    }
    if (map.isEmpty()) {
      local.remove();
    }
  }
}
