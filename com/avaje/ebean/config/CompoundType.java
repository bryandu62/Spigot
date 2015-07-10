package com.avaje.ebean.config;

public abstract interface CompoundType<V>
{
  public abstract V create(Object[] paramArrayOfObject);
  
  public abstract CompoundTypeProperty<V, ?>[] getProperties();
}
