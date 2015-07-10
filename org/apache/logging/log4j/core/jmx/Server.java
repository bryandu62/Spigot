package org.apache.logging.log4j.core.jmx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.status.StatusLogger;

public final class Server
{
  private static final String PROPERTY_DISABLE_JMX = "log4j2.disable.jmx";
  
  public static String escape(String name)
  {
    StringBuilder sb = new StringBuilder(name.length() * 2);
    boolean needsQuotes = false;
    for (int i = 0; i < name.length(); i++)
    {
      char c = name.charAt(i);
      switch (c)
      {
      case '*': 
      case ',': 
      case ':': 
      case '=': 
      case '?': 
      case '\\': 
        sb.append('\\');
        needsQuotes = true;
      }
      sb.append(c);
    }
    if (needsQuotes)
    {
      sb.insert(0, '"');
      sb.append('"');
    }
    return sb.toString();
  }
  
  public static void registerMBeans(ContextSelector selector)
    throws JMException
  {
    if (Boolean.getBoolean("log4j2.disable.jmx"))
    {
      StatusLogger.getLogger().debug("JMX disabled for log4j2. Not registering MBeans.");
      
      return;
    }
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    registerMBeans(selector, mbs);
  }
  
  public static void registerMBeans(ContextSelector selector, final MBeanServer mbs)
    throws JMException
  {
    if (Boolean.getBoolean("log4j2.disable.jmx"))
    {
      StatusLogger.getLogger().debug("JMX disabled for log4j2. Not registering MBeans.");
      
      return;
    }
    final Executor executor = Executors.newFixedThreadPool(1);
    registerStatusLogger(mbs, executor);
    registerContextSelector(selector, mbs, executor);
    
    List<LoggerContext> contexts = selector.getLoggerContexts();
    registerContexts(contexts, mbs, executor);
    for (LoggerContext context : contexts) {
      context.addPropertyChangeListener(new PropertyChangeListener()
      {
        public void propertyChange(PropertyChangeEvent evt)
        {
          if (!"config".equals(evt.getPropertyName())) {
            return;
          }
          Server.unregisterLoggerConfigs(this.val$context, mbs);
          Server.unregisterAppenders(this.val$context, mbs);
          try
          {
            Server.registerLoggerConfigs(this.val$context, mbs, executor);
            Server.registerAppenders(this.val$context, mbs, executor);
          }
          catch (Exception ex)
          {
            StatusLogger.getLogger().error("Could not register mbeans", ex);
          }
        }
      });
    }
  }
  
  private static void registerStatusLogger(MBeanServer mbs, Executor executor)
    throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
  {
    StatusLoggerAdmin mbean = new StatusLoggerAdmin(executor);
    mbs.registerMBean(mbean, mbean.getObjectName());
  }
  
  private static void registerContextSelector(ContextSelector selector, MBeanServer mbs, Executor executor)
    throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
  {
    ContextSelectorAdmin mbean = new ContextSelectorAdmin(selector);
    mbs.registerMBean(mbean, mbean.getObjectName());
  }
  
  private static void registerContexts(List<LoggerContext> contexts, MBeanServer mbs, Executor executor)
    throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
  {
    for (LoggerContext ctx : contexts)
    {
      LoggerContextAdmin mbean = new LoggerContextAdmin(ctx, executor);
      mbs.registerMBean(mbean, mbean.getObjectName());
    }
  }
  
  private static void unregisterLoggerConfigs(LoggerContext context, MBeanServer mbs)
  {
    String pattern = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s";
    String search = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s", new Object[] { context.getName(), "*" });
    unregisterAllMatching(search, mbs);
  }
  
  private static void unregisterAppenders(LoggerContext context, MBeanServer mbs)
  {
    String pattern = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s";
    String search = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s", new Object[] { context.getName(), "*" });
    unregisterAllMatching(search, mbs);
  }
  
  private static void unregisterAllMatching(String search, MBeanServer mbs)
  {
    try
    {
      ObjectName pattern = new ObjectName(search);
      Set<ObjectName> found = mbs.queryNames(pattern, null);
      for (ObjectName objectName : found) {
        mbs.unregisterMBean(objectName);
      }
    }
    catch (Exception ex)
    {
      StatusLogger.getLogger().error("Could not unregister " + search, ex);
    }
  }
  
  private static void registerLoggerConfigs(LoggerContext ctx, MBeanServer mbs, Executor executor)
    throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
  {
    Map<String, LoggerConfig> map = ctx.getConfiguration().getLoggers();
    for (String name : map.keySet())
    {
      LoggerConfig cfg = (LoggerConfig)map.get(name);
      LoggerConfigAdmin mbean = new LoggerConfigAdmin(ctx.getName(), cfg);
      mbs.registerMBean(mbean, mbean.getObjectName());
    }
  }
  
  private static void registerAppenders(LoggerContext ctx, MBeanServer mbs, Executor executor)
    throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
  {
    Map<String, Appender> map = ctx.getConfiguration().getAppenders();
    for (String name : map.keySet())
    {
      Appender appender = (Appender)map.get(name);
      AppenderAdmin mbean = new AppenderAdmin(ctx.getName(), appender);
      mbs.registerMBean(mbean, mbean.getObjectName());
    }
  }
}
