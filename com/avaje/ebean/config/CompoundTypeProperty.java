package com.avaje.ebean.config;

public abstract interface CompoundTypeProperty<V, P>
{
  public abstract String getName();
  
  public abstract P getValue(V paramV);
  
  public abstract int getDbType();
}
