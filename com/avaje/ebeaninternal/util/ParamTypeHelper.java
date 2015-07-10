package com.avaje.ebeaninternal.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParamTypeHelper
{
  public static enum ManyType
  {
    LIST,  SET,  MAP,  NONE;
    
    private ManyType() {}
  }
  
  public static class TypeInfo
  {
    private final ParamTypeHelper.ManyType manyType;
    private final Class<?> beanType;
    
    private TypeInfo(ParamTypeHelper.ManyType manyType, Class<?> beanType)
    {
      this.manyType = manyType;
      this.beanType = beanType;
    }
    
    public boolean isManyType()
    {
      return !ParamTypeHelper.ManyType.NONE.equals(this.manyType);
    }
    
    public ParamTypeHelper.ManyType getManyType()
    {
      return this.manyType;
    }
    
    public Class<?> getBeanType()
    {
      return this.beanType;
    }
    
    public String toString()
    {
      if (isManyType()) {
        return this.manyType + " " + this.beanType;
      }
      return this.beanType.toString();
    }
  }
  
  public static TypeInfo getTypeInfo(Type genericType)
  {
    if ((genericType instanceof ParameterizedType)) {
      return getParamTypeInfo((ParameterizedType)genericType);
    }
    Class<?> entityType = getBeanType(genericType);
    if (entityType != null) {
      return new TypeInfo(ManyType.NONE, entityType, null);
    }
    return null;
  }
  
  private static TypeInfo getParamTypeInfo(ParameterizedType paramType)
  {
    Type rawType = paramType.getRawType();
    
    ManyType manyType = getManyType(rawType);
    if (ManyType.NONE.equals(manyType)) {
      return null;
    }
    Type[] typeArguments = paramType.getActualTypeArguments();
    if (typeArguments.length == 1)
    {
      Type argType = typeArguments[0];
      Class<?> beanType = getBeanType(argType);
      if (beanType != null) {
        return new TypeInfo(manyType, beanType, null);
      }
    }
    return null;
  }
  
  private static Class<?> getBeanType(Type argType)
  {
    if ((argType instanceof Class)) {
      return (Class)argType;
    }
    return null;
  }
  
  private static ManyType getManyType(Type rawType)
  {
    if (List.class.equals(rawType)) {
      return ManyType.LIST;
    }
    if (Set.class.equals(rawType)) {
      return ManyType.SET;
    }
    if (Map.class.equals(rawType)) {
      return ManyType.MAP;
    }
    return ManyType.NONE;
  }
}
