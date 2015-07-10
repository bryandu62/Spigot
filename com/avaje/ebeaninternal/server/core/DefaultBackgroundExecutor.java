package com.avaje.ebeaninternal.server.core;

import com.avaje.ebeaninternal.api.SpiBackgroundExecutor;
import com.avaje.ebeaninternal.server.lib.DaemonScheduleThreadPool;
import com.avaje.ebeaninternal.server.lib.DaemonThreadPool;
import java.util.concurrent.TimeUnit;

public class DefaultBackgroundExecutor
  implements SpiBackgroundExecutor
{
  private final DaemonThreadPool pool;
  private final DaemonScheduleThreadPool schedulePool;
  
  public DefaultBackgroundExecutor(int mainPoolSize, int schedulePoolSize, long keepAliveSecs, int shutdownWaitSeconds, String namePrefix)
  {
    this.pool = new DaemonThreadPool(mainPoolSize, keepAliveSecs, shutdownWaitSeconds, namePrefix);
    this.schedulePool = new DaemonScheduleThreadPool(schedulePoolSize, shutdownWaitSeconds, namePrefix + "-periodic-");
  }
  
  public void execute(Runnable r)
  {
    this.pool.execute(r);
  }
  
  public void executePeriodically(Runnable r, long delay, TimeUnit unit)
  {
    this.schedulePool.scheduleWithFixedDelay(r, delay, delay, unit);
  }
  
  public void shutdown()
  {
    this.pool.shutdown();
    this.schedulePool.shutdown();
  }
}
