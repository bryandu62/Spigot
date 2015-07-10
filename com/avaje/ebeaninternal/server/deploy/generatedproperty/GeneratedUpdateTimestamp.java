package com.avaje.ebeaninternal.server.deploy.generatedproperty;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import java.sql.Timestamp;

public class GeneratedUpdateTimestamp
  implements GeneratedProperty
{
  public Object getInsertValue(BeanProperty prop, Object bean)
  {
    return new Timestamp(System.currentTimeMillis());
  }
  
  public Object getUpdateValue(BeanProperty prop, Object bean)
  {
    return new Timestamp(System.currentTimeMillis());
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
