package com.avaje.ebeaninternal.server.query;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class BaseFuture<T>
  implements Future<T>
{
  private final FutureTask<T> futureTask;
  
  public BaseFuture(FutureTask<T> futureTask)
  {
    this.futureTask = futureTask;
  }
  
  public boolean cancel(boolean mayInterruptIfRunning)
  {
    return this.futureTask.cancel(mayInterruptIfRunning);
  }
  
  public T get()
    throws InterruptedException, ExecutionException
  {
    return (T)this.futureTask.get();
  }
  
  public T get(long timeout, TimeUnit unit)
    throws InterruptedException, ExecutionException, TimeoutException
  {
    return (T)this.futureTask.get(timeout, unit);
  }
  
  public boolean isCancelled()
  {
    return this.futureTask.isCancelled();
  }
  
  public boolean isDone()
  {
    return this.futureTask.isDone();
  }
}
