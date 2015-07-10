package com.avaje.ebeaninternal.server.lib.thread;

import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.server.lib.BackgroundThread;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadPoolManager
  implements Runnable
{
  private static final class Single
  {
    private static final ThreadPoolManager me = new ThreadPoolManager(null);
  }
  
  private static int debugLevel = 0;
  private boolean isShuttingDown = false;
  private ConcurrentHashMap<String, ThreadPool> threadPoolCache = new ConcurrentHashMap();
  private long defaultIdleTime;
  
  private ThreadPoolManager()
  {
    initialise();
  }
  
  private void initialise()
  {
    debugLevel = GlobalProperties.getInt("threadpool.debugLevel", 0);
    
    this.defaultIdleTime = (1000 * GlobalProperties.getInt("threadpool.idletime", 60));
    
    int freqIsSecs = GlobalProperties.getInt("threadpool.sleeptime", 30);
    
    BackgroundThread.add(freqIsSecs, this);
  }
  
  public static void setDebugLevel(int level)
  {
    debugLevel = level;
  }
  
  public static int getDebugLevel()
  {
    return debugLevel;
  }
  
  public void run()
  {
    if (!this.isShuttingDown) {
      maintainPoolSize();
    }
  }
  
  public static ThreadPool getThreadPool(String poolName)
  {
    return Single.me.getPool(poolName);
  }
  
  private ThreadPool getPool(String poolName)
  {
    synchronized (this)
    {
      ThreadPool threadPool = (ThreadPool)this.threadPoolCache.get(poolName);
      if (threadPool == null)
      {
        threadPool = createThreadPool(poolName);
        this.threadPoolCache.put(poolName, threadPool);
      }
      return threadPool;
    }
  }
  
  public static Iterator<ThreadPool> pools()
  {
    return Single.me.threadPoolCache.values().iterator();
  }
  
  private void maintainPoolSize()
  {
    if (this.isShuttingDown) {
      return;
    }
    synchronized (this)
    {
      Iterator<ThreadPool> e = pools();
      while (e.hasNext())
      {
        ThreadPool pool = (ThreadPool)e.next();
        pool.maintainPoolSize();
      }
    }
  }
  
  public static void shutdown()
  {
    Single.me.shutdownPools();
  }
  
  private void shutdownPools()
  {
    synchronized (this)
    {
      this.isShuttingDown = true;
      Iterator<ThreadPool> i = pools();
      while (i.hasNext())
      {
        ThreadPool pool = (ThreadPool)i.next();
        pool.shutdown();
      }
    }
  }
  
  private ThreadPool createThreadPool(String poolName)
  {
    int min = GlobalProperties.getInt("threadpool." + poolName + ".min", 0);
    int max = GlobalProperties.getInt("threadpool." + poolName + ".max", 100);
    
    long idle = 1000 * GlobalProperties.getInt("threadpool." + poolName + ".idletime", -1);
    if (idle < 0L) {
      idle = this.defaultIdleTime;
    }
    boolean isDaemon = true;
    Integer priority = null;
    String threadPriority = GlobalProperties.get("threadpool." + poolName + ".priority", null);
    if (threadPriority != null) {
      priority = new Integer(threadPriority);
    }
    ThreadPool newPool = new ThreadPool(poolName, isDaemon, priority);
    newPool.setMaxSize(max);
    newPool.setMinSize(min);
    newPool.setMaxIdleTime(idle);
    
    return newPool;
  }
}
