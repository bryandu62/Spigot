package com.avaje.ebeaninternal.server.reflect;

public abstract interface BeanReflect
{
  public abstract Object createEntityBean();
  
  public abstract Object createVanillaBean();
  
  public abstract boolean isVanillaOnly();
  
  public abstract BeanReflectGetter getGetter(String paramString);
  
  public abstract BeanReflectSetter getSetter(String paramString);
}
