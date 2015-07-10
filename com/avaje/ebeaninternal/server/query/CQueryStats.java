package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.meta.MetaQueryStatistic;

public final class CQueryStats
{
  private final int count;
  private final int totalLoadedBeanCount;
  private final int totalTimeMicros;
  private final long startCollecting;
  private final long lastQueryTime;
  
  public CQueryStats()
  {
    this.count = 0;
    this.totalLoadedBeanCount = 0;
    this.totalTimeMicros = 0;
    this.startCollecting = System.currentTimeMillis();
    this.lastQueryTime = 0L;
  }
  
  public CQueryStats(CQueryStats previous, int loadedBeanCount, int timeMicros)
  {
    previous.count += 1;
    previous.totalLoadedBeanCount += loadedBeanCount;
    previous.totalTimeMicros += timeMicros;
    this.startCollecting = previous.startCollecting;
    this.lastQueryTime = System.currentTimeMillis();
  }
  
  public CQueryStats add(int loadedBeanCount, int timeMicros)
  {
    return new CQueryStats(this, loadedBeanCount, timeMicros);
  }
  
  public int getCount()
  {
    return this.count;
  }
  
  public int getAverageTimeMicros()
  {
    if (this.count == 0) {
      return 0;
    }
    return this.totalTimeMicros / this.count;
  }
  
  public int getTotalLoadedBeanCount()
  {
    return this.totalLoadedBeanCount;
  }
  
  public int getTotalTimeMicros()
  {
    return this.totalTimeMicros;
  }
  
  public long getStartCollecting()
  {
    return this.startCollecting;
  }
  
  public long getLastQueryTime()
  {
    return this.lastQueryTime;
  }
  
  public MetaQueryStatistic createMetaQueryStatistic(String beanName, CQueryPlan qp)
  {
    return new MetaQueryStatistic(qp.isAutofetchTuned(), beanName, qp.getHash(), qp.getSql(), this.count, this.totalLoadedBeanCount, this.totalTimeMicros, this.startCollecting, this.lastQueryTime);
  }
}
