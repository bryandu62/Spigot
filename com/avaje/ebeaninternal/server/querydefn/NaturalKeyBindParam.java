package com.avaje.ebeaninternal.server.querydefn;

public class NaturalKeyBindParam
{
  private final String name;
  private final Object value;
  
  public NaturalKeyBindParam(String name, Object value)
  {
    this.name = name;
    this.value = value;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public Object getValue()
  {
    return this.value;
  }
}
