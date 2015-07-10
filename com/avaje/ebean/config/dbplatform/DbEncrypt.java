package com.avaje.ebean.config.dbplatform;

public abstract interface DbEncrypt
{
  public abstract DbEncryptFunction getDbEncryptFunction(int paramInt);
  
  public abstract int getEncryptDbType();
  
  public abstract boolean isBindEncryptDataFirst();
}
