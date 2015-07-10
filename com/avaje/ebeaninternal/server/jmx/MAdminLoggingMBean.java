package com.avaje.ebeaninternal.server.jmx;

import com.avaje.ebean.LogLevel;

public abstract interface MAdminLoggingMBean
{
  public abstract LogLevel getLogLevel();
  
  public abstract void setLogLevel(LogLevel paramLogLevel);
  
  public abstract boolean isDebugGeneratedSql();
  
  public abstract void setDebugGeneratedSql(boolean paramBoolean);
  
  public abstract boolean isDebugLazyLoad();
  
  public abstract void setDebugLazyLoad(boolean paramBoolean);
}
