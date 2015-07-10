package com.avaje.ebean.config;

import com.avaje.ebeaninternal.api.ClassUtil;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletContext;

public final class GlobalProperties
{
  private static volatile PropertyMap globalMap;
  private static boolean skipPrimaryServer;
  
  public static synchronized void setSkipPrimaryServer(boolean skip)
  {
    skipPrimaryServer = skip;
  }
  
  public static synchronized boolean isSkipPrimaryServer()
  {
    return skipPrimaryServer;
  }
  
  public static String evaluateExpressions(String val)
  {
    return getPropertyMap().eval(val);
  }
  
  public static synchronized void evaluateExpressions()
  {
    getPropertyMap().evaluateProperties();
  }
  
  public static synchronized void setServletContext(ServletContext servletContext)
  {
    PropertyMapLoader.setServletContext(servletContext);
  }
  
  public static synchronized ServletContext getServletContext()
  {
    return PropertyMapLoader.getServletContext();
  }
  
  private static void initPropertyMap()
  {
    String fileName = System.getenv("EBEAN_PROPS_FILE");
    if (fileName == null)
    {
      fileName = System.getProperty("ebean.props.file");
      if (fileName == null) {
        fileName = "ebean.properties";
      }
    }
    globalMap = PropertyMapLoader.load(null, fileName);
    if (globalMap == null) {
      globalMap = new PropertyMap();
    }
    String loaderCn = globalMap.get("ebean.properties.loader");
    if (loaderCn != null) {
      try
      {
        Runnable r = (Runnable)ClassUtil.newInstance(loaderCn);
        r.run();
      }
      catch (Exception e)
      {
        String m = "Error creating or running properties loader " + loaderCn;
        throw new RuntimeException(m, e);
      }
    }
  }
  
  private static synchronized PropertyMap getPropertyMap()
  {
    if (globalMap == null) {
      initPropertyMap();
    }
    return globalMap;
  }
  
  public static synchronized String get(String key, String defaultValue)
  {
    return getPropertyMap().get(key, defaultValue);
  }
  
  public static synchronized int getInt(String key, int defaultValue)
  {
    return getPropertyMap().getInt(key, defaultValue);
  }
  
  public static synchronized boolean getBoolean(String key, boolean defaultValue)
  {
    return getPropertyMap().getBoolean(key, defaultValue);
  }
  
  public static synchronized String put(String key, String value)
  {
    return getPropertyMap().putEval(key, value);
  }
  
  public static synchronized void putAll(Map<String, String> keyValueMap)
  {
    for (Map.Entry<String, String> e : keyValueMap.entrySet()) {
      getPropertyMap().putEval((String)e.getKey(), (String)e.getValue());
    }
  }
  
  public static PropertySource getPropertySource(String name)
  {
    return new ConfigPropertyMap(name);
  }
  
  public static abstract interface PropertySource
  {
    public abstract String getServerName();
    
    public abstract String get(String paramString1, String paramString2);
    
    public abstract int getInt(String paramString, int paramInt);
    
    public abstract boolean getBoolean(String paramString, boolean paramBoolean);
    
    public abstract <T extends Enum<T>> T getEnum(Class<T> paramClass, String paramString, T paramT);
  }
}
