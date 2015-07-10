package org.apache.logging.log4j.core.config;

import java.util.Map;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.Filterable;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.net.Advertiser;

public abstract interface Configuration
  extends Filterable
{
  public static final String CONTEXT_PROPERTIES = "ContextProperties";
  
  public abstract String getName();
  
  public abstract LoggerConfig getLoggerConfig(String paramString);
  
  public abstract Map<String, Appender> getAppenders();
  
  public abstract Map<String, LoggerConfig> getLoggers();
  
  public abstract void addLoggerAppender(Logger paramLogger, Appender paramAppender);
  
  public abstract void addLoggerFilter(Logger paramLogger, Filter paramFilter);
  
  public abstract void setLoggerAdditive(Logger paramLogger, boolean paramBoolean);
  
  public abstract Map<String, String> getProperties();
  
  public abstract void start();
  
  public abstract void stop();
  
  public abstract void addListener(ConfigurationListener paramConfigurationListener);
  
  public abstract void removeListener(ConfigurationListener paramConfigurationListener);
  
  public abstract StrSubstitutor getStrSubstitutor();
  
  public abstract void createConfiguration(Node paramNode, LogEvent paramLogEvent);
  
  public abstract <T> T getComponent(String paramString);
  
  public abstract void addComponent(String paramString, Object paramObject);
  
  public abstract void setConfigurationMonitor(ConfigurationMonitor paramConfigurationMonitor);
  
  public abstract ConfigurationMonitor getConfigurationMonitor();
  
  public abstract void setAdvertiser(Advertiser paramAdvertiser);
  
  public abstract Advertiser getAdvertiser();
  
  public abstract boolean isShutdownHookEnabled();
}
