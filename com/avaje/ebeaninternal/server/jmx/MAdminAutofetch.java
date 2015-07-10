package com.avaje.ebeaninternal.server.jmx;

import com.avaje.ebean.AdminAutofetch;
import com.avaje.ebean.config.AutofetchMode;
import com.avaje.ebeaninternal.server.autofetch.AutoFetchManager;
import java.util.logging.Logger;

public class MAdminAutofetch
  implements MAdminAutofetchMBean, AdminAutofetch
{
  final Logger logger = Logger.getLogger(MAdminAutofetch.class.getName());
  final AutoFetchManager autoFetchManager;
  final String modeOptions;
  
  public MAdminAutofetch(AutoFetchManager autoFetchListener)
  {
    this.autoFetchManager = autoFetchListener;
    this.modeOptions = (AutofetchMode.DEFAULT_OFF + ", " + AutofetchMode.DEFAULT_ON + ", " + AutofetchMode.DEFAULT_ONIFEMPTY);
  }
  
  public boolean isQueryTuning()
  {
    return this.autoFetchManager.isQueryTuning();
  }
  
  public void setQueryTuning(boolean enable)
  {
    this.autoFetchManager.setQueryTuning(enable);
  }
  
  public boolean isProfiling()
  {
    return this.autoFetchManager.isProfiling();
  }
  
  public void setProfiling(boolean enable)
  {
    this.autoFetchManager.setProfiling(enable);
  }
  
  public String getModeOptions()
  {
    return this.modeOptions;
  }
  
  public String getMode()
  {
    return this.autoFetchManager.getMode().name();
  }
  
  public void setMode(String implicitMode)
  {
    try
    {
      AutofetchMode mode = AutofetchMode.valueOf(implicitMode);
      this.autoFetchManager.setMode(mode);
    }
    catch (Exception e)
    {
      this.logger.info("Invalid implicit mode attempted " + e.getMessage());
    }
  }
  
  public String collectUsageViaGC()
  {
    return this.autoFetchManager.collectUsageViaGC(-1L);
  }
  
  public double getProfilingRate()
  {
    return this.autoFetchManager.getProfilingRate();
  }
  
  public void setProfilingRate(double rate)
  {
    this.autoFetchManager.setProfilingRate(rate);
  }
  
  public int getProfilingMin()
  {
    return this.autoFetchManager.getProfilingMin();
  }
  
  public int getProfilingBase()
  {
    return this.autoFetchManager.getProfilingBase();
  }
  
  public void setProfilingMin(int profilingMin)
  {
    this.autoFetchManager.setProfilingMin(profilingMin);
  }
  
  public void setProfilingBase(int profilingMax)
  {
    this.autoFetchManager.setProfilingBase(profilingMax);
  }
  
  public String updateTunedQueryInfo()
  {
    return this.autoFetchManager.updateTunedQueryInfo();
  }
  
  public int clearProfilingInfo()
  {
    return this.autoFetchManager.clearProfilingInfo();
  }
  
  public int clearTunedQueryInfo()
  {
    return this.autoFetchManager.clearTunedQueryInfo();
  }
  
  public void clearQueryStatistics()
  {
    this.autoFetchManager.clearQueryStatistics();
  }
  
  public int getTotalProfileSize()
  {
    return this.autoFetchManager.getTotalProfileSize();
  }
  
  public int getTotalTunedQueryCount()
  {
    return this.autoFetchManager.getTotalTunedQueryCount();
  }
  
  public int getTotalTunedQuerySize()
  {
    return this.autoFetchManager.getTotalTunedQuerySize();
  }
}
