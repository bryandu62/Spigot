package com.avaje.ebean;

import java.util.ArrayList;

public final class TxScope
{
  TxType type;
  String serverName;
  TxIsolation isolation;
  boolean readOnly;
  ArrayList<Class<? extends Throwable>> rollbackFor;
  ArrayList<Class<? extends Throwable>> noRollbackFor;
  
  public static TxScope required()
  {
    return new TxScope(TxType.REQUIRED);
  }
  
  public static TxScope requiresNew()
  {
    return new TxScope(TxType.REQUIRES_NEW);
  }
  
  public static TxScope mandatory()
  {
    return new TxScope(TxType.MANDATORY);
  }
  
  public static TxScope supports()
  {
    return new TxScope(TxType.SUPPORTS);
  }
  
  public static TxScope notSupported()
  {
    return new TxScope(TxType.NOT_SUPPORTED);
  }
  
  public static TxScope never()
  {
    return new TxScope(TxType.NEVER);
  }
  
  public TxScope()
  {
    this.type = TxType.REQUIRED;
  }
  
  public TxScope(TxType type)
  {
    this.type = type;
  }
  
  public String toString()
  {
    return "TxScope[" + this.type + "] readOnly[" + this.readOnly + "] isolation[" + this.isolation + "] serverName[" + this.serverName + "] rollbackFor[" + this.rollbackFor + "] noRollbackFor[" + this.noRollbackFor + "]";
  }
  
  public TxType getType()
  {
    return this.type;
  }
  
  public TxScope setType(TxType type)
  {
    this.type = type;
    return this;
  }
  
  public boolean isReadonly()
  {
    return this.readOnly;
  }
  
  public TxScope setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
    return this;
  }
  
  public TxIsolation getIsolation()
  {
    return this.isolation;
  }
  
  public TxScope setIsolation(TxIsolation isolation)
  {
    this.isolation = isolation;
    return this;
  }
  
  public String getServerName()
  {
    return this.serverName;
  }
  
  public TxScope setServerName(String serverName)
  {
    this.serverName = serverName;
    return this;
  }
  
  public ArrayList<Class<? extends Throwable>> getRollbackFor()
  {
    return this.rollbackFor;
  }
  
  public TxScope setRollbackFor(Class<? extends Throwable> rollbackThrowable)
  {
    if (this.rollbackFor == null) {
      this.rollbackFor = new ArrayList(2);
    }
    this.rollbackFor.add(rollbackThrowable);
    return this;
  }
  
  public TxScope setRollbackFor(Class<?>[] rollbackThrowables)
  {
    if (this.rollbackFor == null) {
      this.rollbackFor = new ArrayList(rollbackThrowables.length);
    }
    for (int i = 0; i < rollbackThrowables.length; i++) {
      this.rollbackFor.add(rollbackThrowables[i]);
    }
    return this;
  }
  
  public ArrayList<Class<? extends Throwable>> getNoRollbackFor()
  {
    return this.noRollbackFor;
  }
  
  public TxScope setNoRollbackFor(Class<? extends Throwable> noRollback)
  {
    if (this.noRollbackFor == null) {
      this.noRollbackFor = new ArrayList(2);
    }
    this.noRollbackFor.add(noRollback);
    return this;
  }
  
  public TxScope setNoRollbackFor(Class<?>[] noRollbacks)
  {
    if (this.noRollbackFor == null) {
      this.noRollbackFor = new ArrayList(noRollbacks.length);
    }
    for (int i = 0; i < noRollbacks.length; i++) {
      this.noRollbackFor.add(noRollbacks[i]);
    }
    return this;
  }
}
