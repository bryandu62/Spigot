package org.apache.logging.log4j.core;

import java.io.Serializable;

public abstract interface Appender
  extends LifeCycle
{
  public abstract void append(LogEvent paramLogEvent);
  
  public abstract String getName();
  
  public abstract Layout<? extends Serializable> getLayout();
  
  public abstract boolean ignoreExceptions();
  
  public abstract ErrorHandler getHandler();
  
  public abstract void setHandler(ErrorHandler paramErrorHandler);
}
