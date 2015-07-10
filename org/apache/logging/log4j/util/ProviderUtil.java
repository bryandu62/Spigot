package org.apache.logging.log4j.util;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.status.StatusLogger;

public final class ProviderUtil
{
  private static final String PROVIDER_RESOURCE = "META-INF/log4j-provider.properties";
  private static final String API_VERSION = "Log4jAPIVersion";
  private static final String[] COMPATIBLE_API_VERSIONS = { "2.0.0" };
  private static final Logger LOGGER = StatusLogger.getLogger();
  private static final List<Provider> PROVIDERS = new ArrayList();
  
  static
  {
    ClassLoader cl = findClassLoader();
    Enumeration<URL> enumResources = null;
    try
    {
      enumResources = cl.getResources("META-INF/log4j-provider.properties");
    }
    catch (IOException e)
    {
      LOGGER.fatal("Unable to locate META-INF/log4j-provider.properties", e);
    }
    if (enumResources != null) {
      while (enumResources.hasMoreElements())
      {
        URL url = (URL)enumResources.nextElement();
        try
        {
          Properties props = PropertiesUtil.loadClose(url.openStream(), url);
          if (validVersion(props.getProperty("Log4jAPIVersion"))) {
            PROVIDERS.add(new Provider(props, url));
          }
        }
        catch (IOException ioe)
        {
          LOGGER.error("Unable to open " + url.toString(), ioe);
        }
      }
    }
  }
  
  public static Iterator<Provider> getProviders()
  {
    return PROVIDERS.iterator();
  }
  
  public static boolean hasProviders()
  {
    return PROVIDERS.size() > 0;
  }
  
  public static ClassLoader findClassLoader()
  {
    ClassLoader cl;
    ClassLoader cl;
    if (System.getSecurityManager() == null) {
      cl = Thread.currentThread().getContextClassLoader();
    } else {
      cl = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
      {
        public ClassLoader run()
        {
          return Thread.currentThread().getContextClassLoader();
        }
      });
    }
    if (cl == null) {
      cl = ProviderUtil.class.getClassLoader();
    }
    return cl;
  }
  
  private static boolean validVersion(String version)
  {
    for (String v : COMPATIBLE_API_VERSIONS) {
      if (version.startsWith(v)) {
        return true;
      }
    }
    return false;
  }
}
