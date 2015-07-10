package com.avaje.ebean.bean;

public abstract interface BeanLoader
{
  public abstract String getName();
  
  public abstract void loadBean(EntityBeanIntercept paramEntityBeanIntercept);
}
