package com.avaje.ebean.cache;

public class ServerCacheStatistics
{
  protected String cacheName;
  protected int maxSize;
  protected int size;
  protected int hitCount;
  protected int missCount;
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(this.cacheName);
    sb.append(" size:").append(this.size);
    sb.append(" hitRatio:").append(getHitRatio());
    sb.append(" hitCount:").append(this.hitCount);
    sb.append(" missCount:").append(this.missCount);
    sb.append(" maxSize:").append(this.maxSize);
    return sb.toString();
  }
  
  public String getCacheName()
  {
    return this.cacheName;
  }
  
  public void setCacheName(String cacheName)
  {
    this.cacheName = cacheName;
  }
  
  public int getHitCount()
  {
    return this.hitCount;
  }
  
  public void setHitCount(int hitCount)
  {
    this.hitCount = hitCount;
  }
  
  public int getMissCount()
  {
    return this.missCount;
  }
  
  public void setMissCount(int missCount)
  {
    this.missCount = missCount;
  }
  
  public int getSize()
  {
    return this.size;
  }
  
  public void setSize(int size)
  {
    this.size = size;
  }
  
  public int getMaxSize()
  {
    return this.maxSize;
  }
  
  public void setMaxSize(int maxSize)
  {
    this.maxSize = maxSize;
  }
  
  public int getHitRatio()
  {
    int totalCount = this.hitCount + this.missCount;
    if (totalCount == 0) {
      return 0;
    }
    return this.hitCount * 100 / totalCount;
  }
}
