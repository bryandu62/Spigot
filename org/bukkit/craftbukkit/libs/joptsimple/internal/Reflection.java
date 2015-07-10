package org.bukkit.craftbukkit.libs.joptsimple.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.bukkit.craftbukkit.libs.joptsimple.ValueConverter;

public final class Reflection
{
  static
  {
    new Reflection();
  }
  
  public static <V> ValueConverter<V> findConverter(Class<V> clazz)
  {
    ValueConverter<V> valueOf = valueOfConverter(clazz);
    if (valueOf != null) {
      return valueOf;
    }
    ValueConverter<V> constructor = constructorConverter(clazz);
    if (constructor != null) {
      return constructor;
    }
    throw new IllegalArgumentException(clazz + " is not a value type");
  }
  
  private static <V> ValueConverter<V> valueOfConverter(Class<V> clazz)
  {
    try
    {
      Method valueOf = clazz.getDeclaredMethod("valueOf", new Class[] { String.class });
      if (!meetsConverterRequirements(valueOf, clazz)) {
        return null;
      }
      return new MethodInvokingValueConverter(valueOf, clazz);
    }
    catch (NoSuchMethodException ignored) {}
    return null;
  }
  
  private static <V> ValueConverter<V> constructorConverter(Class<V> clazz)
  {
    try
    {
      return new ConstructorInvokingValueConverter(clazz.getConstructor(new Class[] { String.class }));
    }
    catch (NoSuchMethodException ignored) {}
    return null;
  }
  
  public static <T> T instantiate(Constructor<T> constructor, Object... args)
  {
    try
    {
      return (T)constructor.newInstance(args);
    }
    catch (Exception ex)
    {
      throw reflectionException(ex);
    }
  }
  
  public static Object invoke(Method method, Object... args)
  {
    try
    {
      return method.invoke(null, args);
    }
    catch (Exception ex)
    {
      throw reflectionException(ex);
    }
  }
  
  private static boolean meetsConverterRequirements(Method method, Class<?> expectedReturnType)
  {
    int modifiers = method.getModifiers();
    return (Modifier.isPublic(modifiers)) && (Modifier.isStatic(modifiers)) && (expectedReturnType.equals(method.getReturnType()));
  }
  
  private static RuntimeException reflectionException(Exception ex)
  {
    if ((ex instanceof IllegalArgumentException)) {
      return new ReflectionException(ex);
    }
    if ((ex instanceof InvocationTargetException)) {
      return new ReflectionException(ex.getCause());
    }
    if ((ex instanceof RuntimeException)) {
      return (RuntimeException)ex;
    }
    return new ReflectionException(ex);
  }
}
