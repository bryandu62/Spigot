package org.apache.logging.log4j.core;

import java.io.Serializable;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.message.Message;

public abstract interface LogEvent
  extends Serializable
{
  public abstract Level getLevel();
  
  public abstract String getLoggerName();
  
  public abstract StackTraceElement getSource();
  
  public abstract Message getMessage();
  
  public abstract Marker getMarker();
  
  public abstract String getThreadName();
  
  public abstract long getMillis();
  
  public abstract Throwable getThrown();
  
  public abstract Map<String, String> getContextMap();
  
  public abstract ThreadContext.ContextStack getContextStack();
  
  public abstract String getFQCN();
  
  public abstract boolean isIncludeLocation();
  
  public abstract void setIncludeLocation(boolean paramBoolean);
  
  public abstract boolean isEndOfBatch();
  
  public abstract void setEndOfBatch(boolean paramBoolean);
}
