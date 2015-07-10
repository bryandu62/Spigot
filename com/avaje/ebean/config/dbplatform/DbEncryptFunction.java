package com.avaje.ebean.config.dbplatform;

public abstract interface DbEncryptFunction
{
  public abstract String getDecryptSql(String paramString);
  
  public abstract String getEncryptBindSql();
}
