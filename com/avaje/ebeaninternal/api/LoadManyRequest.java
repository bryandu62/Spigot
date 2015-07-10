package com.avaje.ebeaninternal.api;

import com.avaje.ebean.Transaction;
import com.avaje.ebean.bean.BeanCollection;
import java.util.List;

public class LoadManyRequest
  extends LoadRequest
{
  private final List<BeanCollection<?>> batch;
  private final LoadManyContext loadContext;
  private final boolean onlyIds;
  private final boolean loadCache;
  
  public LoadManyRequest(LoadManyContext loadContext, List<BeanCollection<?>> batch, Transaction transaction, int batchSize, boolean lazy, boolean onlyIds, boolean loadCache)
  {
    super(transaction, batchSize, lazy);
    this.loadContext = loadContext;
    this.batch = batch;
    this.onlyIds = onlyIds;
    this.loadCache = loadCache;
  }
  
  public String getDescription()
  {
    String fullPath = this.loadContext.getFullPath();
    String s = "path:" + fullPath + " batch:" + this.batchSize + " actual:" + this.batch.size();
    
    return s;
  }
  
  public List<BeanCollection<?>> getBatch()
  {
    return this.batch;
  }
  
  public LoadManyContext getLoadContext()
  {
    return this.loadContext;
  }
  
  public boolean isOnlyIds()
  {
    return this.onlyIds;
  }
  
  public boolean isLoadCache()
  {
    return this.loadCache;
  }
}
