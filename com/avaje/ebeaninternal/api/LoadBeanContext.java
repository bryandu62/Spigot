package com.avaje.ebeaninternal.api;

import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;

public abstract interface LoadBeanContext
  extends LoadSecondaryQuery
{
  public abstract void configureQuery(SpiQuery<?> paramSpiQuery, String paramString);
  
  public abstract String getFullPath();
  
  public abstract PersistenceContext getPersistenceContext();
  
  public abstract BeanDescriptor<?> getBeanDescriptor();
  
  public abstract int getBatchSize();
}
