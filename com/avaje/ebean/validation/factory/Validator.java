package com.avaje.ebean.validation.factory;

public abstract interface Validator
{
  public abstract String getKey();
  
  public abstract Object[] getAttributes();
  
  public abstract boolean isValid(Object paramObject);
}
