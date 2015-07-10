package com.avaje.ebeaninternal.server.core;

public class CacheOptions
{
  private boolean useCache;
  private boolean readOnly;
  private String naturalKey;
  private String warmingQuery;
  
  public boolean isUseCache()
  {
    return this.useCache;
  }
  
  public void setUseCache(boolean useCache)
  {
    this.useCache = useCache;
  }
  
  public boolean isReadOnly()
  {
    return this.readOnly;
  }
  
  public void setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
  }
  
  public String getWarmingQuery()
  {
    return this.warmingQuery;
  }
  
  public void setWarmingQuery(String warmingQuery)
  {
    this.warmingQuery = warmingQuery;
  }
  
  public boolean isUseNaturalKeyCache()
  {
    return this.naturalKey != null;
  }
  
  public String getNaturalKey()
  {
    return this.naturalKey;
  }
  
  public void setNaturalKey(String naturalKey)
  {
    if ((naturalKey == null) || (naturalKey.length() == 0)) {
      naturalKey = null;
    } else {
      this.naturalKey = naturalKey.trim();
    }
  }
}
