package com.avaje.ebeaninternal.server.lib;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BackgroundThread
{
  private static final Logger logger = Logger.getLogger(BackgroundThread.class.getName());
  private static final BackgroundThread me = new BackgroundThread();
  private Vector<BackgroundRunnable> list = new Vector();
  private final Object monitor = new Object();
  private final Thread thread;
  private long sleepTime = 1000L;
  private long count;
  private long exeTime;
  private boolean stopped;
  private Object threadMonitor = new Object();
  
  private BackgroundThread()
  {
    this.thread = new Thread(new Runner(null), "EbeanBackgroundThread");
    this.thread.setDaemon(true);
    this.thread.start();
  }
  
  public static void add(int freqInSecs, Runnable runnable)
  {
    add(new BackgroundRunnable(runnable, freqInSecs));
  }
  
  public static void add(BackgroundRunnable backgroundRunnable)
  {
    me.addTask(backgroundRunnable);
  }
  
  public static void shutdown()
  {
    me.stop();
  }
  
  /* Error */
  public static Iterator<BackgroundRunnable> runnables()
  {
    // Byte code:
    //   0: getstatic 81	com/avaje/ebeaninternal/server/lib/BackgroundThread:me	Lcom/avaje/ebeaninternal/server/lib/BackgroundThread;
    //   3: getfield 39	com/avaje/ebeaninternal/server/lib/BackgroundThread:monitor	Ljava/lang/Object;
    //   6: dup
    //   7: astore_0
    //   8: monitorenter
    //   9: getstatic 81	com/avaje/ebeaninternal/server/lib/BackgroundThread:me	Lcom/avaje/ebeaninternal/server/lib/BackgroundThread;
    //   12: getfield 37	com/avaje/ebeaninternal/server/lib/BackgroundThread:list	Ljava/util/Vector;
    //   15: invokevirtual 95	java/util/Vector:iterator	()Ljava/util/Iterator;
    //   18: aload_0
    //   19: monitorexit
    //   20: areturn
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    // Line number table:
    //   Java source line #122	-> byte code offset #0
    //   Java source line #123	-> byte code offset #9
    //   Java source line #124	-> byte code offset #21
    // Local variable table:
    //   start	length	slot	name	signature
    //   7	16	0	Ljava/lang/Object;	Object
    //   21	4	1	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   9	20	21	finally
    //   21	24	21	finally
  }
  
  private void addTask(BackgroundRunnable backgroundRunnable)
  {
    synchronized (this.monitor)
    {
      this.list.add(backgroundRunnable);
    }
  }
  
  private void stop()
  {
    this.stopped = true;
    synchronized (this.threadMonitor)
    {
      try
      {
        this.threadMonitor.wait(10000L);
      }
      catch (InterruptedException e) {}
    }
  }
  
  private class Runner
    implements Runnable
  {
    private Runner() {}
    
    public void run()
    {
      if (ShutdownManager.isStopping()) {
        return;
      }
      while (!BackgroundThread.this.stopped) {
        try
        {
          long actualSleep = BackgroundThread.this.sleepTime - BackgroundThread.this.exeTime;
          if (actualSleep < 0L) {
            actualSleep = BackgroundThread.this.sleepTime;
          }
          Thread.sleep(actualSleep);
          synchronized (BackgroundThread.this.monitor)
          {
            runJobs();
          }
        }
        catch (InterruptedException e)
        {
          BackgroundThread.logger.log(Level.SEVERE, null, e);
        }
      }
      synchronized (BackgroundThread.this.threadMonitor)
      {
        BackgroundThread.this.threadMonitor.notifyAll();
      }
    }
    
    private void runJobs()
    {
      long startTime = System.currentTimeMillis();
      
      Iterator<BackgroundRunnable> it = BackgroundThread.this.list.iterator();
      while (it.hasNext())
      {
        BackgroundRunnable bgr = (BackgroundRunnable)it.next();
        if (bgr.isActive())
        {
          int freqInSecs = bgr.getFreqInSecs();
          if (BackgroundThread.this.count % freqInSecs == 0L)
          {
            Runnable runable = bgr.getRunnable();
            if (bgr.runNow(startTime))
            {
              bgr.runStart();
              if (BackgroundThread.logger.isLoggable(Level.FINER))
              {
                String msg = BackgroundThread.this.count + " BGRunnable running [" + runable.getClass().getName() + "]";
                
                BackgroundThread.logger.finer(msg);
              }
              runable.run();
              bgr.runEnd();
            }
          }
        }
      }
      BackgroundThread.this.exeTime = (System.currentTimeMillis() - startTime);
      BackgroundThread.access$808(BackgroundThread.this);
      if (BackgroundThread.this.count == 86400L) {
        BackgroundThread.this.count = 0L;
      }
    }
  }
  
  public String toString()
  {
    synchronized (this.monitor)
    {
      StringBuffer sb = new StringBuffer();
      
      Iterator<BackgroundRunnable> it = runnables();
      while (it.hasNext())
      {
        BackgroundRunnable bgr = (BackgroundRunnable)it.next();
        sb.append(bgr);
      }
      return sb.toString();
    }
  }
}
