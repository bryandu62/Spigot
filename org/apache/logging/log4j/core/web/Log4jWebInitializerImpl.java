package org.apache.logging.log4j.core.web;

import java.net.URI;
import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.LoggerContext.Status;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.core.selector.NamedContextSelector;
import org.apache.logging.log4j.spi.LoggerContextFactory;

final class Log4jWebInitializerImpl
  implements Log4jWebInitializer
{
  private static final Object MUTEX = new Object();
  
  static
  {
    try
    {
      Class.forName("org.apache.logging.log4j.core.web.JNDIContextFilter");
      throw new IllegalStateException("You are using Log4j 2 in a web application with the old, extinct log4j-web artifact. This is not supported and could cause serious runtime problems. Pleaseremove the log4j-web JAR file from your application.");
    }
    catch (ClassNotFoundException ignore) {}
  }
  
  private final StrSubstitutor substitutor = new StrSubstitutor(new Interpolator());
  private final ServletContext servletContext;
  private String name;
  private NamedContextSelector selector;
  private LoggerContext loggerContext;
  private boolean initialized = false;
  private boolean deinitialized = false;
  
  private Log4jWebInitializerImpl(ServletContext servletContext)
  {
    this.servletContext = servletContext;
  }
  
  public synchronized void initialize()
    throws UnavailableException
  {
    if (this.deinitialized) {
      throw new IllegalStateException("Cannot initialize Log4jWebInitializer after it was destroyed.");
    }
    if (!this.initialized)
    {
      this.initialized = true;
      
      this.name = this.substitutor.replace(this.servletContext.getInitParameter("log4jContextName"));
      String location = this.substitutor.replace(this.servletContext.getInitParameter("log4jConfiguration"));
      boolean isJndi = "true".equals(this.servletContext.getInitParameter("isLog4jContextSelectorNamed"));
      if (isJndi) {
        initializeJndi(location);
      } else {
        initializeNonJndi(location);
      }
    }
  }
  
  private void initializeJndi(String location)
    throws UnavailableException
  {
    URI configLocation = null;
    if (location != null) {
      try
      {
        configLocation = new URI(location);
      }
      catch (Exception e)
      {
        this.servletContext.log("Unable to convert configuration location [" + location + "] to a URI!", e);
      }
    }
    if (this.name == null) {
      throw new UnavailableException("A log4jContextName context parameter is required");
    }
    LoggerContextFactory factory = LogManager.getFactory();
    LoggerContext loggerContext;
    if ((factory instanceof Log4jContextFactory))
    {
      ContextSelector selector = ((Log4jContextFactory)factory).getSelector();
      if ((selector instanceof NamedContextSelector))
      {
        this.selector = ((NamedContextSelector)selector);
        LoggerContext loggerContext = this.selector.locateContext(this.name, this.servletContext, configLocation);
        ContextAnchor.THREAD_CONTEXT.set(loggerContext);
        if (loggerContext.getStatus() == LoggerContext.Status.INITIALIZED) {
          loggerContext.start();
        }
        ContextAnchor.THREAD_CONTEXT.remove();
      }
      else
      {
        this.servletContext.log("Potential problem: Selector is not an instance of NamedContextSelector."); return;
      }
    }
    else
    {
      this.servletContext.log("Potential problem: Factory is not an instance of Log4jContextFactory."); return;
    }
    LoggerContext loggerContext;
    this.loggerContext = loggerContext;
    this.servletContext.log("Created logger context for [" + this.name + "] using [" + loggerContext.getClass().getClassLoader() + "].");
  }
  
  private void initializeNonJndi(String location)
  {
    if (this.name == null) {
      this.name = this.servletContext.getServletContextName();
    }
    if ((this.name == null) && (location == null))
    {
      this.servletContext.log("No Log4j context configuration provided. This is very unusual.");
      return;
    }
    this.loggerContext = Configurator.initialize(this.name, getClassLoader(), location, this.servletContext);
  }
  
  public synchronized void deinitialize()
  {
    if (!this.initialized) {
      throw new IllegalStateException("Cannot deinitialize Log4jWebInitializer because it has not initialized.");
    }
    if (!this.deinitialized)
    {
      this.deinitialized = true;
      if (this.loggerContext != null)
      {
        this.servletContext.log("Removing LoggerContext for [" + this.name + "].");
        if (this.selector != null) {
          this.selector.removeContext(this.name);
        }
        this.loggerContext.stop();
        this.loggerContext.setExternalContext(null);
        this.loggerContext = null;
      }
    }
  }
  
  public void setLoggerContext()
  {
    if (this.loggerContext != null) {
      ContextAnchor.THREAD_CONTEXT.set(this.loggerContext);
    }
  }
  
  public void clearLoggerContext()
  {
    ContextAnchor.THREAD_CONTEXT.remove();
  }
  
  private ClassLoader getClassLoader()
  {
    try
    {
      return this.servletContext.getClassLoader();
    }
    catch (Throwable ignore) {}
    return Log4jWebInitializerImpl.class.getClassLoader();
  }
  
  static Log4jWebInitializer getLog4jWebInitializer(ServletContext servletContext)
  {
    synchronized (MUTEX)
    {
      Log4jWebInitializer initializer = (Log4jWebInitializer)servletContext.getAttribute(INITIALIZER_ATTRIBUTE);
      if (initializer == null)
      {
        initializer = new Log4jWebInitializerImpl(servletContext);
        servletContext.setAttribute(INITIALIZER_ATTRIBUTE, initializer);
      }
      return initializer;
    }
  }
}
