package com.avaje.ebean.config;

public abstract interface EncryptKeyManager
{
  public abstract void initialise();
  
  public abstract EncryptKey getEncryptKey(String paramString1, String paramString2);
}
