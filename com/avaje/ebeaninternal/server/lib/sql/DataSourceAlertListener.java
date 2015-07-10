package com.avaje.ebeaninternal.server.lib.sql;

public abstract interface DataSourceAlertListener
{
  public abstract void dataSourceDown(String paramString);
  
  public abstract void dataSourceUp(String paramString);
  
  public abstract void warning(String paramString1, String paramString2);
}
