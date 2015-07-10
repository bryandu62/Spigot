package com.avaje.ebeaninternal.server.deploy.generatedproperty;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;

public abstract interface GeneratedProperty
{
  public abstract Object getInsertValue(BeanProperty paramBeanProperty, Object paramObject);
  
  public abstract Object getUpdateValue(BeanProperty paramBeanProperty, Object paramObject);
  
  public abstract boolean includeInUpdate();
  
  public abstract boolean includeInInsert();
  
  public abstract boolean isDDLNotNullable();
}
