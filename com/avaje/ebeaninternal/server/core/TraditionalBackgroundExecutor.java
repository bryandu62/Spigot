package com.avaje.ebeaninternal.server.core;

import com.avaje.ebeaninternal.api.SpiBackgroundExecutor;
import com.avaje.ebeaninternal.server.lib.DaemonScheduleThreadPool;
import com.avaje.ebeaninternal.server.lib.thread.ThreadPool;
import java.util.concurrent.TimeUnit;

public class TraditionalBackgroundExecutor
  implements SpiBackgroundExecutor
{
  private final ThreadPool pool;
  private final DaemonScheduleThreadPool schedulePool;
  
  public TraditionalBackgroundExecutor(ThreadPool pool, int schedulePoolSize, int shutdownWaitSeconds, String namePrefix)
  {
    this.pool = pool;
    this.schedulePool = new DaemonScheduleThreadPool(schedulePoolSize, shutdownWaitSeconds, namePrefix + "-periodic-");
  }
  
  public void execute(Runnable r)
  {
    this.pool.assign(r, true);
  }
  
  public void executePeriodically(Runnable r, long delay, TimeUnit unit)
  {
    this.schedulePool.scheduleWithFixedDelay(r, delay, delay, unit);
  }
  
  public void shutdown()
  {
    this.schedulePool.shutdown();
  }
}
