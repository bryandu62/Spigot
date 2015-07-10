package com.avaje.ebean.cache;

import com.avaje.ebean.EbeanServer;

public abstract interface ServerCacheManager
{
  public abstract void init(EbeanServer paramEbeanServer);
  
  public abstract void setCaching(Class<?> paramClass, boolean paramBoolean);
  
  public abstract boolean isBeanCaching(Class<?> paramClass);
  
  public abstract ServerCache getNaturalKeyCache(Class<?> paramClass);
  
  public abstract ServerCache getBeanCache(Class<?> paramClass);
  
  public abstract ServerCache getCollectionIdsCache(Class<?> paramClass, String paramString);
  
  public abstract ServerCache getQueryCache(Class<?> paramClass);
  
  public abstract void clear(Class<?> paramClass);
  
  public abstract void clearAll();
}
