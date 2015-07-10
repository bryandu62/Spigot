package org.apache.logging.log4j.core.net;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.helpers.Integers;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="multicastdns", category="Core", elementType="advertiser", printObject=false)
public class MulticastDNSAdvertiser
  implements Advertiser
{
  protected static final Logger LOGGER = ;
  private static Object jmDNS = initializeJMDNS();
  private static Class<?> jmDNSClass;
  private static Class<?> serviceInfoClass;
  
  public Object advertise(Map<String, String> properties)
  {
    Map<String, String> truncatedProperties = new HashMap();
    for (Map.Entry<String, String> entry : properties.entrySet()) {
      if ((((String)entry.getKey()).length() <= 255) && (((String)entry.getValue()).length() <= 255)) {
        truncatedProperties.put(entry.getKey(), entry.getValue());
      }
    }
    String protocol = (String)truncatedProperties.get("protocol");
    String zone = "._log4j._" + (protocol != null ? protocol : "tcp") + ".local.";
    
    String portString = (String)truncatedProperties.get("port");
    int port = Integers.parseInt(portString, 4555);
    
    String name = (String)truncatedProperties.get("name");
    if (jmDNS != null)
    {
      boolean isVersion3 = false;
      try
      {
        jmDNSClass.getMethod("create", (Class[])null);
        isVersion3 = true;
      }
      catch (NoSuchMethodException e) {}
      Object serviceInfo;
      Object serviceInfo;
      if (isVersion3) {
        serviceInfo = buildServiceInfoVersion3(zone, port, name, truncatedProperties);
      } else {
        serviceInfo = buildServiceInfoVersion1(zone, port, name, truncatedProperties);
      }
      try
      {
        Method method = jmDNSClass.getMethod("registerService", new Class[] { serviceInfoClass });
        method.invoke(jmDNS, new Object[] { serviceInfo });
      }
      catch (IllegalAccessException e)
      {
        LOGGER.warn("Unable to invoke registerService method", e);
      }
      catch (NoSuchMethodException e)
      {
        LOGGER.warn("No registerService method", e);
      }
      catch (InvocationTargetException e)
      {
        LOGGER.warn("Unable to invoke registerService method", e);
      }
      return serviceInfo;
    }
    LOGGER.warn("JMDNS not available - will not advertise ZeroConf support");
    return null;
  }
  
  public void unadvertise(Object serviceInfo)
  {
    if (jmDNS != null) {
      try
      {
        Method method = jmDNSClass.getMethod("unregisterService", new Class[] { serviceInfoClass });
        method.invoke(jmDNS, new Object[] { serviceInfo });
      }
      catch (IllegalAccessException e)
      {
        LOGGER.warn("Unable to invoke unregisterService method", e);
      }
      catch (NoSuchMethodException e)
      {
        LOGGER.warn("No unregisterService method", e);
      }
      catch (InvocationTargetException e)
      {
        LOGGER.warn("Unable to invoke unregisterService method", e);
      }
    }
  }
  
  private static Object createJmDNSVersion1()
  {
    try
    {
      return jmDNSClass.newInstance();
    }
    catch (InstantiationException e)
    {
      LOGGER.warn("Unable to instantiate JMDNS", e);
    }
    catch (IllegalAccessException e)
    {
      LOGGER.warn("Unable to instantiate JMDNS", e);
    }
    return null;
  }
  
  private static Object createJmDNSVersion3()
  {
    try
    {
      Method jmDNSCreateMethod = jmDNSClass.getMethod("create", (Class[])null);
      return jmDNSCreateMethod.invoke(null, (Object[])null);
    }
    catch (IllegalAccessException e)
    {
      LOGGER.warn("Unable to instantiate jmdns class", e);
    }
    catch (NoSuchMethodException e)
    {
      LOGGER.warn("Unable to access constructor", e);
    }
    catch (InvocationTargetException e)
    {
      LOGGER.warn("Unable to call constructor", e);
    }
    return null;
  }
  
  private Object buildServiceInfoVersion1(String zone, int port, String name, Map<String, String> properties)
  {
    Hashtable<String, String> hashtableProperties = new Hashtable(properties);
    try
    {
      Class<?>[] args = new Class[6];
      args[0] = String.class;
      args[1] = String.class;
      args[2] = Integer.TYPE;
      args[3] = Integer.TYPE;
      args[4] = Integer.TYPE;
      args[5] = Hashtable.class;
      Constructor<?> constructor = serviceInfoClass.getConstructor(args);
      Object[] values = new Object[6];
      values[0] = zone;
      values[1] = name;
      values[2] = Integer.valueOf(port);
      values[3] = Integer.valueOf(0);
      values[4] = Integer.valueOf(0);
      values[5] = hashtableProperties;
      return constructor.newInstance(values);
    }
    catch (IllegalAccessException e)
    {
      LOGGER.warn("Unable to construct ServiceInfo instance", e);
    }
    catch (NoSuchMethodException e)
    {
      LOGGER.warn("Unable to get ServiceInfo constructor", e);
    }
    catch (InstantiationException e)
    {
      LOGGER.warn("Unable to construct ServiceInfo instance", e);
    }
    catch (InvocationTargetException e)
    {
      LOGGER.warn("Unable to construct ServiceInfo instance", e);
    }
    return null;
  }
  
  private Object buildServiceInfoVersion3(String zone, int port, String name, Map<String, String> properties)
  {
    try
    {
      Class<?>[] args = new Class[6];
      args[0] = String.class;
      args[1] = String.class;
      args[2] = Integer.TYPE;
      args[3] = Integer.TYPE;
      args[4] = Integer.TYPE;
      args[5] = Map.class;
      Method serviceInfoCreateMethod = serviceInfoClass.getMethod("create", args);
      Object[] values = new Object[6];
      values[0] = zone;
      values[1] = name;
      values[2] = Integer.valueOf(port);
      values[3] = Integer.valueOf(0);
      values[4] = Integer.valueOf(0);
      values[5] = properties;
      return serviceInfoCreateMethod.invoke(null, values);
    }
    catch (IllegalAccessException e)
    {
      LOGGER.warn("Unable to invoke create method", e);
    }
    catch (NoSuchMethodException e)
    {
      LOGGER.warn("Unable to find create method", e);
    }
    catch (InvocationTargetException e)
    {
      LOGGER.warn("Unable to invoke create method", e);
    }
    return null;
  }
  
  private static Object initializeJMDNS()
  {
    try
    {
      jmDNSClass = Class.forName("javax.jmdns.JmDNS");
      serviceInfoClass = Class.forName("javax.jmdns.ServiceInfo");
      
      boolean isVersion3 = false;
      try
      {
        jmDNSClass.getMethod("create", (Class[])null);
        isVersion3 = true;
      }
      catch (NoSuchMethodException e) {}
      if (isVersion3) {
        return createJmDNSVersion3();
      }
      return createJmDNSVersion1();
    }
    catch (ClassNotFoundException e)
    {
      LOGGER.warn("JmDNS or serviceInfo class not found", e);
    }
    catch (ExceptionInInitializerError e2)
    {
      LOGGER.warn("JmDNS or serviceInfo class not found", e2);
    }
    return null;
  }
}
