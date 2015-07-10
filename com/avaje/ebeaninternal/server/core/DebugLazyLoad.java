package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.config.GlobalProperties;
import java.util.ArrayList;
import java.util.List;

public class DebugLazyLoad
{
  private final String[] ignoreList;
  private final boolean debug;
  
  public DebugLazyLoad(boolean lazyLoadDebug)
  {
    this.ignoreList = buildLazyLoadIgnoreList();
    this.debug = lazyLoadDebug;
  }
  
  public boolean isDebug()
  {
    return this.debug;
  }
  
  public StackTraceElement getStackTraceElement(Class<?> beanType)
  {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    for (int i = 0; i < stackTrace.length; i++) {
      if (isStackLine(stackTrace[i], beanType)) {
        return stackTrace[i];
      }
    }
    return null;
  }
  
  private boolean isStackLine(StackTraceElement element, Class<?> beanType)
  {
    String stackClass = element.getClassName();
    if (isBeanClass(beanType, stackClass)) {
      return false;
    }
    for (int i = 0; i < this.ignoreList.length; i++) {
      if (stackClass.startsWith(this.ignoreList[i])) {
        return false;
      }
    }
    return true;
  }
  
  private boolean isBeanClass(Class<?> beanType, String stackClass)
  {
    if (stackClass.startsWith(beanType.getName())) {
      return true;
    }
    Class<?> superCls = beanType.getSuperclass();
    if (superCls.equals(Object.class)) {
      return false;
    }
    return isBeanClass(superCls, stackClass);
  }
  
  private String[] buildLazyLoadIgnoreList()
  {
    List<String> ignore = new ArrayList();
    
    ignore.add("com.avaje.ebean");
    ignore.add("java");
    ignore.add("sun.reflect");
    ignore.add("org.codehaus.groovy.runtime.");
    
    String extraIgnore = GlobalProperties.get("debug.lazyload.ignore", null);
    if (extraIgnore != null)
    {
      String[] split = extraIgnore.split(",");
      for (int i = 0; i < split.length; i++) {
        ignore.add(split[i].trim());
      }
    }
    return (String[])ignore.toArray(new String[ignore.size()]);
  }
}
