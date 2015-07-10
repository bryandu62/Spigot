package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;

@Beta
public abstract interface FutureFallback<V>
{
  public abstract ListenableFuture<V> create(Throwable paramThrowable)
    throws Exception;
}
