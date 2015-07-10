package com.avaje.ebeaninternal.api;

import java.util.logging.Level;
import java.util.logging.Logger;

class ClassLoadContext
{
  private static final Logger logger = Logger.getLogger(ClassLoadContext.class.getName());
  private final ClassLoader callerLoader;
  private final ClassLoader contextLoader;
  private final boolean preferContext;
  private boolean ambiguous;
  
  public static ClassLoadContext of(Class<?> caller, boolean preferContext)
  {
    return new ClassLoadContext(caller, preferContext);
  }
  
  ClassLoadContext(Class<?> caller, boolean preferContext)
  {
    if (caller == null) {
      throw new IllegalArgumentException("caller is null");
    }
    this.callerLoader = caller.getClassLoader();
    this.contextLoader = Thread.currentThread().getContextClassLoader();
    this.preferContext = preferContext;
  }
  
  public Class<?> forName(String name)
    throws ClassNotFoundException
  {
    ClassLoader defaultLoader = getDefault(this.preferContext);
    try
    {
      return Class.forName(name, true, defaultLoader);
    }
    catch (ClassNotFoundException e)
    {
      if (this.callerLoader == defaultLoader) {
        throw e;
      }
    }
    return Class.forName(name, true, this.callerLoader);
  }
  
  public ClassLoader getDefault(boolean preferContext)
  {
    if (this.contextLoader == null)
    {
      if (logger.isLoggable(Level.FINE)) {
        logger.fine("No Context ClassLoader, using " + this.callerLoader.getClass().getName());
      }
      return this.callerLoader;
    }
    if (this.contextLoader == this.callerLoader)
    {
      if (logger.isLoggable(Level.FINE)) {
        logger.fine("Context and Caller ClassLoader's same instance of " + this.contextLoader.getClass().getName());
      }
      return this.callerLoader;
    }
    if (isChild(this.contextLoader, this.callerLoader))
    {
      if (logger.isLoggable(Level.FINE)) {
        logger.info("Caller ClassLoader " + this.callerLoader.getClass().getName() + " child of ContextLoader " + this.contextLoader.getClass().getName());
      }
      return this.callerLoader;
    }
    if (isChild(this.callerLoader, this.contextLoader))
    {
      if (logger.isLoggable(Level.FINE)) {
        logger.info("Context ClassLoader " + this.contextLoader.getClass().getName() + " child of Caller ClassLoader " + this.callerLoader.getClass().getName());
      }
      return this.contextLoader;
    }
    logger.info("Ambiguous ClassLoader choice preferContext:" + preferContext + " Context:" + this.contextLoader.getClass().getName() + " Caller:" + this.callerLoader.getClass().getName());
    
    this.ambiguous = true;
    return preferContext ? this.contextLoader : this.callerLoader;
  }
  
  public boolean isAmbiguous()
  {
    return this.ambiguous;
  }
  
  public ClassLoader getCallerLoader()
  {
    return this.callerLoader;
  }
  
  public ClassLoader getContextLoader()
  {
    return this.contextLoader;
  }
  
  public ClassLoader getThisLoader()
  {
    return getClass().getClassLoader();
  }
  
  private boolean isChild(ClassLoader loader1, ClassLoader loader2)
  {
    for (; loader2 != null; loader2 = loader2.getParent()) {
      if (loader2 == loader1) {
        return true;
      }
    }
    return false;
  }
}
