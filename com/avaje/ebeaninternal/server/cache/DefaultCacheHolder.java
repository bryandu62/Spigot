package com.avaje.ebeaninternal.server.cache;

import com.avaje.ebean.annotation.CacheTuning;
import com.avaje.ebean.cache.ServerCache;
import com.avaje.ebean.cache.ServerCacheFactory;
import com.avaje.ebean.cache.ServerCacheOptions;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultCacheHolder
{
  private final ConcurrentHashMap<String, ServerCache> concMap = new ConcurrentHashMap();
  private final HashMap<String, ServerCache> synchMap = new HashMap();
  private final Object monitor = new Object();
  private final ServerCacheFactory cacheFactory;
  private final ServerCacheOptions defaultOptions;
  private final boolean useBeanTuning;
  
  public DefaultCacheHolder(ServerCacheFactory cacheFactory, ServerCacheOptions defaultOptions, boolean useBeanTuning)
  {
    this.cacheFactory = cacheFactory;
    this.defaultOptions = defaultOptions;
    this.useBeanTuning = useBeanTuning;
  }
  
  public ServerCacheOptions getDefaultOptions()
  {
    return this.defaultOptions;
  }
  
  public ServerCache getCache(String cacheKey)
  {
    ServerCache cache = (ServerCache)this.concMap.get(cacheKey);
    if (cache != null) {
      return cache;
    }
    synchronized (this.monitor)
    {
      cache = (ServerCache)this.synchMap.get(cacheKey);
      if (cache == null)
      {
        ServerCacheOptions options = getCacheOptions(cacheKey);
        cache = this.cacheFactory.createCache(cacheKey, options);
        this.synchMap.put(cacheKey, cache);
        this.concMap.put(cacheKey, cache);
      }
      return cache;
    }
  }
  
  public void clearCache(String cacheKey)
  {
    ServerCache cache = (ServerCache)this.concMap.get(cacheKey);
    if (cache != null) {
      cache.clear();
    }
  }
  
  public boolean isCaching(String beanType)
  {
    return this.concMap.containsKey(beanType);
  }
  
  public void clearAll()
  {
    Iterator<ServerCache> it = this.concMap.values().iterator();
    while (it.hasNext())
    {
      ServerCache serverCache = (ServerCache)it.next();
      serverCache.clear();
    }
  }
  
  private ServerCacheOptions getCacheOptions(String beanType)
  {
    if (this.useBeanTuning) {
      try
      {
        Class<?> cls = Class.forName(beanType);
        CacheTuning cacheTuning = (CacheTuning)cls.getAnnotation(CacheTuning.class);
        if (cacheTuning != null)
        {
          ServerCacheOptions o = new ServerCacheOptions(cacheTuning);
          o.applyDefaults(this.defaultOptions);
          return o;
        }
      }
      catch (ClassNotFoundException e) {}
    }
    return this.defaultOptions.copy();
  }
}
