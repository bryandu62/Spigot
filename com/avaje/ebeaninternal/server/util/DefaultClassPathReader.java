package com.avaje.ebeaninternal.server.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.logging.Logger;

public class DefaultClassPathReader
  implements ClassPathReader
{
  private static final Logger logger = Logger.getLogger(DefaultClassPathReader.class.getName());
  
  public Object[] readPath(ClassLoader classLoader)
  {
    if ((classLoader instanceof URLClassLoader))
    {
      URLClassLoader ucl = (URLClassLoader)classLoader;
      return ucl.getURLs();
    }
    try
    {
      Method method = classLoader.getClass().getMethod("getClassPath", new Class[0]);
      if (method != null)
      {
        logger.info("Using getClassPath() method on classLoader[" + classLoader.getClass() + "]");
        String s = method.invoke(classLoader, new Object[0]).toString();
        return s.split(File.pathSeparator);
      }
    }
    catch (NoSuchMethodException e) {}catch (Exception e)
    {
      throw new RuntimeException("Unexpected Error trying to read classpath from classloader", e);
    }
    try
    {
      Method method = classLoader.getClass().getMethod("getClasspath", new Class[0]);
      if (method != null)
      {
        logger.info("Using getClasspath() method on classLoader[" + classLoader.getClass() + "]");
        String s = method.invoke(classLoader, new Object[0]).toString();
        
        return s.split(File.pathSeparator);
      }
    }
    catch (NoSuchMethodException e) {}catch (Exception e)
    {
      throw new RuntimeException("Unexpected Error trying to read classpath from classloader", e);
    }
    String imsg = "Unsure how to read classpath from classLoader [" + classLoader.getClass() + "]";
    logger.info(imsg);
    
    String msg = "Using java.class.path system property to search for entity beans";
    logger.warning(msg);
    
    return System.getProperty("java.class.path", "").split(File.pathSeparator);
  }
}
