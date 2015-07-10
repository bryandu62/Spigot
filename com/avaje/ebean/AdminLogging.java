package com.avaje.ebean;

public abstract interface AdminLogging
{
  public abstract void setLogLevel(LogLevel paramLogLevel);
  
  public abstract LogLevel getLogLevel();
  
  public abstract boolean isDebugGeneratedSql();
  
  public abstract void setDebugGeneratedSql(boolean paramBoolean);
  
  public abstract boolean isDebugLazyLoad();
  
  public abstract void setDebugLazyLoad(boolean paramBoolean);
}
