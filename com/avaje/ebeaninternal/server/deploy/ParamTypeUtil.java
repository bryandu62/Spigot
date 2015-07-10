package com.avaje.ebeaninternal.server.deploy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParamTypeUtil
{
  public static Class<?> findParamType(Class<?> cls, Class<?> matchType)
  {
    Type paramType = matchByInterfaces(cls, matchType);
    if (paramType == null)
    {
      Type genericSuperclass = cls.getGenericSuperclass();
      if (genericSuperclass != null) {
        paramType = matchParamType(genericSuperclass, matchType);
      }
    }
    if ((paramType instanceof Class)) {
      return (Class)paramType;
    }
    return null;
  }
  
  private static Type matchParamType(Type type, Class<?> matchType)
  {
    if ((type instanceof ParameterizedType))
    {
      ParameterizedType pt = (ParameterizedType)type;
      Type rawType = pt.getRawType();
      boolean isAssignable = matchType.isAssignableFrom((Class)rawType);
      if (isAssignable)
      {
        Type[] typeArguments = pt.getActualTypeArguments();
        if (typeArguments.length != 1)
        {
          String m = "Expecting only 1 generic paramater but got " + typeArguments.length + " for " + type;
          throw new RuntimeException(m);
        }
        return typeArguments[0];
      }
    }
    return null;
  }
  
  private static Type matchByInterfaces(Class<?> cls, Class<?> matchType)
  {
    Type[] gis = cls.getGenericInterfaces();
    for (int i = 0; i < gis.length; i++)
    {
      Type match = matchParamType(gis[i], matchType);
      if (match != null) {
        return match;
      }
    }
    return null;
  }
}
