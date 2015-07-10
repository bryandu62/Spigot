package org.apache.logging.log4j.core.appender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(name="Failover", category="Core", elementType="appender", printObject=true)
public final class FailoverAppender
  extends AbstractAppender
{
  private static final int DEFAULT_INTERVAL_SECONDS = 60;
  private final String primaryRef;
  private final String[] failovers;
  private final Configuration config;
  private AppenderControl primary;
  private final List<AppenderControl> failoverAppenders = new ArrayList();
  private final long intervalMillis;
  private long nextCheckMillis = 0L;
  private volatile boolean failure = false;
  
  private FailoverAppender(String name, Filter filter, String primary, String[] failovers, int intervalMillis, Configuration config, boolean ignoreExceptions)
  {
    super(name, filter, null, ignoreExceptions);
    this.primaryRef = primary;
    this.failovers = failovers;
    this.config = config;
    this.intervalMillis = intervalMillis;
  }
  
  public void start()
  {
    Map<String, Appender> map = this.config.getAppenders();
    int errors = 0;
    if (map.containsKey(this.primaryRef))
    {
      this.primary = new AppenderControl((Appender)map.get(this.primaryRef), null, null);
    }
    else
    {
      LOGGER.error("Unable to locate primary Appender " + this.primaryRef);
      errors++;
    }
    for (String name : this.failovers) {
      if (map.containsKey(name)) {
        this.failoverAppenders.add(new AppenderControl((Appender)map.get(name), null, null));
      } else {
        LOGGER.error("Failover appender " + name + " is not configured");
      }
    }
    if (this.failoverAppenders.size() == 0)
    {
      LOGGER.error("No failover appenders are available");
      errors++;
    }
    if (errors == 0) {
      super.start();
    }
  }
  
  public void append(LogEvent event)
  {
    if (!isStarted())
    {
      error("FailoverAppender " + getName() + " did not start successfully");
      return;
    }
    if (!this.failure)
    {
      callAppender(event);
    }
    else
    {
      long currentMillis = System.currentTimeMillis();
      if (currentMillis >= this.nextCheckMillis) {
        callAppender(event);
      } else {
        failover(event, null);
      }
    }
  }
  
  private void callAppender(LogEvent event)
  {
    try
    {
      this.primary.callAppender(event);
    }
    catch (Exception ex)
    {
      this.nextCheckMillis = (System.currentTimeMillis() + this.intervalMillis);
      this.failure = true;
      failover(event, ex);
    }
  }
  
  private void failover(LogEvent event, Exception ex)
  {
    RuntimeException re = ex != null ? new LoggingException(ex) : (ex instanceof LoggingException) ? (LoggingException)ex : null;
    
    boolean written = false;
    Exception failoverException = null;
    for (AppenderControl control : this.failoverAppenders) {
      try
      {
        control.callAppender(event);
        written = true;
      }
      catch (Exception fex)
      {
        if (failoverException == null) {
          failoverException = fex;
        }
      }
    }
    if ((!written) && (!ignoreExceptions()))
    {
      if (re != null) {
        throw re;
      }
      throw new LoggingException("Unable to write to failover appenders", failoverException);
    }
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder(getName());
    sb.append(" primary=").append(this.primary).append(", failover={");
    boolean first = true;
    for (String str : this.failovers)
    {
      if (!first) {
        sb.append(", ");
      }
      sb.append(str);
      first = false;
    }
    sb.append("}");
    return sb.toString();
  }
  
  @PluginFactory
  public static FailoverAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("primary") String primary, @PluginElement("Failovers") String[] failovers, @PluginAttribute("retryInterval") String retryIntervalString, @PluginConfiguration Configuration config, @PluginElement("Filters") Filter filter, @PluginAttribute("ignoreExceptions") String ignore)
  {
    if (name == null)
    {
      LOGGER.error("A name for the Appender must be specified");
      return null;
    }
    if (primary == null)
    {
      LOGGER.error("A primary Appender must be specified");
      return null;
    }
    if ((failovers == null) || (failovers.length == 0))
    {
      LOGGER.error("At least one failover Appender must be specified");
      return null;
    }
    int seconds = parseInt(retryIntervalString, 60);
    int retryIntervalMillis;
    int retryIntervalMillis;
    if (seconds >= 0)
    {
      retryIntervalMillis = seconds * 1000;
    }
    else
    {
      LOGGER.warn("Interval " + retryIntervalString + " is less than zero. Using default");
      retryIntervalMillis = 60000;
    }
    boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
    
    return new FailoverAppender(name, filter, primary, failovers, retryIntervalMillis, config, ignoreExceptions);
  }
}
