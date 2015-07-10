package com.avaje.ebean.bean;

public abstract interface BeanCollectionLoader
{
  public abstract String getName();
  
  public abstract void loadMany(BeanCollection<?> paramBeanCollection, boolean paramBoolean);
}
