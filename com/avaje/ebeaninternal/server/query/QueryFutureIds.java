package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.FutureIds;
import com.avaje.ebean.Query;
import com.avaje.ebeaninternal.api.SpiQuery;
import java.util.List;
import java.util.concurrent.FutureTask;

public class QueryFutureIds<T>
  extends BaseFuture<List<Object>>
  implements FutureIds<T>
{
  private final SpiQuery<T> query;
  
  public QueryFutureIds(SpiQuery<T> query, FutureTask<List<Object>> futureTask)
  {
    super(futureTask);
    this.query = query;
  }
  
  public Query<T> getQuery()
  {
    return this.query;
  }
  
  public List<Object> getPartialIds()
  {
    return this.query.getIdList();
  }
  
  public boolean cancel(boolean mayInterruptIfRunning)
  {
    this.query.cancel();
    return super.cancel(mayInterruptIfRunning);
  }
}
