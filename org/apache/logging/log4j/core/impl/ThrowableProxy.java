package org.apache.logging.log4j.core.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class ThrowableProxy
  implements Serializable
{
  private static final long serialVersionUID = -2752771578252251910L;
  private static final Logger LOGGER = ;
  private static final PrivateSecurityManager SECURITY_MANAGER;
  private static final Method GET_SUPPRESSED;
  private static final Method ADD_SUPPRESSED;
  private final ThrowableProxy proxyCause;
  private final Throwable throwable;
  private final String name;
  private final StackTracePackageElement[] callerPackageData;
  private int commonElementCount;
  
  static
  {
    if (ReflectiveCallerClassUtility.isSupported())
    {
      SECURITY_MANAGER = null;
    }
    else
    {
      PrivateSecurityManager securityManager;
      try
      {
        securityManager = new PrivateSecurityManager(null);
        if (securityManager.getClasses() == null)
        {
          securityManager = null;
          LOGGER.error("Unable to obtain call stack from security manager.");
        }
      }
      catch (Exception e)
      {
        securityManager = null;
        LOGGER.debug("Unable to install security manager.", e);
      }
      SECURITY_MANAGER = securityManager;
    }
    Method getSuppressed = null;Method addSuppressed = null;
    Method[] methods = Throwable.class.getMethods();
    for (Method method : methods) {
      if (method.getName().equals("getSuppressed")) {
        getSuppressed = method;
      } else if (method.getName().equals("addSuppressed")) {
        addSuppressed = method;
      }
    }
    GET_SUPPRESSED = getSuppressed;
    ADD_SUPPRESSED = addSuppressed;
  }
  
  public ThrowableProxy(Throwable throwable)
  {
    this.throwable = throwable;
    this.name = throwable.getClass().getName();
    Map<String, CacheEntry> map = new HashMap();
    Stack<Class<?>> stack = getCurrentStack();
    this.callerPackageData = resolvePackageData(stack, map, null, throwable.getStackTrace());
    this.proxyCause = (throwable.getCause() == null ? null : new ThrowableProxy(throwable, stack, map, throwable.getCause()));
    
    setSuppressed(throwable);
  }
  
  private ThrowableProxy(Throwable parent, Stack<Class<?>> stack, Map<String, CacheEntry> map, Throwable cause)
  {
    this.throwable = cause;
    this.name = cause.getClass().getName();
    this.callerPackageData = resolvePackageData(stack, map, parent.getStackTrace(), cause.getStackTrace());
    this.proxyCause = (cause.getCause() == null ? null : new ThrowableProxy(parent, stack, map, cause.getCause()));
    
    setSuppressed(cause);
  }
  
  public Throwable getThrowable()
  {
    return this.throwable;
  }
  
  public ThrowableProxy getCause()
  {
    return this.proxyCause;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public int getCommonElementCount()
  {
    return this.commonElementCount;
  }
  
  public StackTracePackageElement[] getPackageData()
  {
    return this.callerPackageData;
  }
  
  public String toString()
  {
    String msg = this.throwable.getMessage();
    return msg != null ? this.name + ": " + msg : this.name;
  }
  
  public String getRootCauseStackTrace()
  {
    return getRootCauseStackTrace(null);
  }
  
  public String getRootCauseStackTrace(List<String> packages)
  {
    StringBuilder sb = new StringBuilder();
    if (this.proxyCause != null)
    {
      formatWrapper(sb, this.proxyCause);
      sb.append("Wrapped by: ");
    }
    sb.append(toString());
    sb.append("\n");
    formatElements(sb, 0, this.throwable.getStackTrace(), this.callerPackageData, packages);
    return sb.toString();
  }
  
  public void formatWrapper(StringBuilder sb, ThrowableProxy cause)
  {
    formatWrapper(sb, cause, null);
  }
  
  public void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> packages)
  {
    Throwable caused = cause.getCause() != null ? cause.getCause().getThrowable() : null;
    if (caused != null)
    {
      formatWrapper(sb, cause.proxyCause);
      sb.append("Wrapped by: ");
    }
    sb.append(cause).append("\n");
    formatElements(sb, cause.commonElementCount, cause.getThrowable().getStackTrace(), cause.callerPackageData, packages);
  }
  
  public String getExtendedStackTrace()
  {
    return getExtendedStackTrace(null);
  }
  
  public String getExtendedStackTrace(List<String> packages)
  {
    StringBuilder sb = new StringBuilder(this.name);
    String msg = this.throwable.getMessage();
    if (msg != null) {
      sb.append(": ").append(this.throwable.getMessage());
    }
    sb.append("\n");
    formatElements(sb, 0, this.throwable.getStackTrace(), this.callerPackageData, packages);
    if (this.proxyCause != null) {
      formatCause(sb, this.proxyCause, packages);
    }
    return sb.toString();
  }
  
  public String getSuppressedStackTrace()
  {
    ThrowableProxy[] suppressed = getSuppressed();
    if ((suppressed == null) || (suppressed.length == 0)) {
      return "";
    }
    StringBuilder sb = new StringBuilder("Suppressed Stack Trace Elements:\n");
    for (ThrowableProxy proxy : suppressed) {
      sb.append(proxy.getExtendedStackTrace());
    }
    return sb.toString();
  }
  
  private void formatCause(StringBuilder sb, ThrowableProxy cause, List<String> packages)
  {
    sb.append("Caused by: ").append(cause).append("\n");
    formatElements(sb, cause.commonElementCount, cause.getThrowable().getStackTrace(), cause.callerPackageData, packages);
    if (cause.getCause() != null) {
      formatCause(sb, cause.proxyCause, packages);
    }
  }
  
  private void formatElements(StringBuilder sb, int commonCount, StackTraceElement[] causedTrace, StackTracePackageElement[] packageData, List<String> packages)
  {
    if ((packages == null) || (packages.size() == 0))
    {
      for (int i = 0; i < packageData.length; i++) {
        formatEntry(causedTrace[i], packageData[i], sb);
      }
    }
    else
    {
      int count = 0;
      for (int i = 0; i < packageData.length; i++) {
        if (!isSuppressed(causedTrace[i], packages))
        {
          if (count > 0)
          {
            if (count == 1) {
              sb.append("\t....\n");
            } else {
              sb.append("\t... suppressed ").append(count).append(" lines\n");
            }
            count = 0;
          }
          formatEntry(causedTrace[i], packageData[i], sb);
        }
        else
        {
          count++;
        }
      }
      if (count > 0) {
        if (count == 1) {
          sb.append("\t...\n");
        } else {
          sb.append("\t... suppressed ").append(count).append(" lines\n");
        }
      }
    }
    if (commonCount != 0) {
      sb.append("\t... ").append(commonCount).append(" more").append("\n");
    }
  }
  
  private void formatEntry(StackTraceElement element, StackTracePackageElement packageData, StringBuilder sb)
  {
    sb.append("\tat ");
    sb.append(element);
    sb.append(" ");
    sb.append(packageData);
    sb.append("\n");
  }
  
  private boolean isSuppressed(StackTraceElement element, List<String> packages)
  {
    String className = element.getClassName();
    for (String pkg : packages) {
      if (className.startsWith(pkg)) {
        return true;
      }
    }
    return false;
  }
  
  private Stack<Class<?>> getCurrentStack()
  {
    if (ReflectiveCallerClassUtility.isSupported())
    {
      Stack<Class<?>> classes = new Stack();
      int index = 1;
      for (Class<?> clazz = ReflectiveCallerClassUtility.getCaller(index); clazz != null; clazz = ReflectiveCallerClassUtility.getCaller(index))
      {
        classes.push(clazz);
        index++;
      }
      return classes;
    }
    if (SECURITY_MANAGER != null)
    {
      Class<?>[] array = SECURITY_MANAGER.getClasses();
      Stack<Class<?>> classes = new Stack();
      for (Class<?> clazz : array) {
        classes.push(clazz);
      }
      return classes;
    }
    return new Stack();
  }
  
  StackTracePackageElement[] resolvePackageData(Stack<Class<?>> stack, Map<String, CacheEntry> map, StackTraceElement[] rootTrace, StackTraceElement[] stackTrace)
  {
    int stackLength;
    int stackLength;
    if (rootTrace != null)
    {
      int rootIndex = rootTrace.length - 1;
      int stackIndex = stackTrace.length - 1;
      while ((rootIndex >= 0) && (stackIndex >= 0) && (rootTrace[rootIndex].equals(stackTrace[stackIndex])))
      {
        rootIndex--;
        stackIndex--;
      }
      this.commonElementCount = (stackTrace.length - 1 - stackIndex);
      stackLength = stackIndex + 1;
    }
    else
    {
      this.commonElementCount = 0;
      stackLength = stackTrace.length;
    }
    StackTracePackageElement[] packageArray = new StackTracePackageElement[stackLength];
    Class<?> clazz = stack.isEmpty() ? null : (Class)stack.peek();
    ClassLoader lastLoader = null;
    for (int i = stackLength - 1; i >= 0; i--)
    {
      String className = stackTrace[i].getClassName();
      if ((clazz != null) && (className.equals(clazz.getName())))
      {
        CacheEntry entry = resolvePackageElement(clazz, true);
        packageArray[i] = entry.element;
        lastLoader = entry.loader;
        stack.pop();
        clazz = stack.isEmpty() ? null : (Class)stack.peek();
      }
      else if (map.containsKey(className))
      {
        CacheEntry entry = (CacheEntry)map.get(className);
        packageArray[i] = entry.element;
        if (entry.loader != null) {
          lastLoader = entry.loader;
        }
      }
      else
      {
        CacheEntry entry = resolvePackageElement(loadClass(lastLoader, className), false);
        packageArray[i] = entry.element;
        map.put(className, entry);
        if (entry.loader != null) {
          lastLoader = entry.loader;
        }
      }
    }
    return packageArray;
  }
  
  private CacheEntry resolvePackageElement(Class<?> callerClass, boolean exact)
  {
    String location = "?";
    String version = "?";
    ClassLoader lastLoader = null;
    if (callerClass != null)
    {
      try
      {
        CodeSource source = callerClass.getProtectionDomain().getCodeSource();
        if (source != null)
        {
          URL locationURL = source.getLocation();
          if (locationURL != null)
          {
            String str = locationURL.toString().replace('\\', '/');
            int index = str.lastIndexOf("/");
            if ((index >= 0) && (index == str.length() - 1))
            {
              index = str.lastIndexOf("/", index - 1);
              location = str.substring(index + 1);
            }
            else
            {
              location = str.substring(index + 1);
            }
          }
        }
      }
      catch (Exception ex) {}
      Package pkg = callerClass.getPackage();
      if (pkg != null)
      {
        String ver = pkg.getImplementationVersion();
        if (ver != null) {
          version = ver;
        }
      }
      lastLoader = callerClass.getClassLoader();
    }
    return new CacheEntry(new StackTracePackageElement(location, version, exact), lastLoader);
  }
  
  private Class<?> loadClass(ClassLoader lastLoader, String className)
  {
    Class<?> clazz;
    if (lastLoader != null) {
      try
      {
        clazz = lastLoader.loadClass(className);
        if (clazz != null) {
          return clazz;
        }
      }
      catch (Exception ex) {}
    }
    try
    {
      clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
    }
    catch (ClassNotFoundException e)
    {
      try
      {
        clazz = Class.forName(className);
      }
      catch (ClassNotFoundException e1)
      {
        try
        {
          clazz = getClass().getClassLoader().loadClass(className);
        }
        catch (ClassNotFoundException e2)
        {
          return null;
        }
      }
    }
    return clazz;
  }
  
  public ThrowableProxy[] getSuppressed()
  {
    if (GET_SUPPRESSED != null) {
      try
      {
        return (ThrowableProxy[])GET_SUPPRESSED.invoke(this.throwable, new Object[0]);
      }
      catch (Exception ignore)
      {
        return null;
      }
    }
    return null;
  }
  
  private void setSuppressed(Throwable throwable)
  {
    if ((GET_SUPPRESSED != null) && (ADD_SUPPRESSED != null)) {
      try
      {
        Throwable[] array = (Throwable[])GET_SUPPRESSED.invoke(throwable, new Object[0]);
        for (Throwable t : array) {
          ADD_SUPPRESSED.invoke(this, new Object[] { new ThrowableProxy(t) });
        }
      }
      catch (Exception ignore) {}
    }
  }
  
  class CacheEntry
  {
    private final StackTracePackageElement element;
    private final ClassLoader loader;
    
    public CacheEntry(StackTracePackageElement element, ClassLoader loader)
    {
      this.element = element;
      this.loader = loader;
    }
  }
  
  private static class PrivateSecurityManager
    extends SecurityManager
  {
    public Class<?>[] getClasses()
    {
      return getClassContext();
    }
  }
}
