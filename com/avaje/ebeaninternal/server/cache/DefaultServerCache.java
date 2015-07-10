package com.avaje.ebeaninternal.server.cache;

import com.avaje.ebean.BackgroundExecutor;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.cache.ServerCache;
import com.avaje.ebean.cache.ServerCacheOptions;
import com.avaje.ebean.cache.ServerCacheStatistics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultServerCache
  implements ServerCache
{
  private static final Logger logger = Logger.getLogger(DefaultServerCache.class.getName());
  private static final CacheEntryComparator comparator = new CacheEntryComparator(null);
  private final ConcurrentHashMap<Object, CacheEntry> map = new ConcurrentHashMap();
  private final AtomicInteger missCount = new AtomicInteger();
  private final AtomicInteger removedHitCount = new AtomicInteger();
  private final Object monitor = new Object();
  private final String name;
  private int maxSize;
  private long trimFrequency;
  private int maxIdleSecs;
  private int maxSecsToLive;
  
  public DefaultServerCache(String name, ServerCacheOptions options)
  {
    this(name, options.getMaxSize(), options.getMaxIdleSecs(), options.getMaxSecsToLive());
  }
  
  public DefaultServerCache(String name, int maxSize, int maxIdleSecs, int maxSecsToLive)
  {
    this.name = name;
    this.maxSize = maxSize;
    this.maxIdleSecs = maxIdleSecs;
    this.maxSecsToLive = maxSecsToLive;
    this.trimFrequency = 60L;
  }
  
  public void init(EbeanServer server)
  {
    TrimTask trim = new TrimTask(null);
    
    BackgroundExecutor executor = server.getBackgroundExecutor();
    executor.executePeriodically(trim, this.trimFrequency, TimeUnit.SECONDS);
  }
  
  public ServerCacheStatistics getStatistics(boolean reset)
  {
    ServerCacheStatistics s = new ServerCacheStatistics();
    s.setCacheName(this.name);
    s.setMaxSize(this.maxSize);
    
    int mc = reset ? this.missCount.getAndSet(0) : this.missCount.get();
    int hc = getHitCount(reset);
    int size = size();
    
    s.setSize(size);
    s.setHitCount(hc);
    s.setMissCount(mc);
    
    return s;
  }
  
  public int getHitRatio()
  {
    int mc = this.missCount.get();
    int hc = getHitCount(false);
    
    int totalCount = hc + mc;
    if (totalCount == 0) {
      return 0;
    }
    return hc * 100 / totalCount;
  }
  
  private int getHitCount(boolean reset)
  {
    int hc = reset ? this.removedHitCount.getAndSet(0) : this.removedHitCount.get();
    
    Iterator<CacheEntry> it = this.map.values().iterator();
    while (it.hasNext())
    {
      CacheEntry cacheEntry = (CacheEntry)it.next();
      hc += cacheEntry.getHitCount(reset);
    }
    return hc;
  }
  
  public ServerCacheOptions getOptions()
  {
    synchronized (this.monitor)
    {
      ServerCacheOptions o = new ServerCacheOptions();
      o.setMaxIdleSecs(this.maxIdleSecs);
      o.setMaxSize(this.maxSize);
      o.setMaxSecsToLive(this.maxSecsToLive);
      return o;
    }
  }
  
  public void setOptions(ServerCacheOptions o)
  {
    synchronized (this.monitor)
    {
      this.maxIdleSecs = o.getMaxIdleSecs();
      this.maxSize = o.getMaxSize();
      this.maxSecsToLive = o.getMaxSecsToLive();
    }
  }
  
  public int getMaxSize()
  {
    return this.maxSize;
  }
  
  public void setMaxSize(int maxSize)
  {
    synchronized (this.monitor)
    {
      this.maxSize = maxSize;
    }
  }
  
  public long getMaxIdleSecs()
  {
    return this.maxIdleSecs;
  }
  
  public void setMaxIdleSecs(int maxIdleSecs)
  {
    synchronized (this.monitor)
    {
      this.maxIdleSecs = maxIdleSecs;
    }
  }
  
  public long getMaxSecsToLive()
  {
    return this.maxSecsToLive;
  }
  
  public void setMaxSecsToLive(int maxSecsToLive)
  {
    synchronized (this.monitor)
    {
      this.maxSecsToLive = maxSecsToLive;
    }
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void clear()
  {
    this.map.clear();
  }
  
  public Object get(Object key)
  {
    CacheEntry entry = (CacheEntry)this.map.get(key);
    if (entry == null)
    {
      this.missCount.incrementAndGet();
      return null;
    }
    return entry.getValue();
  }
  
  public Object put(Object key, Object value)
  {
    CacheEntry entry = (CacheEntry)this.map.put(key, new CacheEntry(key, value));
    if (entry == null) {
      return null;
    }
    int removedHits = entry.getHitCount(true);
    this.removedHitCount.addAndGet(removedHits);
    return entry.getValue();
  }
  
  public Object putIfAbsent(Object key, Object value)
  {
    CacheEntry entry = (CacheEntry)this.map.putIfAbsent(key, new CacheEntry(key, value));
    if (entry == null) {
      return null;
    }
    return entry.getValue();
  }
  
  public Object remove(Object key)
  {
    CacheEntry entry = (CacheEntry)this.map.remove(key);
    if (entry == null) {
      return null;
    }
    int removedHits = entry.getHitCount(true);
    this.removedHitCount.addAndGet(removedHits);
    return entry.getValue();
  }
  
  public int size()
  {
    return this.map.size();
  }
  
  private Iterator<CacheEntry> cacheEntries()
  {
    return this.map.values().iterator();
  }
  
  private class TrimTask
    implements Runnable
  {
    private TrimTask() {}
    
    public void run()
    {
      long startTime = System.currentTimeMillis();
      if (DefaultServerCache.logger.isLoggable(Level.FINER)) {
        DefaultServerCache.logger.finer("trimming cache " + DefaultServerCache.this.name);
      }
      int trimmedByIdle = 0;
      int trimmedByTTL = 0;
      int trimmedByLRU = 0;
      
      boolean trimMaxSize = (DefaultServerCache.this.maxSize > 0) && (DefaultServerCache.this.maxSize < DefaultServerCache.this.size());
      
      ArrayList<DefaultServerCache.CacheEntry> activeList = new ArrayList();
      
      long idleExpire = System.currentTimeMillis() - DefaultServerCache.this.maxIdleSecs * 1000;
      long ttlExpire = System.currentTimeMillis() - DefaultServerCache.this.maxSecsToLive * 1000;
      
      Iterator<DefaultServerCache.CacheEntry> it = DefaultServerCache.this.cacheEntries();
      while (it.hasNext())
      {
        DefaultServerCache.CacheEntry cacheEntry = (DefaultServerCache.CacheEntry)it.next();
        if ((DefaultServerCache.this.maxIdleSecs > 0) && (idleExpire > cacheEntry.getLastAccessTime()))
        {
          it.remove();
          trimmedByIdle++;
        }
        else if ((DefaultServerCache.this.maxSecsToLive > 0) && (ttlExpire > cacheEntry.getCreateTime()))
        {
          it.remove();
          trimmedByTTL++;
        }
        else if (trimMaxSize)
        {
          activeList.add(cacheEntry);
        }
      }
      if (trimMaxSize)
      {
        trimmedByLRU = activeList.size() - DefaultServerCache.this.maxSize;
        if (trimmedByLRU > 0)
        {
          Collections.sort(activeList, DefaultServerCache.comparator);
          for (int i = DefaultServerCache.this.maxSize; i < activeList.size(); i++) {
            DefaultServerCache.this.map.remove(((DefaultServerCache.CacheEntry)activeList.get(i)).getKey());
          }
        }
      }
      long exeTime = System.currentTimeMillis() - startTime;
      if (DefaultServerCache.logger.isLoggable(Level.FINE)) {
        DefaultServerCache.logger.fine("Executed trim of cache " + DefaultServerCache.this.name + " in [" + exeTime + "]millis  idle[" + trimmedByIdle + "] timeToLive[" + trimmedByTTL + "] accessTime[" + trimmedByLRU + "]");
      }
    }
  }
  
  private static class CacheEntryComparator
    implements Comparator<DefaultServerCache.CacheEntry>, Serializable
  {
    private static final long serialVersionUID = 1L;
    
    public int compare(DefaultServerCache.CacheEntry o1, DefaultServerCache.CacheEntry o2)
    {
      return o1.getLastAccessLong().compareTo(o2.getLastAccessLong());
    }
  }
  
  public static class CacheEntry
  {
    private final Object key;
    private final Object value;
    private final long createTime;
    private final AtomicInteger hitCount = new AtomicInteger();
    private Long lastAccessTime;
    
    public CacheEntry(Object key, Object value)
    {
      this.key = key;
      this.value = value;
      this.createTime = System.currentTimeMillis();
      this.lastAccessTime = Long.valueOf(this.createTime);
    }
    
    public Object getKey()
    {
      return this.key;
    }
    
    public Object getValue()
    {
      this.hitCount.incrementAndGet();
      this.lastAccessTime = Long.valueOf(System.currentTimeMillis());
      return this.value;
    }
    
    public long getCreateTime()
    {
      return this.createTime;
    }
    
    public long getLastAccessTime()
    {
      return this.lastAccessTime.longValue();
    }
    
    public Long getLastAccessLong()
    {
      return this.lastAccessTime;
    }
    
    public int getHitCount(boolean reset)
    {
      if (reset) {
        return this.hitCount.getAndSet(0);
      }
      return this.hitCount.get();
    }
    
    public int getHitCount()
    {
      return this.hitCount.get();
    }
  }
}
