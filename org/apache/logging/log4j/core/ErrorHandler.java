package org.apache.logging.log4j.core;

public abstract interface ErrorHandler
{
  public abstract void error(String paramString);
  
  public abstract void error(String paramString, Throwable paramThrowable);
  
  public abstract void error(String paramString, LogEvent paramLogEvent, Throwable paramThrowable);
}
