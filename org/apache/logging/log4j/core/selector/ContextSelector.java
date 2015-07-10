package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.List;
import org.apache.logging.log4j.core.LoggerContext;

public abstract interface ContextSelector
{
  public abstract LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean);
  
  public abstract LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean, URI paramURI);
  
  public abstract List<LoggerContext> getLoggerContexts();
  
  public abstract void removeContext(LoggerContext paramLoggerContext);
}
