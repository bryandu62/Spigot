package com.avaje.ebeaninternal.server.lib.sql;

public abstract interface DataSourceNotify
{
  public abstract void notifyDataSourceUp(String paramString);
  
  public abstract void notifyDataSourceDown(String paramString);
  
  public abstract void notifyWarning(String paramString1, String paramString2);
}
