package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public abstract interface CheckedFuture<V, X extends Exception>
  extends ListenableFuture<V>
{
  public abstract V checkedGet()
    throws Exception;
  
  public abstract V checkedGet(long paramLong, TimeUnit paramTimeUnit)
    throws TimeoutException, Exception;
}
