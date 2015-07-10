package org.apache.logging.log4j.core.helpers;

import java.util.Locale;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class OptionConverter
{
  private static final Logger LOGGER = ;
  private static final String DELIM_START = "${";
  private static final char DELIM_STOP = '}';
  private static final int DELIM_START_LEN = 2;
  private static final int DELIM_STOP_LEN = 1;
  private static final int ONE_K = 1024;
  
  public static String[] concatenateArrays(String[] l, String[] r)
  {
    int len = l.length + r.length;
    String[] a = new String[len];
    
    System.arraycopy(l, 0, a, 0, l.length);
    System.arraycopy(r, 0, a, l.length, r.length);
    
    return a;
  }
  
  public static String convertSpecialChars(String s)
  {
    int len = s.length();
    StringBuilder sbuf = new StringBuilder(len);
    
    int i = 0;
    while (i < len)
    {
      char c = s.charAt(i++);
      if (c == '\\')
      {
        c = s.charAt(i++);
        if (c == 'n') {
          c = '\n';
        } else if (c == 'r') {
          c = '\r';
        } else if (c == 't') {
          c = '\t';
        } else if (c == 'f') {
          c = '\f';
        } else if (c == '\b') {
          c = '\b';
        } else if (c == '"') {
          c = '"';
        } else if (c == '\'') {
          c = '\'';
        } else if (c == '\\') {
          c = '\\';
        }
      }
      sbuf.append(c);
    }
    return sbuf.toString();
  }
  
  public static Object instantiateByKey(Properties props, String key, Class<?> superClass, Object defaultValue)
  {
    String className = findAndSubst(key, props);
    if (className == null)
    {
      LOGGER.error("Could not find value for key " + key);
      return defaultValue;
    }
    return instantiateByClassName(className.trim(), superClass, defaultValue);
  }
  
  public static boolean toBoolean(String value, boolean defaultValue)
  {
    if (value == null) {
      return defaultValue;
    }
    String trimmedVal = value.trim();
    if ("true".equalsIgnoreCase(trimmedVal)) {
      return true;
    }
    if ("false".equalsIgnoreCase(trimmedVal)) {
      return false;
    }
    return defaultValue;
  }
  
  public static int toInt(String value, int defaultValue)
  {
    if (value != null)
    {
      String s = value.trim();
      try
      {
        return Integer.parseInt(s);
      }
      catch (NumberFormatException e)
      {
        LOGGER.error("[" + s + "] is not in proper int form.");
        e.printStackTrace();
      }
    }
    return defaultValue;
  }
  
  public static long toFileSize(String value, long defaultValue)
  {
    if (value == null) {
      return defaultValue;
    }
    String str = value.trim().toUpperCase(Locale.ENGLISH);
    long multiplier = 1L;
    int index;
    if ((index = str.indexOf("KB")) != -1)
    {
      multiplier = 1024L;
      str = str.substring(0, index);
    }
    else if ((index = str.indexOf("MB")) != -1)
    {
      multiplier = 1048576L;
      str = str.substring(0, index);
    }
    else if ((index = str.indexOf("GB")) != -1)
    {
      multiplier = 1073741824L;
      str = str.substring(0, index);
    }
    if (str != null) {
      try
      {
        return Long.parseLong(str) * multiplier;
      }
      catch (NumberFormatException e)
      {
        LOGGER.error("[" + str + "] is not in proper int form.");
        LOGGER.error("[" + value + "] not in expected format.", e);
      }
    }
    return defaultValue;
  }
  
  public static String findAndSubst(String key, Properties props)
  {
    String value = props.getProperty(key);
    if (value == null) {
      return null;
    }
    try
    {
      return substVars(value, props);
    }
    catch (IllegalArgumentException e)
    {
      LOGGER.error("Bad option value [" + value + "].", e);
    }
    return value;
  }
  
  public static Object instantiateByClassName(String className, Class<?> superClass, Object defaultValue)
  {
    if (className != null) {
      try
      {
        Class<?> classObj = Loader.loadClass(className);
        if (!superClass.isAssignableFrom(classObj))
        {
          LOGGER.error("A \"" + className + "\" object is not assignable to a \"" + superClass.getName() + "\" variable.");
          
          LOGGER.error("The class \"" + superClass.getName() + "\" was loaded by ");
          LOGGER.error("[" + superClass.getClassLoader() + "] whereas object of type ");
          LOGGER.error("\"" + classObj.getName() + "\" was loaded by [" + classObj.getClassLoader() + "].");
          
          return defaultValue;
        }
        return classObj.newInstance();
      }
      catch (ClassNotFoundException e)
      {
        LOGGER.error("Could not instantiate class [" + className + "].", e);
      }
      catch (IllegalAccessException e)
      {
        LOGGER.error("Could not instantiate class [" + className + "].", e);
      }
      catch (InstantiationException e)
      {
        LOGGER.error("Could not instantiate class [" + className + "].", e);
      }
      catch (RuntimeException e)
      {
        LOGGER.error("Could not instantiate class [" + className + "].", e);
      }
    }
    return defaultValue;
  }
  
  public static String substVars(String val, Properties props)
    throws IllegalArgumentException
  {
    StringBuilder sbuf = new StringBuilder();
    
    int i = 0;
    for (;;)
    {
      int j = val.indexOf("${", i);
      if (j == -1)
      {
        if (i == 0) {
          return val;
        }
        sbuf.append(val.substring(i, val.length()));
        return sbuf.toString();
      }
      sbuf.append(val.substring(i, j));
      int k = val.indexOf('}', j);
      if (k == -1) {
        throw new IllegalArgumentException('"' + val + "\" has no closing brace. Opening brace at position " + j + '.');
      }
      j += 2;
      String key = val.substring(j, k);
      
      String replacement = PropertiesUtil.getProperties().getStringProperty(key, null);
      if ((replacement == null) && (props != null)) {
        replacement = props.getProperty(key);
      }
      if (replacement != null)
      {
        String recursiveReplacement = substVars(replacement, props);
        sbuf.append(recursiveReplacement);
      }
      i = k + 1;
    }
  }
}
