package com.avaje.ebeaninternal.server.deploy.generatedproperty;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import java.util.Date;

public class GeneratedInsertDate
  implements GeneratedProperty
{
  public Object getInsertValue(BeanProperty prop, Object bean)
  {
    return new Date(System.currentTimeMillis());
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
