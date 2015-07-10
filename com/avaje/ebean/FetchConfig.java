package com.avaje.ebean;

import java.io.Serializable;

public class FetchConfig
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int lazyBatchSize = -1;
  private int queryBatchSize = -1;
  private boolean queryAll;
  
  public FetchConfig lazy()
  {
    this.lazyBatchSize = 0;
    return this;
  }
  
  public FetchConfig lazy(int lazyBatchSize)
  {
    this.lazyBatchSize = lazyBatchSize;
    return this;
  }
  
  public FetchConfig query()
  {
    this.queryBatchSize = 0;
    this.queryAll = true;
    return this;
  }
  
  public FetchConfig query(int queryBatchSize)
  {
    this.queryBatchSize = queryBatchSize;
    this.queryAll = true;
    return this;
  }
  
  public FetchConfig queryFirst(int queryBatchSize)
  {
    this.queryBatchSize = queryBatchSize;
    this.queryAll = false;
    return this;
  }
  
  public int getLazyBatchSize()
  {
    return this.lazyBatchSize;
  }
  
  public int getQueryBatchSize()
  {
    return this.queryBatchSize;
  }
  
  public boolean isQueryAll()
  {
    return this.queryAll;
  }
}
