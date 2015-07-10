package com.avaje.ebean.bean;

public abstract interface PersistenceContext
{
  public abstract void put(Object paramObject1, Object paramObject2);
  
  public abstract Object putIfAbsent(Object paramObject1, Object paramObject2);
  
  public abstract Object get(Class<?> paramClass, Object paramObject);
  
  public abstract void clear();
  
  public abstract void clear(Class<?> paramClass);
  
  public abstract void clear(Class<?> paramClass, Object paramObject);
  
  public abstract int size(Class<?> paramClass);
}
