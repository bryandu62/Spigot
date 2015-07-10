package com.avaje.ebeaninternal.server.deploy;

public abstract interface CollectionTypeConverter
{
  public abstract Object toUnderlying(Object paramObject);
  
  public abstract Object toWrapped(Object paramObject);
}
