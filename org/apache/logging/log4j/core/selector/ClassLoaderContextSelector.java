package org.apache.logging.log4j.core.selector;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.impl.ReflectiveCallerClassUtility;
import org.apache.logging.log4j.status.StatusLogger;

public class ClassLoaderContextSelector
  implements ContextSelector
{
  private static final AtomicReference<LoggerContext> CONTEXT = new AtomicReference();
  private static final PrivateSecurityManager SECURITY_MANAGER;
  private static final StatusLogger LOGGER = StatusLogger.getLogger();
  private static final ConcurrentMap<String, AtomicReference<WeakReference<LoggerContext>>> CONTEXT_MAP = new ConcurrentHashMap();
  
  static
  {
    if (ReflectiveCallerClassUtility.isSupported())
    {
      SECURITY_MANAGER = null;
    }
    else
    {
      PrivateSecurityManager securityManager;
      try
      {
        securityManager = new PrivateSecurityManager(null);
        if (securityManager.getCaller(ClassLoaderContextSelector.class.getName()) == null)
        {
          securityManager = null;
          LOGGER.error("Unable to obtain call stack from security manager.");
        }
      }
      catch (Exception e)
      {
        securityManager = null;
        LOGGER.debug("Unable to install security manager", e);
      }
      SECURITY_MANAGER = securityManager;
    }
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext)
  {
    return getContext(fqcn, loader, currentContext, null);
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation)
  {
    if (currentContext)
    {
      LoggerContext ctx = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
      if (ctx != null) {
        return ctx;
      }
      return getDefault();
    }
    if (loader != null) {
      return locateContext(loader, configLocation);
    }
    if (ReflectiveCallerClassUtility.isSupported()) {
      try
      {
        Class<?> clazz = Class.class;
        boolean next = false;
        for (int index = 2; clazz != null; index++)
        {
          clazz = ReflectiveCallerClassUtility.getCaller(index);
          if (clazz == null) {
            break;
          }
          if (clazz.getName().equals(fqcn)) {
            next = true;
          } else {
            if (next) {
              break;
            }
          }
        }
        if (clazz != null) {
          return locateContext(clazz.getClassLoader(), configLocation);
        }
      }
      catch (Exception ex) {}
    }
    if (SECURITY_MANAGER != null)
    {
      Class<?> clazz = SECURITY_MANAGER.getCaller(fqcn);
      if (clazz != null)
      {
        ClassLoader ldr = clazz.getClassLoader() != null ? clazz.getClassLoader() : ClassLoader.getSystemClassLoader();
        
        return locateContext(ldr, configLocation);
      }
    }
    Throwable t = new Throwable();
    boolean next = false;
    String name = null;
    for (StackTraceElement element : t.getStackTrace()) {
      if (element.getClassName().equals(fqcn))
      {
        next = true;
      }
      else if (next)
      {
        name = element.getClassName();
        break;
      }
    }
    if (name != null) {
      try
      {
        return locateContext(Loader.loadClass(name).getClassLoader(), configLocation);
      }
      catch (ClassNotFoundException ignore) {}
    }
    LoggerContext lc = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
    if (lc != null) {
      return lc;
    }
    return getDefault();
  }
  
  public void removeContext(LoggerContext context)
  {
    for (Map.Entry<String, AtomicReference<WeakReference<LoggerContext>>> entry : CONTEXT_MAP.entrySet())
    {
      LoggerContext ctx = (LoggerContext)((WeakReference)((AtomicReference)entry.getValue()).get()).get();
      if (ctx == context) {
        CONTEXT_MAP.remove(entry.getKey());
      }
    }
  }
  
  public List<LoggerContext> getLoggerContexts()
  {
    List<LoggerContext> list = new ArrayList();
    Collection<AtomicReference<WeakReference<LoggerContext>>> coll = CONTEXT_MAP.values();
    for (AtomicReference<WeakReference<LoggerContext>> ref : coll)
    {
      LoggerContext ctx = (LoggerContext)((WeakReference)ref.get()).get();
      if (ctx != null) {
        list.add(ctx);
      }
    }
    return Collections.unmodifiableList(list);
  }
  
  private LoggerContext locateContext(ClassLoader loader, URI configLocation)
  {
    String name = loader.toString();
    AtomicReference<WeakReference<LoggerContext>> ref = (AtomicReference)CONTEXT_MAP.get(name);
    if (ref == null)
    {
      if (configLocation == null)
      {
        ClassLoader parent = loader.getParent();
        while (parent != null)
        {
          ref = (AtomicReference)CONTEXT_MAP.get(parent.toString());
          if (ref != null)
          {
            WeakReference<LoggerContext> r = (WeakReference)ref.get();
            LoggerContext ctx = (LoggerContext)r.get();
            if (ctx != null) {
              return ctx;
            }
          }
          parent = parent.getParent();
        }
      }
      LoggerContext ctx = new LoggerContext(name, null, configLocation);
      AtomicReference<WeakReference<LoggerContext>> r = new AtomicReference();
      
      r.set(new WeakReference(ctx));
      CONTEXT_MAP.putIfAbsent(loader.toString(), r);
      ctx = (LoggerContext)((WeakReference)((AtomicReference)CONTEXT_MAP.get(name)).get()).get();
      return ctx;
    }
    WeakReference<LoggerContext> r = (WeakReference)ref.get();
    LoggerContext ctx = (LoggerContext)r.get();
    if (ctx != null)
    {
      if ((ctx.getConfigLocation() == null) && (configLocation != null))
      {
        LOGGER.debug("Setting configuration to {}", new Object[] { configLocation });
        ctx.setConfigLocation(configLocation);
      }
      else if ((ctx.getConfigLocation() != null) && (configLocation != null) && (!ctx.getConfigLocation().equals(configLocation)))
      {
        LOGGER.warn("locateContext called with URI {}. Existing LoggerContext has URI {}", new Object[] { configLocation, ctx.getConfigLocation() });
      }
      return ctx;
    }
    ctx = new LoggerContext(name, null, configLocation);
    ref.compareAndSet(r, new WeakReference(ctx));
    return ctx;
  }
  
  private LoggerContext getDefault()
  {
    LoggerContext ctx = (LoggerContext)CONTEXT.get();
    if (ctx != null) {
      return ctx;
    }
    CONTEXT.compareAndSet(null, new LoggerContext("Default"));
    return (LoggerContext)CONTEXT.get();
  }
  
  private static class PrivateSecurityManager
    extends SecurityManager
  {
    public Class<?> getCaller(String fqcn)
    {
      Class<?>[] classes = getClassContext();
      boolean next = false;
      for (Class<?> clazz : classes) {
        if (clazz.getName().equals(fqcn)) {
          next = true;
        } else if (next) {
          return clazz;
        }
      }
      return null;
    }
  }
}
