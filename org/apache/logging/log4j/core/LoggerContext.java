package org.apache.logging.log4j.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.NullConfiguration;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.helpers.Assert;
import org.apache.logging.log4j.core.helpers.NetUtils;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.status.StatusLogger;

public class LoggerContext
  implements org.apache.logging.log4j.spi.LoggerContext, ConfigurationListener, LifeCycle
{
  public static final String PROPERTY_CONFIG = "config";
  private static final StatusLogger LOGGER = ;
  private final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap();
  private final CopyOnWriteArrayList<PropertyChangeListener> propertyChangeListeners = new CopyOnWriteArrayList();
  private volatile Configuration config = new DefaultConfiguration();
  private Object externalContext;
  private final String name;
  private URI configLocation;
  private ShutdownThread shutdownThread = null;
  
  public static enum Status
  {
    INITIALIZED,  STARTING,  STARTED,  STOPPING,  STOPPED;
    
    private Status() {}
  }
  
  private volatile Status status = Status.INITIALIZED;
  private final Lock configLock = new ReentrantLock();
  
  public LoggerContext(String name)
  {
    this(name, null, (URI)null);
  }
  
  public LoggerContext(String name, Object externalContext)
  {
    this(name, externalContext, (URI)null);
  }
  
  public LoggerContext(String name, Object externalContext, URI configLocn)
  {
    this.name = name;
    this.externalContext = externalContext;
    this.configLocation = configLocn;
  }
  
  public LoggerContext(String name, Object externalContext, String configLocn)
  {
    this.name = name;
    this.externalContext = externalContext;
    if (configLocn != null)
    {
      URI uri;
      try
      {
        uri = new File(configLocn).toURI();
      }
      catch (Exception ex)
      {
        uri = null;
      }
      this.configLocation = uri;
    }
    else
    {
      this.configLocation = null;
    }
  }
  
  public void start()
  {
    if (this.configLock.tryLock()) {
      try
      {
        if ((this.status == Status.INITIALIZED) || (this.status == Status.STOPPED))
        {
          this.status = Status.STARTING;
          reconfigure();
          if (this.config.isShutdownHookEnabled())
          {
            this.shutdownThread = new ShutdownThread(this);
            try
            {
              Runtime.getRuntime().addShutdownHook(this.shutdownThread);
            }
            catch (IllegalStateException ise)
            {
              LOGGER.warn("Unable to register shutdown hook due to JVM state");
              this.shutdownThread = null;
            }
            catch (SecurityException se)
            {
              LOGGER.warn("Unable to register shutdown hook due to security restrictions");
              this.shutdownThread = null;
            }
          }
          this.status = Status.STARTED;
        }
      }
      finally
      {
        this.configLock.unlock();
      }
    }
  }
  
  public void start(Configuration config)
  {
    if (this.configLock.tryLock()) {
      try
      {
        if (((this.status == Status.INITIALIZED) || (this.status == Status.STOPPED)) && (config.isShutdownHookEnabled()))
        {
          this.shutdownThread = new ShutdownThread(this);
          try
          {
            Runtime.getRuntime().addShutdownHook(this.shutdownThread);
          }
          catch (IllegalStateException ise)
          {
            LOGGER.warn("Unable to register shutdown hook due to JVM state");
            this.shutdownThread = null;
          }
          catch (SecurityException se)
          {
            LOGGER.warn("Unable to register shutdown hook due to security restrictions");
            this.shutdownThread = null;
          }
          this.status = Status.STARTED;
        }
      }
      finally
      {
        this.configLock.unlock();
      }
    }
    setConfiguration(config);
  }
  
  public void stop()
  {
    this.configLock.lock();
    try
    {
      if (this.status == Status.STOPPED) {
        return;
      }
      this.status = Status.STOPPING;
      if (this.shutdownThread != null)
      {
        Runtime.getRuntime().removeShutdownHook(this.shutdownThread);
        this.shutdownThread = null;
      }
      Configuration prev = this.config;
      this.config = new NullConfiguration();
      updateLoggers();
      prev.stop();
      this.externalContext = null;
      LogManager.getFactory().removeContext(this);
      this.status = Status.STOPPED;
    }
    finally
    {
      this.configLock.unlock();
    }
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public Status getStatus()
  {
    return this.status;
  }
  
  public boolean isStarted()
  {
    return this.status == Status.STARTED;
  }
  
  public void setExternalContext(Object context)
  {
    this.externalContext = context;
  }
  
  public Object getExternalContext()
  {
    return this.externalContext;
  }
  
  public Logger getLogger(String name)
  {
    return getLogger(name, null);
  }
  
  public Logger getLogger(String name, MessageFactory messageFactory)
  {
    Logger logger = (Logger)this.loggers.get(name);
    if (logger != null)
    {
      AbstractLogger.checkMessageFactory(logger, messageFactory);
      return logger;
    }
    logger = newInstance(this, name, messageFactory);
    Logger prev = (Logger)this.loggers.putIfAbsent(name, logger);
    return prev == null ? logger : prev;
  }
  
  public boolean hasLogger(String name)
  {
    return this.loggers.containsKey(name);
  }
  
  public Configuration getConfiguration()
  {
    return this.config;
  }
  
  public void addFilter(Filter filter)
  {
    this.config.addFilter(filter);
  }
  
  public void removeFilter(Filter filter)
  {
    this.config.removeFilter(filter);
  }
  
  private synchronized Configuration setConfiguration(Configuration config)
  {
    if (config == null) {
      throw new NullPointerException("No Configuration was provided");
    }
    Configuration prev = this.config;
    config.addListener(this);
    Map<String, String> map = new HashMap();
    map.put("hostName", NetUtils.getLocalHostname());
    map.put("contextName", this.name);
    config.addComponent("ContextProperties", map);
    config.start();
    this.config = config;
    updateLoggers();
    if (prev != null)
    {
      prev.removeListener(this);
      prev.stop();
    }
    PropertyChangeEvent evt = new PropertyChangeEvent(this, "config", prev, config);
    for (PropertyChangeListener listener : this.propertyChangeListeners) {
      listener.propertyChange(evt);
    }
    return prev;
  }
  
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    this.propertyChangeListeners.add(Assert.isNotNull(listener, "listener"));
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    this.propertyChangeListeners.remove(listener);
  }
  
  public synchronized URI getConfigLocation()
  {
    return this.configLocation;
  }
  
  public synchronized void setConfigLocation(URI configLocation)
  {
    this.configLocation = configLocation;
    reconfigure();
  }
  
  public synchronized void reconfigure()
  {
    LOGGER.debug("Reconfiguration started for context " + this.name);
    Configuration instance = ConfigurationFactory.getInstance().getConfiguration(this.name, this.configLocation);
    setConfiguration(instance);
    
    LOGGER.debug("Reconfiguration completed");
  }
  
  public void updateLoggers()
  {
    updateLoggers(this.config);
  }
  
  public void updateLoggers(Configuration config)
  {
    for (Logger logger : this.loggers.values()) {
      logger.updateConfiguration(config);
    }
  }
  
  public synchronized void onChange(Reconfigurable reconfigurable)
  {
    LOGGER.debug("Reconfiguration started for context " + this.name);
    Configuration config = reconfigurable.reconfigure();
    if (config != null)
    {
      setConfiguration(config);
      LOGGER.debug("Reconfiguration completed");
    }
    else
    {
      LOGGER.debug("Reconfiguration failed");
    }
  }
  
  protected Logger newInstance(LoggerContext ctx, String name, MessageFactory messageFactory)
  {
    return new Logger(ctx, name, messageFactory);
  }
  
  private class ShutdownThread
    extends Thread
  {
    private final LoggerContext context;
    
    public ShutdownThread(LoggerContext context)
    {
      this.context = context;
    }
    
    public void run()
    {
      this.context.shutdownThread = null;
      this.context.stop();
    }
  }
}
