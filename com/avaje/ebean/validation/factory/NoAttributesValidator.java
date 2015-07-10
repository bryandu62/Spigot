package com.avaje.ebean.validation.factory;

public abstract class NoAttributesValidator
  implements Validator
{
  private static final Object[] EMPTY = new Object[0];
  
  public Object[] getAttributes()
  {
    return EMPTY;
  }
}
