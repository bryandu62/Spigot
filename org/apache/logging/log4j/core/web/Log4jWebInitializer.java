package org.apache.logging.log4j.core.web;

import javax.servlet.UnavailableException;

abstract interface Log4jWebInitializer
{
  public static final String LOG4J_CONTEXT_NAME = "log4jContextName";
  public static final String LOG4J_CONFIG_LOCATION = "log4jConfiguration";
  public static final String IS_LOG4J_CONTEXT_SELECTOR_NAMED = "isLog4jContextSelectorNamed";
  public static final String INITIALIZER_ATTRIBUTE = Log4jWebInitializer.class.getName() + ".INSTANCE";
  
  public abstract void initialize()
    throws UnavailableException;
  
  public abstract void deinitialize();
  
  public abstract void setLoggerContext();
  
  public abstract void clearLoggerContext();
}
