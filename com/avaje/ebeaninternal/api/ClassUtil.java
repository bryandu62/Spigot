package com.avaje.ebeaninternal.api;

import java.util.logging.Logger;

public class ClassUtil
{
  private static final Logger logger = Logger.getLogger(ClassUtil.class.getName());
  private static boolean preferContext = true;
  
  public static Class<?> forName(String name)
    throws ClassNotFoundException
  {
    return forName(name, null);
  }
  
  public static Class<?> forName(String name, Class<?> caller)
    throws ClassNotFoundException
  {
    if (caller == null) {
      caller = ClassUtil.class;
    }
    ClassLoadContext ctx = ClassLoadContext.of(caller, preferContext);
    
    return ctx.forName(name);
  }
  
  public static ClassLoader getClassLoader(Class<?> caller, boolean preferContext)
  {
    if (caller == null) {
      caller = ClassUtil.class;
    }
    ClassLoadContext ctx = ClassLoadContext.of(caller, preferContext);
    ClassLoader classLoader = ctx.getDefault(preferContext);
    if (ctx.isAmbiguous()) {
      logger.info("Ambigous ClassLoader (Context vs Caller) chosen " + classLoader);
    }
    return classLoader;
  }
  
  public static boolean isPresent(String className)
  {
    return isPresent(className, null);
  }
  
  public static boolean isPresent(String className, Class<?> caller)
  {
    try
    {
      forName(className, caller);
      return true;
    }
    catch (Throwable ex) {}
    return false;
  }
  
  public static Object newInstance(String className)
  {
    return newInstance(className, null);
  }
  
  public static Object newInstance(String className, Class<?> caller)
  {
    try
    {
      Class<?> cls = forName(className, caller);
      return cls.newInstance();
    }
    catch (Exception e)
    {
      String msg = "Error constructing " + className;
      throw new IllegalArgumentException(msg, e);
    }
  }
}
