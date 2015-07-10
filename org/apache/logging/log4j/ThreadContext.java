package org.apache.logging.log4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.spi.DefaultThreadContextStack;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.spi.MutableThreadContextStack;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextStack;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ProviderUtil;

public final class ThreadContext
{
  public static final Map<String, String> EMPTY_MAP = ;
  public static final ThreadContextStack EMPTY_STACK = new MutableThreadContextStack(new ArrayList());
  private static final String DISABLE_MAP = "disableThreadContextMap";
  private static final String DISABLE_STACK = "disableThreadContextStack";
  private static final String DISABLE_ALL = "disableThreadContext";
  private static final String THREAD_CONTEXT_KEY = "log4j2.threadContextMap";
  private static boolean all;
  private static boolean useMap;
  private static boolean useStack;
  private static ThreadContextMap contextMap;
  private static ThreadContextStack contextStack;
  private static final Logger LOGGER = StatusLogger.getLogger();
  
  static
  {
    PropertiesUtil managerProps = PropertiesUtil.getProperties();
    all = managerProps.getBooleanProperty("disableThreadContext");
    useStack = (!managerProps.getBooleanProperty("disableThreadContextStack")) && (!all);
    contextStack = new DefaultThreadContextStack(useStack);
    
    useMap = (!managerProps.getBooleanProperty("disableThreadContextMap")) && (!all);
    String threadContextMapName = managerProps.getStringProperty("log4j2.threadContextMap");
    ClassLoader cl = ProviderUtil.findClassLoader();
    if (threadContextMapName != null) {
      try
      {
        Class<?> clazz = cl.loadClass(threadContextMapName);
        if (ThreadContextMap.class.isAssignableFrom(clazz)) {
          contextMap = (ThreadContextMap)clazz.newInstance();
        }
      }
      catch (ClassNotFoundException cnfe)
      {
        LOGGER.error("Unable to locate configured LoggerContextFactory {}", new Object[] { threadContextMapName });
      }
      catch (Exception ex)
      {
        LOGGER.error("Unable to create configured LoggerContextFactory {}", new Object[] { threadContextMapName, ex });
      }
    }
    if ((contextMap == null) && (ProviderUtil.hasProviders()))
    {
      LoggerContextFactory factory = LogManager.getFactory();
      Iterator<Provider> providers = ProviderUtil.getProviders();
      while (providers.hasNext())
      {
        Provider provider = (Provider)providers.next();
        threadContextMapName = provider.getThreadContextMap();
        String factoryClassName = provider.getClassName();
        if ((threadContextMapName != null) && (factory.getClass().getName().equals(factoryClassName))) {
          try
          {
            Class<?> clazz = cl.loadClass(threadContextMapName);
            if (ThreadContextMap.class.isAssignableFrom(clazz))
            {
              contextMap = (ThreadContextMap)clazz.newInstance();
              break;
            }
          }
          catch (ClassNotFoundException cnfe)
          {
            LOGGER.error("Unable to locate configured LoggerContextFactory {}", new Object[] { threadContextMapName });
            contextMap = new DefaultThreadContextMap(useMap);
          }
          catch (Exception ex)
          {
            LOGGER.error("Unable to create configured LoggerContextFactory {}", new Object[] { threadContextMapName, ex });
            contextMap = new DefaultThreadContextMap(useMap);
          }
        }
      }
    }
    if (contextMap == null) {
      contextMap = new DefaultThreadContextMap(useMap);
    }
  }
  
  public static void put(String key, String value)
  {
    contextMap.put(key, value);
  }
  
  public static String get(String key)
  {
    return contextMap.get(key);
  }
  
  public static void remove(String key)
  {
    contextMap.remove(key);
  }
  
  public static void clear()
  {
    contextMap.clear();
  }
  
  public static boolean containsKey(String key)
  {
    return contextMap.containsKey(key);
  }
  
  public static Map<String, String> getContext()
  {
    return contextMap.getCopy();
  }
  
  public static Map<String, String> getImmutableContext()
  {
    Map<String, String> map = contextMap.getImmutableMapOrNull();
    return map == null ? EMPTY_MAP : map;
  }
  
  public static boolean isEmpty()
  {
    return contextMap.isEmpty();
  }
  
  public static void clearStack()
  {
    contextStack.clear();
  }
  
  public static ContextStack cloneStack()
  {
    return contextStack.copy();
  }
  
  public static ContextStack getImmutableStack()
  {
    return contextStack;
  }
  
  public static void setStack(Collection<String> stack)
  {
    if ((stack.size() == 0) || (!useStack)) {
      return;
    }
    contextStack.clear();
    contextStack.addAll(stack);
  }
  
  public static int getDepth()
  {
    return contextStack.getDepth();
  }
  
  public static String pop()
  {
    return contextStack.pop();
  }
  
  public static String peek()
  {
    return contextStack.peek();
  }
  
  public static void push(String message)
  {
    contextStack.push(message);
  }
  
  public static void push(String message, Object... args)
  {
    contextStack.push(ParameterizedMessage.format(message, args));
  }
  
  public static void removeStack()
  {
    contextStack.clear();
  }
  
  public static void trim(int depth)
  {
    contextStack.trim(depth);
  }
  
  public static abstract interface ContextStack
    extends Serializable
  {
    public abstract void clear();
    
    public abstract String pop();
    
    public abstract String peek();
    
    public abstract void push(String paramString);
    
    public abstract int getDepth();
    
    public abstract List<String> asList();
    
    public abstract void trim(int paramInt);
    
    public abstract ContextStack copy();
  }
}
