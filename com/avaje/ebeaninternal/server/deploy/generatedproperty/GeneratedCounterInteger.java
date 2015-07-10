package com.avaje.ebeaninternal.server.deploy.generatedproperty;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;

public class GeneratedCounterInteger
  implements GeneratedProperty
{
  public Object getInsertValue(BeanProperty prop, Object bean)
  {
    return Integer.valueOf(1);
  }
  
  public Object getUpdateValue(BeanProperty prop, Object bean)
  {
    Integer i = (Integer)prop.getValue(bean);
    return Integer.valueOf(i.intValue() + 1);
  }
  
  public boolean includeInUpdate()
  {
    return true;
  }
  
  public boolean includeInInsert()
  {
    return true;
  }
  
  public boolean isDDLNotNullable()
  {
    return true;
  }
}
