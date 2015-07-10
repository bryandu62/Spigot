package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.FutureRowCount;
import com.avaje.ebean.Query;
import java.util.concurrent.FutureTask;

public class QueryFutureRowCount<T>
  extends BaseFuture<Integer>
  implements FutureRowCount<T>
{
  private final Query<T> query;
  
  public QueryFutureRowCount(Query<T> query, FutureTask<Integer> futureTask)
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
