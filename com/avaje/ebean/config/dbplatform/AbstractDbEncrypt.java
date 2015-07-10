package com.avaje.ebean.config.dbplatform;

public abstract class AbstractDbEncrypt
  implements DbEncrypt
{
  protected DbEncryptFunction varcharEncryptFunction;
  protected DbEncryptFunction dateEncryptFunction;
  protected DbEncryptFunction timestampEncryptFunction;
  
  public DbEncryptFunction getDbEncryptFunction(int jdbcType)
  {
    switch (jdbcType)
    {
    case 12: 
      return this.varcharEncryptFunction;
    case 2005: 
      return this.varcharEncryptFunction;
    case 1: 
      return this.varcharEncryptFunction;
    case -1: 
      return this.varcharEncryptFunction;
    case 91: 
      return this.dateEncryptFunction;
    case 93: 
      return this.timestampEncryptFunction;
    }
    return null;
  }
  
  public int getEncryptDbType()
  {
    return -3;
  }
  
  public boolean isBindEncryptDataFirst()
  {
    return true;
  }
}
