package com.avaje.ebean;

import java.util.List;
import java.util.concurrent.Future;

public abstract interface FutureIds<T>
  extends Future<List<Object>>
{
  public abstract Query<T> getQuery();
  
  public abstract List<Object> getPartialIds();
}
