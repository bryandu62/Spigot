package com.avaje.ebean.bean;

public abstract interface BeanCollectionTouched
{
  public abstract void notifyTouched(BeanCollection<?> paramBeanCollection);
}
