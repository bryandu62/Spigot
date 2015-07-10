package com.avaje.ebeaninternal.server.reflect;

public abstract interface BeanReflectGetter
{
  public abstract Object get(Object paramObject);
  
  public abstract Object getIntercept(Object paramObject);
}
