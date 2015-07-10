package org.apache.logging.log4j.core.appender;

public abstract interface ManagerFactory<M, T>
{
  public abstract M createManager(String paramString, T paramT);
}
