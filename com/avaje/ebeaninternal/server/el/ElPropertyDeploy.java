package com.avaje.ebeaninternal.server.el;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;

public abstract interface ElPropertyDeploy
{
  public static final String ROOT_ELPREFIX = "${}";
  
  public abstract boolean containsMany();
  
  public abstract boolean containsManySince(String paramString);
  
  public abstract String getElPrefix();
  
  public abstract String getElPlaceholder(boolean paramBoolean);
  
  public abstract String getName();
  
  public abstract String getElName();
  
  public abstract String getDbColumn();
  
  public abstract BeanProperty getBeanProperty();
}
