package com.avaje.ebeaninternal.api;

import com.avaje.ebean.Update;

public abstract interface SpiUpdate<T>
  extends Update<T>
{
  public abstract Class<?> getBeanType();
  
  public abstract OrmUpdateType getOrmUpdateType();
  
  public abstract String getBaseTable();
  
  public abstract String getUpdateStatement();
  
  public abstract int getTimeout();
  
  public abstract boolean isNotifyCache();
  
  public abstract BindParams getBindParams();
  
  public abstract void setGeneratedSql(String paramString);
  
  public static enum OrmUpdateType
  {
    INSERT,  UPDATE,  DELETE,  UNKNOWN;
    
    private OrmUpdateType() {}
  }
}
