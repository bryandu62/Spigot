package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractManager
{
  protected static final Logger LOGGER = ;
  private static final Map<String, AbstractManager> MAP = new HashMap();
  private static final Lock LOCK = new ReentrantLock();
  protected int count;
  private final String name;
  
  protected AbstractManager(String name)
  {
    this.name = name;
    LOGGER.debug("Starting {} {}", new Object[] { getClass().getSimpleName(), name });
  }
  
  public static <M extends AbstractManager, T> M getManager(String name, ManagerFactory<M, T> factory, T data)
  {
    LOCK.lock();
    try
    {
      M manager = (AbstractManager)MAP.get(name);
      if (manager == null)
      {
        manager = (AbstractManager)factory.createManager(name, data);
        if (manager == null) {
          throw new IllegalStateException("Unable to create a manager");
        }
        MAP.put(name, manager);
      }
      manager.count += 1;
      return manager;
    }
    finally
    {
      LOCK.unlock();
    }
  }
  
  public static boolean hasManager(String name)
  {
    LOCK.lock();
    try
    {
      return MAP.containsKey(name);
    }
    finally
    {
      LOCK.unlock();
    }
  }
  
  protected void releaseSub() {}
  
  protected int getCount()
  {
    return this.count;
  }
  
  public void release()
  {
    LOCK.lock();
    try
    {
      this.count -= 1;
      if (this.count <= 0)
      {
        MAP.remove(this.name);
        LOGGER.debug("Shutting down {} {}", new Object[] { getClass().getSimpleName(), getName() });
        releaseSub();
      }
    }
    finally
    {
      LOCK.unlock();
    }
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public Map<String, String> getContentFormat()
  {
    return new HashMap();
  }
}
