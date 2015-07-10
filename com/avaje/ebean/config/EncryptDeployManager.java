package com.avaje.ebean.config;

public abstract interface EncryptDeployManager
{
  public abstract EncryptDeploy getEncryptDeploy(TableName paramTableName, String paramString);
}
