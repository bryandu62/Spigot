package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.SqlFutureList;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import java.util.List;
import java.util.concurrent.FutureTask;

public class SqlQueryFutureList
  extends BaseFuture<List<SqlRow>>
  implements SqlFutureList
{
  private final SqlQuery query;
  
  public SqlQueryFutureList(SqlQuery query, FutureTask<List<SqlRow>> futureTask)
  {
    super(futureTask);
    this.query = query;
  }
  
  public SqlQuery getQuery()
  {
    return this.query;
  }
  
  public boolean cancel(boolean mayInterruptIfRunning)
  {
    this.query.cancel();
    return super.cancel(mayInterruptIfRunning);
  }
}
