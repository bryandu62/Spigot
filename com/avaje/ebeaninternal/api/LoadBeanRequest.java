package com.avaje.ebeaninternal.api;

import com.avaje.ebean.Transaction;
import com.avaje.ebean.bean.EntityBeanIntercept;
import java.util.List;

public class LoadBeanRequest
  extends LoadRequest
{
  private final List<EntityBeanIntercept> batch;
  private final LoadBeanContext loadContext;
  private final String lazyLoadProperty;
  private final boolean loadCache;
  
  public LoadBeanRequest(LoadBeanContext loadContext, List<EntityBeanIntercept> batch, Transaction transaction, int batchSize, boolean lazy, String lazyLoadProperty, boolean loadCache)
  {
    super(transaction, batchSize, lazy);
    this.loadContext = loadContext;
    this.batch = batch;
    this.lazyLoadProperty = lazyLoadProperty;
    this.loadCache = loadCache;
  }
  
  public boolean isLoadCache()
  {
    return this.loadCache;
  }
  
  public String getDescription()
  {
    String fullPath = this.loadContext.getFullPath();
    String s = "path:" + fullPath + " batch:" + this.batchSize + " actual:" + this.batch.size();
    
    return s;
  }
  
  public List<EntityBeanIntercept> getBatch()
  {
    return this.batch;
  }
  
  public LoadBeanContext getLoadContext()
  {
    return this.loadContext;
  }
  
  public String getLazyLoadProperty()
  {
    return this.lazyLoadProperty;
  }
}
