package com.avaje.ebeaninternal.api;

import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import javax.persistence.PersistenceException;

public class BeanIdList
{
  private final List<Object> idList;
  private boolean hasMore = true;
  private FutureTask<Integer> fetchFuture;
  
  public BeanIdList(List<Object> idList)
  {
    this.idList = idList;
  }
  
  public boolean isFetchingInBackground()
  {
    return this.fetchFuture != null;
  }
  
  public void setBackgroundFetch(FutureTask<Integer> fetchFuture)
  {
    this.fetchFuture = fetchFuture;
  }
  
  public void backgroundFetchWait(long wait, TimeUnit timeUnit)
  {
    if (this.fetchFuture != null) {
      try
      {
        this.fetchFuture.get(wait, timeUnit);
      }
      catch (Exception e)
      {
        throw new PersistenceException(e);
      }
    }
  }
  
  public void backgroundFetchWait()
  {
    if (this.fetchFuture != null) {
      try
      {
        this.fetchFuture.get();
      }
      catch (Exception e)
      {
        throw new PersistenceException(e);
      }
    }
  }
  
  public void add(Object id)
  {
    this.idList.add(id);
  }
  
  public List<Object> getIdList()
  {
    return this.idList;
  }
  
  public boolean isHasMore()
  {
    return this.hasMore;
  }
  
  public void setHasMore(boolean hasMore)
  {
    this.hasMore = hasMore;
  }
}
