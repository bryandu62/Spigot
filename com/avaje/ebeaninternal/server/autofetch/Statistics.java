package com.avaje.ebeaninternal.server.autofetch;

import com.avaje.ebean.bean.NodeUsageCollector;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.bean.ObjectGraphOrigin;
import com.avaje.ebean.meta.MetaAutoFetchStatistic;
import com.avaje.ebean.meta.MetaAutoFetchStatistic.NodeUsageStats;
import com.avaje.ebean.meta.MetaAutoFetchStatistic.QueryStats;
import com.avaje.ebean.text.PathProperties;
import com.avaje.ebean.text.PathProperties.Props;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryDetail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Statistics
  implements Serializable
{
  private static final long serialVersionUID = -5586783791097230766L;
  private final ObjectGraphOrigin origin;
  private final boolean queryTuningAddVersion;
  private int counter;
  private Map<String, StatisticsQuery> queryStatsMap = new LinkedHashMap();
  private Map<String, StatisticsNodeUsage> nodeUsageMap = new LinkedHashMap();
  private final String monitor = new String();
  
  public Statistics(ObjectGraphOrigin origin, boolean queryTuningAddVersion)
  {
    this.origin = origin;
    this.queryTuningAddVersion = queryTuningAddVersion;
  }
  
  public ObjectGraphOrigin getOrigin()
  {
    return this.origin;
  }
  
  /* Error */
  public TunedQueryInfo createTunedFetch(OrmQueryDetail newFetchDetail)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 54	com/avaje/ebeaninternal/server/autofetch/Statistics:monitor	Ljava/lang/String;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: new 66	com/avaje/ebeaninternal/server/autofetch/TunedQueryInfo
    //   10: dup
    //   11: aload_0
    //   12: getfield 56	com/avaje/ebeaninternal/server/autofetch/Statistics:origin	Lcom/avaje/ebean/bean/ObjectGraphOrigin;
    //   15: aload_1
    //   16: aload_0
    //   17: getfield 68	com/avaje/ebeaninternal/server/autofetch/Statistics:counter	I
    //   20: invokespecial 71	com/avaje/ebeaninternal/server/autofetch/TunedQueryInfo:<init>	(Lcom/avaje/ebean/bean/ObjectGraphOrigin;Lcom/avaje/ebeaninternal/server/querydefn/OrmQueryDetail;I)V
    //   23: aload_2
    //   24: monitorexit
    //   25: areturn
    //   26: astore_3
    //   27: aload_2
    //   28: monitorexit
    //   29: aload_3
    //   30: athrow
    // Line number table:
    //   Java source line #49	-> byte code offset #0
    //   Java source line #52	-> byte code offset #7
    //   Java source line #53	-> byte code offset #26
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	31	0	this	Statistics
    //   0	31	1	newFetchDetail	OrmQueryDetail
    //   5	23	2	Ljava/lang/Object;	Object
    //   26	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	25	26	finally
    //   26	29	26	finally
  }
  
  public MetaAutoFetchStatistic createPublicMeta()
  {
    synchronized (this.monitor)
    {
      StatisticsQuery[] sourceQueryStats = (StatisticsQuery[])this.queryStatsMap.values().toArray(new StatisticsQuery[this.queryStatsMap.size()]);
      List<MetaAutoFetchStatistic.QueryStats> destQueryStats = new ArrayList(sourceQueryStats.length);
      for (int i = 0; i < sourceQueryStats.length; i++) {
        destQueryStats.add(sourceQueryStats[i].createPublicMeta());
      }
      StatisticsNodeUsage[] sourceNodeUsage = (StatisticsNodeUsage[])this.nodeUsageMap.values().toArray(new StatisticsNodeUsage[this.nodeUsageMap.size()]);
      List<MetaAutoFetchStatistic.NodeUsageStats> destNodeUsage = new ArrayList(sourceNodeUsage.length);
      for (int i = 0; i < sourceNodeUsage.length; i++) {
        destNodeUsage.add(sourceNodeUsage[i].createPublicMeta());
      }
      return new MetaAutoFetchStatistic(this.origin, this.counter, destQueryStats, destNodeUsage);
    }
  }
  
  public int getCounter()
  {
    return this.counter;
  }
  
  public boolean hasUsage()
  {
    synchronized (this.monitor)
    {
      return !this.nodeUsageMap.isEmpty();
    }
  }
  
  public OrmQueryDetail buildTunedFetch(BeanDescriptor<?> rootDesc)
  {
    synchronized (this.monitor)
    {
      if (this.nodeUsageMap.isEmpty()) {
        return null;
      }
      PathProperties pathProps = new PathProperties();
      
      Iterator<StatisticsNodeUsage> it = this.nodeUsageMap.values().iterator();
      while (it.hasNext())
      {
        StatisticsNodeUsage statsNode = (StatisticsNodeUsage)it.next();
        statsNode.buildTunedFetch(pathProps, rootDesc);
      }
      OrmQueryDetail detail = new OrmQueryDetail();
      
      Collection<PathProperties.Props> pathProperties = pathProps.getPathProps();
      for (PathProperties.Props props : pathProperties) {
        if (!props.isEmpty()) {
          detail.addFetch(props.getPath(), props.getPropertiesAsString(), null);
        }
      }
      detail.sortFetchPaths(rootDesc);
      return detail;
    }
  }
  
  public void collectQueryInfo(ObjectGraphNode node, int beansLoaded, int micros)
  {
    synchronized (this.monitor)
    {
      String key = node.getPath();
      if (key == null)
      {
        key = "";
        
        this.counter += 1;
      }
      StatisticsQuery stats = (StatisticsQuery)this.queryStatsMap.get(key);
      if (stats == null)
      {
        stats = new StatisticsQuery(key);
        this.queryStatsMap.put(key, stats);
      }
      stats.add(beansLoaded, micros);
    }
  }
  
  public void collectUsageInfo(NodeUsageCollector profile)
  {
    if (!profile.isEmpty())
    {
      ObjectGraphNode node = profile.getNode();
      
      StatisticsNodeUsage nodeStats = getNodeStats(node.getPath());
      nodeStats.publish(profile);
    }
  }
  
  private StatisticsNodeUsage getNodeStats(String path)
  {
    synchronized (this.monitor)
    {
      StatisticsNodeUsage nodeStats = (StatisticsNodeUsage)this.nodeUsageMap.get(path);
      if (nodeStats == null)
      {
        nodeStats = new StatisticsNodeUsage(path, this.queryTuningAddVersion);
        this.nodeUsageMap.put(path, nodeStats);
      }
      return nodeStats;
    }
  }
  
  public String getUsageDebug()
  {
    synchronized (this.monitor)
    {
      StringBuilder sb = new StringBuilder();
      sb.append("root[").append(this.origin.getBeanType()).append("] ");
      for (StatisticsNodeUsage node : this.nodeUsageMap.values()) {
        sb.append(node.toString()).append("\n");
      }
      return sb.toString();
    }
  }
  
  public String getQueryStatDebug()
  {
    synchronized (this.monitor)
    {
      StringBuilder sb = new StringBuilder();
      for (StatisticsQuery queryStat : this.queryStatsMap.values()) {
        sb.append(queryStat.toString()).append("\n");
      }
      return sb.toString();
    }
  }
  
  /* Error */
  public String toString()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 54	com/avaje/ebeaninternal/server/autofetch/Statistics:monitor	Ljava/lang/String;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: invokevirtual 272	com/avaje/ebeaninternal/server/autofetch/Statistics:getUsageDebug	()Ljava/lang/String;
    //   11: aload_1
    //   12: monitorexit
    //   13: areturn
    //   14: astore_2
    //   15: aload_1
    //   16: monitorexit
    //   17: aload_2
    //   18: athrow
    // Line number table:
    //   Java source line #202	-> byte code offset #0
    //   Java source line #203	-> byte code offset #7
    //   Java source line #204	-> byte code offset #14
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	19	0	this	Statistics
    //   5	11	1	Ljava/lang/Object;	Object
    //   14	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	13	14	finally
    //   14	17	14	finally
  }
}
