package com.avaje.ebean;

import java.util.List;
import java.util.concurrent.Future;

public abstract interface FutureList<T>
  extends Future<List<T>>
{
  public abstract Query<T> getQuery();
}
