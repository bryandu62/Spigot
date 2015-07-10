package com.avaje.ebeaninternal.server.lib.sql;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PstmtCache
  extends LinkedHashMap<String, ExtendedPreparedStatement>
{
  private static final Logger logger = Logger.getLogger(PstmtCache.class.getName());
  static final long serialVersionUID = -3096406924865550697L;
  final String cacheName;
  final int maxSize;
  int removeCounter;
  int hitCounter;
  int missCounter;
  int putCounter;
  
  public PstmtCache(String cacheName, int maxCacheSize)
  {
    super(maxCacheSize * 3, 0.75F, true);
    this.cacheName = cacheName;
    this.maxSize = maxCacheSize;
  }
  
  public String getDescription()
  {
    return this.cacheName + " size:" + size() + " max:" + this.maxSize + " totalHits:" + this.hitCounter + " hitRatio:" + getHitRatio() + " removes:" + this.removeCounter;
  }
  
  public int getMaxSize()
  {
    return this.maxSize;
  }
  
  public int getHitRatio()
  {
    if (this.hitCounter == 0) {
      return 0;
    }
    return this.hitCounter * 100 / (this.hitCounter + this.missCounter);
  }
  
  public int getHitCounter()
  {
    return this.hitCounter;
  }
  
  public int getMissCounter()
  {
    return this.missCounter;
  }
  
  public int getPutCounter()
  {
    return this.putCounter;
  }
  
  public ExtendedPreparedStatement get(Object key)
  {
    ExtendedPreparedStatement o = (ExtendedPreparedStatement)super.get(key);
    if (o == null) {
      this.missCounter += 1;
    } else {
      this.hitCounter += 1;
    }
    return o;
  }
  
  public ExtendedPreparedStatement remove(Object key)
  {
    ExtendedPreparedStatement o = (ExtendedPreparedStatement)super.remove(key);
    if (o == null) {
      this.missCounter += 1;
    } else {
      this.hitCounter += 1;
    }
    return o;
  }
  
  public ExtendedPreparedStatement put(String key, ExtendedPreparedStatement value)
  {
    this.putCounter += 1;
    return (ExtendedPreparedStatement)super.put(key, value);
  }
  
  protected boolean removeEldestEntry(Map.Entry<String, ExtendedPreparedStatement> eldest)
  {
    if (size() < this.maxSize) {
      return false;
    }
    this.removeCounter += 1;
    try
    {
      ExtendedPreparedStatement pstmt = (ExtendedPreparedStatement)eldest.getValue();
      pstmt.closeDestroy();
    }
    catch (SQLException e)
    {
      logger.log(Level.SEVERE, "Error closing ExtendedPreparedStatement", e);
    }
    return true;
  }
}
