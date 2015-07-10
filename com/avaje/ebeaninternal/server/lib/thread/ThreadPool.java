package com.avaje.ebeaninternal.server.lib.thread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadPool
{
  private static final Logger logger = Logger.getLogger(ThreadPool.class.getName());
  private long maxIdleTime;
  private String poolName;
  private int minSize;
  private boolean isDaemon;
  private boolean isStopping = false;
  private Integer threadPriority;
  private int uniqueThreadID;
  private Vector<PooledThread> freeList = new Vector();
  private Vector<PooledThread> busyList = new Vector();
  private Vector<Work> workOverflowQueue = new Vector();
  private int maxSize = 100;
  private boolean stopThePool;
  
  public ThreadPool(String poolName, boolean isDaemon, Integer threadPriority)
  {
    this.poolName = poolName;
    this.stopThePool = false;
    this.isDaemon = isDaemon;
    this.threadPriority = threadPriority;
  }
  
  public boolean isStopping()
  {
    return this.isStopping;
  }
  
  public String getName()
  {
    return this.poolName;
  }
  
  public void setMinSize(int minSize)
  {
    if (minSize > 0)
    {
      if (minSize > this.maxSize) {
        this.maxSize = minSize;
      }
      this.minSize = minSize;
      maintainPoolSize();
    }
  }
  
  public int getMinSize()
  {
    return this.minSize;
  }
  
  public void setMaxSize(int maxSize)
  {
    if (maxSize > 0)
    {
      if (this.minSize > maxSize) {
        this.minSize = maxSize;
      }
      this.maxSize = maxSize;
      maintainPoolSize();
    }
  }
  
  public int getMaxSize()
  {
    return this.maxSize;
  }
  
  public int size()
  {
    return this.busyList.size() + this.freeList.size();
  }
  
  public int getBusyCount()
  {
    return this.busyList.size();
  }
  
  public boolean assign(Runnable work, boolean addToQueueIfFull)
  {
    if (this.stopThePool) {
      throw new RuntimeException("Pool is stopping... no more work please.");
    }
    Work runWork = new Work(work);
    
    PooledThread thread = getNextAvailableThread();
    if (thread != null)
    {
      this.busyList.add(thread);
      thread.assignWork(runWork);
      return true;
    }
    if (addToQueueIfFull)
    {
      runWork.setEnterQueueTime(System.currentTimeMillis());
      this.workOverflowQueue.add(runWork);
    }
    return false;
  }
  
  protected void removeThread(PooledThread thread)
  {
    synchronized (this.freeList)
    {
      this.busyList.remove(thread);
      this.freeList.remove(thread);
      this.freeList.notify();
    }
  }
  
  protected void returnThread(PooledThread thread)
  {
    synchronized (this.freeList)
    {
      this.busyList.remove(thread);
      if (!this.workOverflowQueue.isEmpty())
      {
        Work queuedWork = (Work)this.workOverflowQueue.remove(0);
        
        queuedWork.setExitQueueTime(System.currentTimeMillis());
        this.busyList.add(thread);
        thread.assignWork(queuedWork);
      }
      else
      {
        this.freeList.add(thread);
        
        this.freeList.notify();
      }
    }
  }
  
  private PooledThread getNextAvailableThread()
  {
    synchronized (this.freeList)
    {
      if (!this.freeList.isEmpty()) {
        return (PooledThread)this.freeList.remove(0);
      }
      if (size() < this.maxSize) {
        return growPool(true);
      }
      return null;
    }
  }
  
  /* Error */
  public Iterator<PooledThread> getBusyThreads()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 39	com/avaje/ebeaninternal/server/lib/thread/ThreadPool:freeList	Ljava/util/Vector;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 41	com/avaje/ebeaninternal/server/lib/thread/ThreadPool:busyList	Ljava/util/Vector;
    //   11: invokevirtual 147	java/util/Vector:iterator	()Ljava/util/Iterator;
    //   14: aload_1
    //   15: monitorexit
    //   16: areturn
    //   17: astore_2
    //   18: aload_1
    //   19: monitorexit
    //   20: aload_2
    //   21: athrow
    // Line number table:
    //   Java source line #293	-> byte code offset #0
    //   Java source line #294	-> byte code offset #7
    //   Java source line #295	-> byte code offset #17
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	22	0	this	ThreadPool
    //   5	14	1	Ljava/lang/Object;	Object
    //   17	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	16	17	finally
    //   17	20	17	finally
  }
  
  protected void shutdown()
  {
    synchronized (this.freeList)
    {
      this.isStopping = true;
      
      int size = size();
      if (size > 0)
      {
        String msg = null;
        msg = "ThreadPool [" + this.poolName + "] Shutting down; threadCount[" + size() + "] busyCount[" + getBusyCount() + "]";
        
        logger.info(msg);
      }
      this.stopThePool = true;
      while (!this.freeList.isEmpty())
      {
        PooledThread thread = (PooledThread)this.freeList.remove(0);
        thread.stop();
      }
      try
      {
        while (getBusyCount() > 0)
        {
          String msg = "ThreadPool [" + this.poolName + "] has [" + getBusyCount() + "] busy threads, waiting for those to finish.";
          
          logger.info(msg);
          
          Iterator<PooledThread> busyThreads = getBusyThreads();
          while (busyThreads.hasNext())
          {
            PooledThread busyThread = (PooledThread)busyThreads.next();
            
            String threadName = busyThread.getName();
            Work work = busyThread.getWork();
            
            String busymsg = "Busy thread [" + threadName + "] work[" + work + "]";
            logger.info(busymsg);
          }
          this.freeList.wait();
          PooledThread thread = (PooledThread)this.freeList.remove(0);
          if (thread != null) {
            thread.stop();
          }
        }
      }
      catch (InterruptedException e)
      {
        logger.log(Level.SEVERE, null, e);
      }
    }
  }
  
  protected void maintainPoolSize()
  {
    synchronized (this.freeList)
    {
      if (this.isStopping) {
        return;
      }
      int numToStop = size() - this.minSize;
      if (numToStop > 0)
      {
        long usedAfter = System.currentTimeMillis() - this.maxIdleTime;
        ArrayList<PooledThread> stopList = new ArrayList();
        Iterator<PooledThread> it = this.freeList.iterator();
        while ((it.hasNext()) && (numToStop > 0))
        {
          PooledThread thread = (PooledThread)it.next();
          if (thread.getLastUsedTime() < usedAfter)
          {
            stopList.add(thread);
            numToStop--;
          }
        }
        Iterator<PooledThread> stopIt = stopList.iterator();
        while (stopIt.hasNext())
        {
          PooledThread thread = (PooledThread)stopIt.next();
          thread.stop();
        }
      }
      int numToAdd = this.minSize - size();
      if (numToAdd > 0) {
        for (int i = 0; i < numToAdd; i++) {
          growPool(false);
        }
      }
    }
  }
  
  public PooledThread interrupt(String threadName)
  {
    PooledThread thread = getBusyThread(threadName);
    if (thread != null)
    {
      thread.interrupt();
      return thread;
    }
    return null;
  }
  
  public PooledThread getBusyThread(String threadName)
  {
    synchronized (this.freeList)
    {
      Iterator<PooledThread> it = getBusyThreads();
      while (it.hasNext())
      {
        PooledThread pt = (PooledThread)it.next();
        if (pt.getName().equals(threadName)) {
          return pt;
        }
      }
      return null;
    }
  }
  
  private PooledThread growPool(boolean andReturn)
  {
    synchronized (this.freeList)
    {
      String threadName = this.poolName + "." + this.uniqueThreadID++;
      PooledThread bgw = new PooledThread(this, threadName, this.isDaemon, this.threadPriority);
      bgw.start();
      if (logger.isLoggable(Level.FINE)) {
        logger.fine("ThreadPool grow created [" + threadName + "] size[" + size() + "]");
      }
      if (andReturn) {
        return bgw;
      }
      this.freeList.add(bgw);
      return null;
    }
  }
  
  public long getMaxIdleTime()
  {
    return this.maxIdleTime;
  }
  
  public void setMaxIdleTime(long maxIdleTime)
  {
    this.maxIdleTime = maxIdleTime;
  }
}
