package com.avaje.ebeaninternal.server.lib;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DaemonThreadFactory
  implements ThreadFactory
{
  private static final AtomicInteger poolNumber = new AtomicInteger(1);
  private final ThreadGroup group;
  private final AtomicInteger threadNumber = new AtomicInteger(1);
  private final String namePrefix;
  
  public DaemonThreadFactory(String namePrefix)
  {
    SecurityManager s = System.getSecurityManager();
    this.group = (s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup());
    this.namePrefix = ("pool-" + poolNumber.getAndIncrement() + "-thread-");
  }
  
  public Thread newThread(Runnable r)
  {
    Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
    
    t.setDaemon(true);
    if (t.getPriority() != 5) {
      t.setPriority(5);
    }
    return t;
  }
}
