package com.avaje.ebeaninternal.server.type.reflect;

import com.avaje.ebean.config.ScalarTypeConverter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionBasedScalarTypeConverter
  implements ScalarTypeConverter
{
  private static final Object[] NO_ARGS = new Object[0];
  private final Constructor<?> constructor;
  private final Method reader;
  
  public ReflectionBasedScalarTypeConverter(Constructor<?> constructor, Method reader)
  {
    this.constructor = constructor;
    this.reader = reader;
  }
  
  public Object getNullValue()
  {
    return null;
  }
  
  public Object unwrapValue(Object beanType)
  {
    if (beanType == null) {
      return null;
    }
    try
    {
      return this.reader.invoke(beanType, NO_ARGS);
    }
    catch (Exception e)
    {
      String msg = "Error invoking read method " + this.reader.getName() + " on " + beanType.getClass().getName();
      
      throw new RuntimeException(msg);
    }
  }
  
  public Object wrapValue(Object scalarType)
  {
    try
    {
      return this.constructor.newInstance(new Object[] { scalarType });
    }
    catch (Exception e)
    {
      String msg = "Error invoking constructor " + this.constructor + " with " + scalarType;
      throw new RuntimeException(msg);
    }
  }
}
