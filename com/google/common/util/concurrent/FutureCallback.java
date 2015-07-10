package com.google.common.util.concurrent;

import javax.annotation.Nullable;

public abstract interface FutureCallback<V>
{
  public abstract void onSuccess(@Nullable V paramV);
  
  public abstract void onFailure(Throwable paramThrowable);
}
