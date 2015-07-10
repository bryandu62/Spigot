package com.avaje.ebeaninternal.server.lib;

import com.avaje.ebeaninternal.api.Monitor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DaemonScheduleThreadPool
  extends ScheduledThreadPoolExecutor
{
  private static final Logger logger = Logger.getLogger(DaemonScheduleThreadPool.class.getName());
  private final Monitor monitor = new Monitor();
  private int shutdownWaitSeconds;
  
  public DaemonScheduleThreadPool(int coreSize, int shutdownWaitSeconds, String namePrefix)
  {
    super(coreSize, new DaemonThreadFactory(namePrefix));
    this.shutdownWaitSeconds = shutdownWaitSeconds;
    
    Runtime.getRuntime().addShutdownHook(new ShutdownHook(null));
  }
  
  public void shutdown()
  {
    synchronized (this.monitor)
    {
      if (super.isShutdown())
      {
        logger.fine("... DaemonScheduleThreadPool already shut down");
        return;
      }
      try
      {
        logger.fine("DaemonScheduleThreadPool shutting down...");
        super.shutdown();
        if (!super.awaitTermination(this.shutdownWaitSeconds, TimeUnit.SECONDS))
        {
          logger.info("ScheduleService shut down timeout exceeded. Terminating running threads.");
          super.shutdownNow();
        }
      }
      catch (Exception e)
      {
        String msg = "Error during shutdown";
        logger.log(Level.SEVERE, msg, e);
        e.printStackTrace();
      }
    }
  }
  
  private class ShutdownHook
    extends Thread
  {
    private ShutdownHook() {}
    
    public void run()
    {
      DaemonScheduleThreadPool.this.shutdown();
    }
  }
}
