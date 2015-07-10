package com.avaje.ebean.cache;

import com.avaje.ebean.EbeanServer;

public abstract interface ServerCacheFactory
{
  public abstract void init(EbeanServer paramEbeanServer);
  
  public abstract ServerCache createCache(String paramString, ServerCacheOptions paramServerCacheOptions);
}
