package com.avaje.ebeaninternal.server.autofetch;

import com.avaje.ebean.bean.CallStack;
import com.avaje.ebean.bean.NodeUsageCollector;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.bean.ObjectGraphOrigin;
import com.avaje.ebean.config.AutofetchConfig;
import com.avaje.ebean.config.AutofetchMode;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.ClassUtil;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryDetail;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import javax.persistence.PersistenceException;

public class DefaultAutoFetchManager
  implements AutoFetchManager, Serializable
{
  private static final long serialVersionUID = -6826119882781771722L;
  private final String statisticsMonitor = new String();
  private final String fileName;
  private Map<String, Statistics> statisticsMap = new ConcurrentHashMap();
  private Map<String, TunedQueryInfo> tunedQueryInfoMap = new ConcurrentHashMap();
  private transient long defaultGarbageCollectionWait = 100L;
  private transient int tunedQueryCount;
  private transient double profilingRate = 0.1D;
  private transient int profilingBase = 10;
  private transient int profilingMin = 1;
  private transient boolean profiling;
  private transient boolean queryTuning;
  private transient boolean queryTuningAddVersion;
  private transient AutofetchMode mode;
  private transient boolean useFileLogging;
  private transient SpiEbeanServer server;
  private transient DefaultAutoFetchManagerLogging logging;
  
  public DefaultAutoFetchManager(String fileName)
  {
    this.fileName = fileName;
  }
  
  public void setOwner(SpiEbeanServer server, ServerConfig serverConfig)
  {
    this.server = server;
    this.logging = new DefaultAutoFetchManagerLogging(serverConfig, this);
    
    AutofetchConfig autofetchConfig = serverConfig.getAutofetchConfig();
    
    this.useFileLogging = autofetchConfig.isUseFileLogging();
    this.queryTuning = autofetchConfig.isQueryTuning();
    this.queryTuningAddVersion = autofetchConfig.isQueryTuningAddVersion();
    this.profiling = autofetchConfig.isProfiling();
    this.profilingMin = autofetchConfig.getProfilingMin();
    this.profilingBase = autofetchConfig.getProfilingBase();
    
    setProfilingRate(autofetchConfig.getProfilingRate());
    
    this.defaultGarbageCollectionWait = autofetchConfig.getGarbageCollectionWait();
    
    this.mode = autofetchConfig.getMode();
    if ((this.profiling) || (this.queryTuning))
    {
      String msg = "AutoFetch queryTuning[" + this.queryTuning + "] profiling[" + this.profiling + "] mode[" + this.mode + "]  profiling rate[" + this.profilingRate + "] min[" + this.profilingMin + "] base[" + this.profilingBase + "]";
      
      this.logging.logToJavaLogger(msg);
    }
  }
  
  public void clearQueryStatistics()
  {
    this.server.clearQueryStatistics();
  }
  
  public int getTotalTunedQueryCount()
  {
    return this.tunedQueryCount;
  }
  
  public int getTotalTunedQuerySize()
  {
    return this.tunedQueryInfoMap.size();
  }
  
  public int getTotalProfileSize()
  {
    return this.statisticsMap.size();
  }
  
  public int clearTunedQueryInfo()
  {
    this.tunedQueryCount = 0;
    
    int size = this.tunedQueryInfoMap.size();
    this.tunedQueryInfoMap.clear();
    return size;
  }
  
  public int clearProfilingInfo()
  {
    int size = this.statisticsMap.size();
    this.statisticsMap.clear();
    return size;
  }
  
  public void serialize()
  {
    File autoFetchFile = new File(this.fileName);
    try
    {
      FileOutputStream fout = new FileOutputStream(autoFetchFile);
      
      ObjectOutputStream oout = new ObjectOutputStream(fout);
      oout.writeObject(this);
      oout.flush();
      oout.close();
    }
    catch (Exception e)
    {
      String msg = "Error serializing autofetch file";
      this.logging.logError(Level.SEVERE, msg, e);
    }
  }
  
  public TunedQueryInfo getTunedQueryInfo(String originKey)
  {
    return (TunedQueryInfo)this.tunedQueryInfoMap.get(originKey);
  }
  
  public Statistics getStatistics(String originKey)
  {
    return (Statistics)this.statisticsMap.get(originKey);
  }
  
  public Iterator<TunedQueryInfo> iterateTunedQueryInfo()
  {
    return this.tunedQueryInfoMap.values().iterator();
  }
  
  public Iterator<Statistics> iterateStatistics()
  {
    return this.statisticsMap.values().iterator();
  }
  
  public boolean isProfiling()
  {
    return this.profiling;
  }
  
  public void setProfiling(boolean profiling)
  {
    this.profiling = profiling;
  }
  
  public boolean isQueryTuning()
  {
    return this.queryTuning;
  }
  
  public void setQueryTuning(boolean queryTuning)
  {
    this.queryTuning = queryTuning;
  }
  
  public double getProfilingRate()
  {
    return this.profilingRate;
  }
  
  public AutofetchMode getMode()
  {
    return this.mode;
  }
  
  public void setMode(AutofetchMode mode)
  {
    this.mode = mode;
  }
  
  public void setProfilingRate(double rate)
  {
    if (rate < 0.0D) {
      rate = 0.0D;
    } else if (rate > 1.0D) {
      rate = 1.0D;
    }
    this.profilingRate = rate;
  }
  
  public int getProfilingBase()
  {
    return this.profilingBase;
  }
  
  public void setProfilingBase(int profilingBase)
  {
    this.profilingBase = profilingBase;
  }
  
  public int getProfilingMin()
  {
    return this.profilingMin;
  }
  
  public void setProfilingMin(int profilingMin)
  {
    this.profilingMin = profilingMin;
  }
  
  public void shutdown()
  {
    if (this.useFileLogging)
    {
      collectUsageViaGC(-1L);
      serialize();
    }
  }
  
  public String collectUsageViaGC(long waitMillis)
  {
    
    try
    {
      if (waitMillis < 0L) {
        waitMillis = this.defaultGarbageCollectionWait;
      }
      Thread.sleep(waitMillis);
    }
    catch (InterruptedException e)
    {
      String msg = "Error while sleeping after System.gc() request.";
      this.logging.logError(Level.SEVERE, msg, e);
      return msg;
    }
    return updateTunedQueryInfo();
  }
  
  public String updateTunedQueryInfo()
  {
    if (!this.profiling) {
      return "Not profiling";
    }
    synchronized (this.statisticsMonitor)
    {
      Counters counters = new Counters(null);
      
      Iterator<Statistics> it = this.statisticsMap.values().iterator();
      while (it.hasNext())
      {
        Statistics queryPointStatistics = (Statistics)it.next();
        if (!queryPointStatistics.hasUsage()) {
          counters.incrementNoUsage();
        } else {
          updateTunedQueryFromUsage(counters, queryPointStatistics);
        }
      }
      String summaryInfo = counters.toString();
      if (counters.isInteresting()) {
        this.logging.logSummary(summaryInfo);
      }
      return summaryInfo;
    }
  }
  
  private static class Counters
  {
    int newPlan;
    int modified;
    int unchanged;
    int noUsage;
    
    void incrementNoUsage()
    {
      this.noUsage += 1;
    }
    
    void incrementNew()
    {
      this.newPlan += 1;
    }
    
    void incrementModified()
    {
      this.modified += 1;
    }
    
    void incrementUnchanged()
    {
      this.unchanged += 1;
    }
    
    boolean isInteresting()
    {
      return (this.newPlan > 0) || (this.modified > 0);
    }
    
    public String toString()
    {
      return "new[" + this.newPlan + "] modified[" + this.modified + "] unchanged[" + this.unchanged + "] nousage[" + this.noUsage + "]";
    }
  }
  
  private void updateTunedQueryFromUsage(Counters counters, Statistics statistics)
  {
    ObjectGraphOrigin queryPoint = statistics.getOrigin();
    String beanType = queryPoint.getBeanType();
    try
    {
      Class<?> beanClass = ClassUtil.forName(beanType, getClass());
      BeanDescriptor<?> beanDescriptor = this.server.getBeanDescriptor(beanClass);
      if (beanDescriptor != null)
      {
        OrmQueryDetail newFetchDetail = statistics.buildTunedFetch(beanDescriptor);
        
        TunedQueryInfo currentFetch = (TunedQueryInfo)this.tunedQueryInfoMap.get(queryPoint.getKey());
        if (currentFetch == null)
        {
          counters.incrementNew();
          
          currentFetch = statistics.createTunedFetch(newFetchDetail);
          this.logging.logNew(currentFetch);
          this.tunedQueryInfoMap.put(queryPoint.getKey(), currentFetch);
        }
        else if (!currentFetch.isSame(newFetchDetail))
        {
          counters.incrementModified();
          
          this.logging.logChanged(currentFetch, newFetchDetail);
          currentFetch.setTunedDetail(newFetchDetail);
        }
        else
        {
          counters.incrementUnchanged();
        }
        currentFetch.setProfileCount(statistics.getCounter());
      }
    }
    catch (ClassNotFoundException e)
    {
      String msg = e.toString() + " updating autoFetch tuned query for " + beanType + ". It isLikely this bean has been renamed or moved";
      
      this.logging.logError(Level.INFO, msg, null);
      this.statisticsMap.remove(statistics.getOrigin().getKey());
    }
  }
  
  private boolean useAutoFetch(SpiQuery<?> query)
  {
    if (query.isLoadBeanCache()) {
      return false;
    }
    Boolean autoFetch = query.isAutofetch();
    if (autoFetch != null) {
      return autoFetch.booleanValue();
    }
    switch (this.mode)
    {
    case DEFAULT_ON: 
      return true;
    case DEFAULT_OFF: 
      return false;
    case DEFAULT_ONIFEMPTY: 
      return query.isDetailEmpty();
    }
    throw new PersistenceException("Invalid autoFetchMode " + this.mode);
  }
  
  public boolean tuneQuery(SpiQuery<?> query)
  {
    if ((!this.queryTuning) && (!this.profiling)) {
      return false;
    }
    if (!useAutoFetch(query)) {
      return false;
    }
    ObjectGraphNode parentAutoFetchNode = query.getParentNode();
    if (parentAutoFetchNode != null)
    {
      query.setAutoFetchManager(this);
      return true;
    }
    CallStack stack = this.server.createCallStack();
    ObjectGraphNode origin = query.setOrigin(stack);
    
    TunedQueryInfo tunedFetch = (TunedQueryInfo)this.tunedQueryInfoMap.get(origin.getOriginQueryPoint().getKey());
    
    int profileCount = tunedFetch == null ? 0 : tunedFetch.getProfileCount();
    if (this.profiling) {
      if (tunedFetch == null) {
        query.setAutoFetchManager(this);
      } else if (profileCount < this.profilingBase) {
        query.setAutoFetchManager(this);
      } else if (tunedFetch.isPercentageProfile(this.profilingRate)) {
        query.setAutoFetchManager(this);
      }
    }
    if ((this.queryTuning) && 
      (tunedFetch != null) && (profileCount >= this.profilingMin))
    {
      if (tunedFetch.autoFetchTune(query)) {
        this.tunedQueryCount += 1;
      }
      return true;
    }
    return false;
  }
  
  public void collectQueryInfo(ObjectGraphNode node, int beans, int micros)
  {
    if (node != null)
    {
      ObjectGraphOrigin origin = node.getOriginQueryPoint();
      if (origin != null)
      {
        Statistics stats = getQueryPointStats(origin);
        stats.collectQueryInfo(node, beans, micros);
      }
    }
  }
  
  public void collectNodeUsage(NodeUsageCollector usageCollector)
  {
    ObjectGraphOrigin origin = usageCollector.getNode().getOriginQueryPoint();
    
    Statistics stats = getQueryPointStats(origin);
    if (this.logging.isTraceUsageCollection()) {
      System.out.println("... NodeUsageCollector " + usageCollector);
    }
    stats.collectUsageInfo(usageCollector);
    if (this.logging.isTraceUsageCollection()) {
      System.out.println("stats\n" + stats);
    }
  }
  
  private Statistics getQueryPointStats(ObjectGraphOrigin originQueryPoint)
  {
    synchronized (this.statisticsMonitor)
    {
      Statistics stats = (Statistics)this.statisticsMap.get(originQueryPoint.getKey());
      if (stats == null)
      {
        stats = new Statistics(originQueryPoint, this.queryTuningAddVersion);
        this.statisticsMap.put(originQueryPoint.getKey(), stats);
      }
      return stats;
    }
  }
  
  /* Error */
  public String toString()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 54	com/avaje/ebeaninternal/server/autofetch/DefaultAutoFetchManager:statisticsMonitor	Ljava/lang/String;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 59	com/avaje/ebeaninternal/server/autofetch/DefaultAutoFetchManager:statisticsMap	Ljava/util/Map;
    //   11: invokeinterface 272 1 0
    //   16: invokevirtual 576	java/lang/Object:toString	()Ljava/lang/String;
    //   19: aload_1
    //   20: monitorexit
    //   21: areturn
    //   22: astore_2
    //   23: aload_1
    //   24: monitorexit
    //   25: aload_2
    //   26: athrow
    // Line number table:
    //   Java source line #576	-> byte code offset #0
    //   Java source line #577	-> byte code offset #7
    //   Java source line #578	-> byte code offset #22
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	27	0	this	DefaultAutoFetchManager
    //   5	19	1	Ljava/lang/Object;	Object
    //   22	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	21	22	finally
    //   22	25	22	finally
  }
}
