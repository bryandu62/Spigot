package com.avaje.ebean.event;

public abstract interface BeanQueryAdapter
{
  public abstract boolean isRegisterFor(Class<?> paramClass);
  
  public abstract int getExecutionOrder();
  
  public abstract void preQuery(BeanQueryRequest<?> paramBeanQueryRequest);
}
