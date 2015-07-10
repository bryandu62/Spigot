package com.avaje.ebeaninternal.server.reflect;

public final class EnhanceBeanReflectFactory
  implements BeanReflectFactory
{
  public BeanReflect create(Class<?> vanillaType, Class<?> entityBeanType)
  {
    return new EnhanceBeanReflect(vanillaType, entityBeanType);
  }
}
