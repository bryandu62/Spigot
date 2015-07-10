package com.avaje.ebeaninternal.api;

import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;

public abstract interface LoadManyContext
  extends LoadSecondaryQuery
{
  public abstract void configureQuery(SpiQuery<?> paramSpiQuery);
  
  public abstract String getFullPath();
  
  public abstract ObjectGraphNode getObjectGraphNode();
  
  public abstract PersistenceContext getPersistenceContext();
  
  public abstract int getBatchSize();
  
  public abstract BeanDescriptor<?> getBeanDescriptor();
  
  public abstract BeanPropertyAssocMany<?> getBeanProperty();
}
