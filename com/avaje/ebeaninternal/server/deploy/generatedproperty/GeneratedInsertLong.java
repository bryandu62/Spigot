package com.avaje.ebeaninternal.server.deploy.generatedproperty;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;

public class GeneratedInsertLong
  implements GeneratedProperty
{
  public Object getInsertValue(BeanProperty prop, Object bean)
  {
    return Long.valueOf(System.currentTimeMillis());
  }
  
  public Object getUpdateValue(BeanProperty prop, Object bean)
  {
    return prop.getValue(bean);
  }
  
  public boolean includeInUpdate()
  {
    return false;
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
