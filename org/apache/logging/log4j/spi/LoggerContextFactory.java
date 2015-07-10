package org.apache.logging.log4j.spi;

import java.net.URI;

public abstract interface LoggerContextFactory
{
  public abstract LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean);
  
  public abstract LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean, URI paramURI);
  
  public abstract void removeContext(LoggerContext paramLoggerContext);
}
