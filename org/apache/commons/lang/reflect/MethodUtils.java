package org.apache.commons.lang.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;

public class MethodUtils
{
  public static Object invokeMethod(Object object, String methodName, Object arg)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    return invokeMethod(object, methodName, new Object[] { arg });
  }
  
  public static Object invokeMethod(Object object, String methodName, Object[] args)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    if (args == null) {
      args = ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
    int arguments = args.length;
    Class[] parameterTypes = new Class[arguments];
    for (int i = 0; i < arguments; i++) {
      parameterTypes[i] = args[i].getClass();
    }
    return invokeMethod(object, methodName, args, parameterTypes);
  }
  
  public static Object invokeMethod(Object object, String methodName, Object[] args, Class[] parameterTypes)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    if (parameterTypes == null) {
      parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    if (args == null) {
      args = ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
    Method method = getMatchingAccessibleMethod(object.getClass(), methodName, parameterTypes);
    if (method == null) {
      throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
    }
    return method.invoke(object, args);
  }
  
  public static Object invokeExactMethod(Object object, String methodName, Object arg)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    return invokeExactMethod(object, methodName, new Object[] { arg });
  }
  
  public static Object invokeExactMethod(Object object, String methodName, Object[] args)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    if (args == null) {
      args = ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
    int arguments = args.length;
    Class[] parameterTypes = new Class[arguments];
    for (int i = 0; i < arguments; i++) {
      parameterTypes[i] = args[i].getClass();
    }
    return invokeExactMethod(object, methodName, args, parameterTypes);
  }
  
  public static Object invokeExactMethod(Object object, String methodName, Object[] args, Class[] parameterTypes)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    if (args == null) {
      args = ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
    if (parameterTypes == null) {
      parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    Method method = getAccessibleMethod(object.getClass(), methodName, parameterTypes);
    if (method == null) {
      throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
    }
    return method.invoke(object, args);
  }
  
  public static Object invokeExactStaticMethod(Class cls, String methodName, Object[] args, Class[] parameterTypes)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    if (args == null) {
      args = ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
    if (parameterTypes == null) {
      parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    Method method = getAccessibleMethod(cls, methodName, parameterTypes);
    if (method == null) {
      throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName());
    }
    return method.invoke(null, args);
  }
  
  public static Object invokeStaticMethod(Class cls, String methodName, Object arg)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    return invokeStaticMethod(cls, methodName, new Object[] { arg });
  }
  
  public static Object invokeStaticMethod(Class cls, String methodName, Object[] args)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    if (args == null) {
      args = ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
    int arguments = args.length;
    Class[] parameterTypes = new Class[arguments];
    for (int i = 0; i < arguments; i++) {
      parameterTypes[i] = args[i].getClass();
    }
    return invokeStaticMethod(cls, methodName, args, parameterTypes);
  }
  
  public static Object invokeStaticMethod(Class cls, String methodName, Object[] args, Class[] parameterTypes)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    if (parameterTypes == null) {
      parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    if (args == null) {
      args = ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
    Method method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
    if (method == null) {
      throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName());
    }
    return method.invoke(null, args);
  }
  
  public static Object invokeExactStaticMethod(Class cls, String methodName, Object arg)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    return invokeExactStaticMethod(cls, methodName, new Object[] { arg });
  }
  
  public static Object invokeExactStaticMethod(Class cls, String methodName, Object[] args)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    if (args == null) {
      args = ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
    int arguments = args.length;
    Class[] parameterTypes = new Class[arguments];
    for (int i = 0; i < arguments; i++) {
      parameterTypes[i] = args[i].getClass();
    }
    return invokeExactStaticMethod(cls, methodName, args, parameterTypes);
  }
  
  public static Method getAccessibleMethod(Class cls, String methodName, Class parameterType)
  {
    return getAccessibleMethod(cls, methodName, new Class[] { parameterType });
  }
  
  public static Method getAccessibleMethod(Class cls, String methodName, Class[] parameterTypes)
  {
    try
    {
      return getAccessibleMethod(cls.getMethod(methodName, parameterTypes));
    }
    catch (NoSuchMethodException e) {}
    return null;
  }
  
  public static Method getAccessibleMethod(Method method)
  {
    if (!MemberUtils.isAccessible(method)) {
      return null;
    }
    Class cls = method.getDeclaringClass();
    if (Modifier.isPublic(cls.getModifiers())) {
      return method;
    }
    String methodName = method.getName();
    Class[] parameterTypes = method.getParameterTypes();
    
    method = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
    if (method == null) {
      method = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
    }
    return method;
  }
  
  private static Method getAccessibleMethodFromSuperclass(Class cls, String methodName, Class[] parameterTypes)
  {
    Class parentClass = cls.getSuperclass();
    while (parentClass != null)
    {
      if (Modifier.isPublic(parentClass.getModifiers())) {
        try
        {
          return parentClass.getMethod(methodName, parameterTypes);
        }
        catch (NoSuchMethodException e)
        {
          return null;
        }
      }
      parentClass = parentClass.getSuperclass();
    }
    return null;
  }
  
  private static Method getAccessibleMethodFromInterfaceNest(Class cls, String methodName, Class[] parameterTypes)
  {
    Method method = null;
    for (; cls != null; cls = cls.getSuperclass())
    {
      Class[] interfaces = cls.getInterfaces();
      for (int i = 0; i < interfaces.length; i++) {
        if (Modifier.isPublic(interfaces[i].getModifiers()))
        {
          try
          {
            method = interfaces[i].getDeclaredMethod(methodName, parameterTypes);
          }
          catch (NoSuchMethodException e) {}
          if (method != null) {
            break;
          }
          method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
          if (method != null) {
            break;
          }
        }
      }
    }
    return method;
  }
  
  public static Method getMatchingAccessibleMethod(Class cls, String methodName, Class[] parameterTypes)
  {
    try
    {
      Method method = cls.getMethod(methodName, parameterTypes);
      MemberUtils.setAccessibleWorkaround(method);
      return method;
    }
    catch (NoSuchMethodException e)
    {
      Method bestMatch = null;
      Method[] methods = cls.getMethods();
      int i = 0;
      for (int size = methods.length; i < size; i++) {
        if (methods[i].getName().equals(methodName)) {
          if (ClassUtils.isAssignable(parameterTypes, methods[i].getParameterTypes(), true))
          {
            Method accessibleMethod = getAccessibleMethod(methods[i]);
            if ((accessibleMethod != null) && (
              (bestMatch == null) || (MemberUtils.compareParameterTypes(accessibleMethod.getParameterTypes(), bestMatch.getParameterTypes(), parameterTypes) < 0))) {
              bestMatch = accessibleMethod;
            }
          }
        }
      }
      if (bestMatch != null) {
        MemberUtils.setAccessibleWorkaround(bestMatch);
      }
      return bestMatch;
    }
  }
}
