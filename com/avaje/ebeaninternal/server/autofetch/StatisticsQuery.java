package com.avaje.ebeaninternal.server.autofetch;

import com.avaje.ebean.meta.MetaAutoFetchStatistic.QueryStats;
import java.io.Serializable;

public class StatisticsQuery
  implements Serializable
{
  private static final long serialVersionUID = -1133958958072778811L;
  private final String path;
  private int exeCount;
  private int totalBeanLoaded;
  private int totalMicros;
  
  public StatisticsQuery(String path)
  {
    this.path = path;
  }
  
  public MetaAutoFetchStatistic.QueryStats createPublicMeta()
  {
    return new MetaAutoFetchStatistic.QueryStats(this.path, this.exeCount, this.totalBeanLoaded, this.totalMicros);
  }
  
  public void add(int beansLoaded, int micros)
  {
    this.exeCount += 1;
    this.totalBeanLoaded += beansLoaded;
    this.totalMicros += micros;
  }
  
  public String toString()
  {
    long avgMicros = this.exeCount == 0 ? 0L : this.totalMicros / this.exeCount;
    
    return "queryExe path[" + this.path + "] count[" + this.exeCount + "] totalBeansLoaded[" + this.totalBeanLoaded + "] avgMicros[" + avgMicros + "] totalMicros[" + this.totalMicros + "]";
  }
}
