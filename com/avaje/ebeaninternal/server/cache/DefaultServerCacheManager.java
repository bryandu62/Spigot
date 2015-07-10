package com.avaje.ebeaninternal.server.cache;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.cache.ServerCache;
import com.avaje.ebean.cache.ServerCacheFactory;
import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.ebean.cache.ServerCacheOptions;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.core.CacheOptions;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;

public class DefaultServerCacheManager
  implements ServerCacheManager
{
  private final DefaultCacheHolder beanCache;
  private final DefaultCacheHolder queryCache;
  private final DefaultCacheHolder naturalKeyCache;
  private final DefaultCacheHolder collectionIdsCache;
  private final ServerCacheFactory cacheFactory;
  private SpiEbeanServer ebeanServer;
  
  public DefaultServerCacheManager(ServerCacheFactory cacheFactory, ServerCacheOptions defaultBeanOptions, ServerCacheOptions defaultQueryOptions)
  {
    this.cacheFactory = cacheFactory;
    this.beanCache = new DefaultCacheHolder(cacheFactory, defaultBeanOptions, true);
    this.queryCache = new DefaultCacheHolder(cacheFactory, defaultQueryOptions, false);
    this.naturalKeyCache = new DefaultCacheHolder(cacheFactory, defaultQueryOptions, false);
    this.collectionIdsCache = new DefaultCacheHolder(cacheFactory, defaultQueryOptions, false);
  }
  
  public void init(EbeanServer server)
  {
    this.cacheFactory.init(server);
    this.ebeanServer = ((SpiEbeanServer)server);
  }
  
  public void setCaching(Class<?> beanType, boolean useCache)
  {
    this.ebeanServer.getBeanDescriptor(beanType).getCacheOptions().setUseCache(useCache);
  }
  
  public void clear(Class<?> beanType)
  {
    String beanName = beanType.getName();
    this.beanCache.clearCache(beanName);
    this.naturalKeyCache.clearCache(beanName);
    this.collectionIdsCache.clearCache(beanName);
    this.queryCache.clearCache(beanName);
  }
  
  public void clearAll()
  {
    this.beanCache.clearAll();
    this.queryCache.clearAll();
    this.naturalKeyCache.clearAll();
    this.collectionIdsCache.clearAll();
  }
  
  public ServerCache getCollectionIdsCache(Class<?> beanType, String propertyName)
  {
    return this.collectionIdsCache.getCache(beanType.getName() + "." + propertyName);
  }
  
  public boolean isCollectionIdsCaching(Class<?> beanType)
  {
    return this.collectionIdsCache.isCaching(beanType.getName());
  }
  
  public ServerCache getNaturalKeyCache(Class<?> beanType)
  {
    return this.naturalKeyCache.getCache(beanType.getName());
  }
  
  public boolean isNaturalKeyCaching(Class<?> beanType)
  {
    return this.naturalKeyCache.isCaching(beanType.getName());
  }
  
  public ServerCache getQueryCache(Class<?> beanType)
  {
    return this.queryCache.getCache(beanType.getName());
  }
  
  public ServerCache getBeanCache(Class<?> beanType)
  {
    return this.beanCache.getCache(beanType.getName());
  }
  
  public boolean isBeanCaching(Class<?> beanType)
  {
    return this.beanCache.isCaching(beanType.getName());
  }
  
  public boolean isQueryCaching(Class<?> beanType)
  {
    return this.queryCache.isCaching(beanType.getName());
  }
}
