package com.avaje.ebeaninternal.api;

import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;

public abstract interface LoadContext
{
  public abstract int getSecondaryQueriesMinBatchSize(OrmQueryRequest<?> paramOrmQueryRequest, int paramInt);
  
  public abstract void executeSecondaryQueries(OrmQueryRequest<?> paramOrmQueryRequest, int paramInt);
  
  public abstract void registerSecondaryQueries(SpiQuery<?> paramSpiQuery);
  
  public abstract ObjectGraphNode getObjectGraphNode(String paramString);
  
  public abstract PersistenceContext getPersistenceContext();
  
  public abstract void setPersistenceContext(PersistenceContext paramPersistenceContext);
  
  public abstract void register(String paramString, EntityBeanIntercept paramEntityBeanIntercept);
  
  public abstract void register(String paramString, BeanCollection<?> paramBeanCollection);
}
