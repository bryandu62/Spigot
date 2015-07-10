package com.avaje.ebeaninternal.server.autofetch;

import com.avaje.ebean.bean.NodeUsageListener;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.config.AutofetchMode;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiQuery;
import java.util.Iterator;

public abstract interface AutoFetchManager
  extends NodeUsageListener
{
  public abstract void setOwner(SpiEbeanServer paramSpiEbeanServer, ServerConfig paramServerConfig);
  
  public abstract void clearQueryStatistics();
  
  public abstract int clearTunedQueryInfo();
  
  public abstract int clearProfilingInfo();
  
  public abstract void shutdown();
  
  public abstract TunedQueryInfo getTunedQueryInfo(String paramString);
  
  public abstract Statistics getStatistics(String paramString);
  
  public abstract Iterator<TunedQueryInfo> iterateTunedQueryInfo();
  
  public abstract Iterator<Statistics> iterateStatistics();
  
  public abstract boolean isProfiling();
  
  public abstract void setProfiling(boolean paramBoolean);
  
  public abstract boolean isQueryTuning();
  
  public abstract void setQueryTuning(boolean paramBoolean);
  
  public abstract AutofetchMode getMode();
  
  public abstract void setMode(AutofetchMode paramAutofetchMode);
  
  public abstract double getProfilingRate();
  
  public abstract void setProfilingRate(double paramDouble);
  
  public abstract int getProfilingBase();
  
  public abstract void setProfilingBase(int paramInt);
  
  public abstract int getProfilingMin();
  
  public abstract void setProfilingMin(int paramInt);
  
  public abstract String collectUsageViaGC(long paramLong);
  
  public abstract String updateTunedQueryInfo();
  
  public abstract boolean tuneQuery(SpiQuery<?> paramSpiQuery);
  
  public abstract void collectQueryInfo(ObjectGraphNode paramObjectGraphNode, int paramInt1, int paramInt2);
  
  public abstract int getTotalTunedQueryCount();
  
  public abstract int getTotalTunedQuerySize();
  
  public abstract int getTotalProfileSize();
}
