package com.avaje.ebean.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

final class PropertyMapLoader
{
  private static Logger logger = Logger.getLogger(PropertyMapLoader.class.getName());
  private static ServletContext servletContext;
  
  public static ServletContext getServletContext()
  {
    return servletContext;
  }
  
  public static void setServletContext(ServletContext servletContext)
  {
    servletContext = servletContext;
  }
  
  public static PropertyMap load(PropertyMap p, String fileName)
  {
    InputStream is = findInputStream(fileName);
    if (is == null)
    {
      logger.severe(fileName + " not found");
      return p;
    }
    return load(p, is);
  }
  
  private static PropertyMap load(PropertyMap p, InputStream in)
  {
    Properties props = new Properties();
    try
    {
      props.load(in);
      in.close();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    if (p == null) {
      p = new PropertyMap();
    }
    Iterator<Map.Entry<Object, Object>> it = props.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry<Object, Object> entry = (Map.Entry)it.next();
      String key = ((String)entry.getKey()).toLowerCase();
      String val = (String)entry.getValue();
      if (val != null) {
        val = val.trim();
      }
      p.put(key, val);
    }
    p.evaluateProperties();
    
    String otherProps = p.remove("load.properties");
    if (otherProps == null) {
      otherProps = p.remove("load.properties.override");
    }
    if (otherProps != null)
    {
      otherProps = otherProps.replace("\\", "/");
      InputStream is = findInputStream(otherProps);
      if (is != null)
      {
        logger.fine("loading properties from " + otherProps);
        load(p, is);
      }
      else
      {
        logger.severe("load.properties " + otherProps + " not found.");
      }
    }
    return p;
  }
  
  private static InputStream findInputStream(String fileName)
  {
    if (fileName == null) {
      throw new NullPointerException("fileName is null?");
    }
    if (servletContext == null)
    {
      logger.fine("No servletContext so not looking in WEB-INF for " + fileName);
    }
    else
    {
      InputStream in = servletContext.getResourceAsStream("/WEB-INF/" + fileName);
      if (in != null)
      {
        logger.fine(fileName + " found in WEB-INF");
        return in;
      }
    }
    try
    {
      File f = new File(fileName);
      if (f.exists())
      {
        logger.fine(fileName + " found in file system");
        return new FileInputStream(f);
      }
      InputStream in = findInClassPath(fileName);
      if (in != null) {
        logger.fine(fileName + " found in classpath");
      }
      return in;
    }
    catch (FileNotFoundException ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  private static InputStream findInClassPath(String fileName)
  {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
  }
}
