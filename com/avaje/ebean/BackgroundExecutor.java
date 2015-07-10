package com.avaje.ebean;

import java.util.concurrent.TimeUnit;

public abstract interface BackgroundExecutor
{
  public abstract void execute(Runnable paramRunnable);
  
  public abstract void executePeriodically(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit);
}
