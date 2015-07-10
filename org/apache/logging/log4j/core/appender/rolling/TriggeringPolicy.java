package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.LogEvent;

public abstract interface TriggeringPolicy
{
  public abstract void initialize(RollingFileManager paramRollingFileManager);
  
  public abstract boolean isTriggeringEvent(LogEvent paramLogEvent);
}
