package com.avaje.ebeaninternal.server.lib;

import com.avaje.ebeaninternal.api.Monitor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DaemonThreadPool
  extends ThreadPoolExecutor
{
  private static final Logger logger = Logger.getLogger(DaemonThreadPool.class.getName());
  private final Monitor monitor = new Monitor();
  private final String namePrefix;
  private int shutdownWaitSeconds;
  
  public DaemonThreadPool(int coreSize, long keepAliveSecs, int shutdownWaitSeconds, String namePrefix)
  {
    super(coreSize, coreSize, keepAliveSecs, TimeUnit.SECONDS, new LinkedBlockingQueue(), new DaemonThreadFactory(namePrefix));
    this.shutdownWaitSeconds = shutdownWaitSeconds;
    this.namePrefix = namePrefix;
    
    Runtime.getRuntime().addShutdownHook(new ShutdownHook(null));
  }
  
  public void shutdown()
  {
    synchronized (this.monitor)
    {
      if (super.isShutdown())
      {
        logger.fine("... DaemonThreadPool[" + this.namePrefix + "] already shut down");
        return;
      }
      try
      {
        logger.fine("DaemonThreadPool[" + this.namePrefix + "] shutting down...");
        super.shutdown();
        if (!super.awaitTermination(this.shutdownWaitSeconds, TimeUnit.SECONDS))
        {
          logger.info("DaemonThreadPool[" + this.namePrefix + "] shut down timeout exceeded. Terminating running threads.");
          super.shutdownNow();
        }
      }
      catch (Exception e)
      {
        String msg = "Error during shutdown of DaemonThreadPool[" + this.namePrefix + "]";
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
      DaemonThreadPool.this.shutdown();
    }
  }
}
