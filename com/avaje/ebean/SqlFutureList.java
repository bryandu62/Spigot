package com.avaje.ebean;

import java.util.List;
import java.util.concurrent.Future;

public abstract interface SqlFutureList
  extends Future<List<SqlRow>>
{
  public abstract SqlQuery getQuery();
}
