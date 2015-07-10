package com.avaje.ebeaninternal.api;

import com.avaje.ebean.BackgroundExecutor;

public abstract interface SpiBackgroundExecutor
  extends BackgroundExecutor
{
  public abstract void shutdown();
}
