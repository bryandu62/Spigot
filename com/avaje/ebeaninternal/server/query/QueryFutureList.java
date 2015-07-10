package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.FutureList;
import com.avaje.ebean.Query;
import java.util.List;
import java.util.concurrent.FutureTask;

public class QueryFutureList<T>
  extends BaseFuture<List<T>>
  implements FutureList<T>
{
  private final Query<T> query;
  
  public QueryFutureList(Query<T> query, FutureTask<List<T>> futureTask)
  {
    super(futureTask);
    this.query = query;
  }
  
  public Query<T> getQuery()
  {
    return this.query;
  }
  
  public boolean cancel(boolean mayInterruptIfRunning)
  {
    this.query.cancel();
    return super.cancel(mayInterruptIfRunning);
  }
}
