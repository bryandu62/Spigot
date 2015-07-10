package org.apache.logging.log4j.core.config;

public abstract interface ConfigurationListener
{
  public abstract void onChange(Reconfigurable paramReconfigurable);
}
