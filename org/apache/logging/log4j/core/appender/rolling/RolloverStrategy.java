package org.apache.logging.log4j.core.appender.rolling;

public abstract interface RolloverStrategy
{
  public abstract RolloverDescription rollover(RollingFileManager paramRollingFileManager)
    throws SecurityException;
}
