package com.avaje.ebeaninternal.server.deploy.generatedproperty;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;

public class GeneratedCounterLong
  implements GeneratedProperty
{
  public Object getInsertValue(BeanProperty prop, Object bean)
  {
    return Long.valueOf(1L);
  }
  
  public Object getUpdateValue(BeanProperty prop, Object bean)
  {
    Long i = (Long)prop.getValue(bean);
    return Long.valueOf(i.longValue() + 1L);
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
