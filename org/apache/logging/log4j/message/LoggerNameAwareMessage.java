package org.apache.logging.log4j.message;

public abstract interface LoggerNameAwareMessage
{
  public abstract void setLoggerName(String paramString);
  
  public abstract String getLoggerName();
}
