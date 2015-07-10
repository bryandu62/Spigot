package com.avaje.ebean.config;

public abstract interface ScalarTypeConverter<B, S>
{
  public abstract B getNullValue();
  
  public abstract B wrapValue(S paramS);
  
  public abstract S unwrapValue(B paramB);
}
