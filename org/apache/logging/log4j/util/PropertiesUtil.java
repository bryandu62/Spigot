package org.apache.logging.log4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class PropertiesUtil
{
  private static final PropertiesUtil LOG4J_PROPERTIES = new PropertiesUtil("log4j2.component.properties");
  private static final Logger LOGGER = StatusLogger.getLogger();
  private final Properties props;
  
  public PropertiesUtil(Properties props)
  {
    this.props = props;
  }
  
  static Properties loadClose(InputStream in, Object source)
  {
    props = new Properties();
    if (null != in) {
      try
      {
        props.load(in);
        
        return props;
      }
      catch (IOException e)
      {
        LOGGER.error("Unable to read " + source, e);
      }
      finally
      {
        try
        {
          in.close();
        }
        catch (IOException e)
        {
          LOGGER.error("Unable to close " + source, e);
        }
      }
    }
  }
  
  public PropertiesUtil(String propsLocn)
  {
    ClassLoader loader = ProviderUtil.findClassLoader();
    InputStream in = loader.getResourceAsStream(propsLocn);
    this.props = loadClose(in, propsLocn);
  }
  
  public static PropertiesUtil getProperties()
  {
    return LOG4J_PROPERTIES;
  }
  
  public String getStringProperty(String name)
  {
    String prop = null;
    try
    {
      prop = System.getProperty(name);
    }
    catch (SecurityException e) {}
    return prop == null ? this.props.getProperty(name) : prop;
  }
  
  public int getIntegerProperty(String name, int defaultValue)
  {
    String prop = null;
    try
    {
      prop = System.getProperty(name);
    }
    catch (SecurityException e) {}
    if (prop == null) {
      prop = this.props.getProperty(name);
    }
    if (prop != null) {
      try
      {
        return Integer.parseInt(prop);
      }
      catch (Exception ex)
      {
        return defaultValue;
      }
    }
    return defaultValue;
  }
  
  public long getLongProperty(String name, long defaultValue)
  {
    String prop = null;
    try
    {
      prop = System.getProperty(name);
    }
    catch (SecurityException e) {}
    if (prop == null) {
      prop = this.props.getProperty(name);
    }
    if (prop != null) {
      try
      {
        return Long.parseLong(prop);
      }
      catch (Exception ex)
      {
        return defaultValue;
      }
    }
    return defaultValue;
  }
  
  public String getStringProperty(String name, String defaultValue)
  {
    String prop = getStringProperty(name);
    return prop == null ? defaultValue : prop;
  }
  
  public boolean getBooleanProperty(String name)
  {
    return getBooleanProperty(name, false);
  }
  
  public boolean getBooleanProperty(String name, boolean defaultValue)
  {
    String prop = getStringProperty(name);
    return prop == null ? defaultValue : "true".equalsIgnoreCase(prop);
  }
  
  public static Properties getSystemProperties()
  {
    try
    {
      return new Properties(System.getProperties());
    }
    catch (SecurityException ex)
    {
      StatusLogger.getLogger().error("Unable to access system properties.");
    }
    return new Properties();
  }
}
