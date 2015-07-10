package org.apache.logging.log4j.core;

public abstract interface LifeCycle
{
  public abstract void start();
  
  public abstract void stop();
  
  public abstract boolean isStarted();
}
