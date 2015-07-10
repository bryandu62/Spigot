package com.avaje.ebean.config;

public class AutofetchConfig
{
  private AutofetchMode mode = AutofetchMode.DEFAULT_ONIFEMPTY;
  private boolean queryTuning = false;
  private boolean queryTuningAddVersion = false;
  private boolean profiling = false;
  private int profilingMin = 1;
  private int profilingBase = 10;
  private double profilingRate = 0.05D;
  private boolean useFileLogging = false;
  private String logDirectory;
  private int profileUpdateFrequency = 60;
  private int garbageCollectionWait = 100;
  
  public AutofetchMode getMode()
  {
    return this.mode;
  }
  
  public void setMode(AutofetchMode mode)
  {
    this.mode = mode;
  }
  
  public boolean isQueryTuning()
  {
    return this.queryTuning;
  }
  
  public void setQueryTuning(boolean queryTuning)
  {
    this.queryTuning = queryTuning;
  }
  
  public boolean isQueryTuningAddVersion()
  {
    return this.queryTuningAddVersion;
  }
  
  public void setQueryTuningAddVersion(boolean queryTuningAddVersion)
  {
    this.queryTuningAddVersion = queryTuningAddVersion;
  }
  
  public boolean isProfiling()
  {
    return this.profiling;
  }
  
  public void setProfiling(boolean profiling)
  {
    this.profiling = profiling;
  }
  
  public int getProfilingMin()
  {
    return this.profilingMin;
  }
  
  public void setProfilingMin(int profilingMin)
  {
    this.profilingMin = profilingMin;
  }
  
  public int getProfilingBase()
  {
    return this.profilingBase;
  }
  
  public void setProfilingBase(int profilingBase)
  {
    this.profilingBase = profilingBase;
  }
  
  public double getProfilingRate()
  {
    return this.profilingRate;
  }
  
  public void setProfilingRate(double profilingRate)
  {
    this.profilingRate = profilingRate;
  }
  
  public boolean isUseFileLogging()
  {
    return this.useFileLogging;
  }
  
  public void setUseFileLogging(boolean useFileLogging)
  {
    this.useFileLogging = useFileLogging;
  }
  
  public String getLogDirectory()
  {
    return this.logDirectory;
  }
  
  public String getLogDirectoryWithEval()
  {
    return GlobalProperties.evaluateExpressions(this.logDirectory);
  }
  
  public void setLogDirectory(String logDirectory)
  {
    this.logDirectory = logDirectory;
  }
  
  public int getProfileUpdateFrequency()
  {
    return this.profileUpdateFrequency;
  }
  
  public void setProfileUpdateFrequency(int profileUpdateFrequency)
  {
    this.profileUpdateFrequency = profileUpdateFrequency;
  }
  
  public int getGarbageCollectionWait()
  {
    return this.garbageCollectionWait;
  }
  
  public void setGarbageCollectionWait(int garbageCollectionWait)
  {
    this.garbageCollectionWait = garbageCollectionWait;
  }
  
  public void loadSettings(ConfigPropertyMap p)
  {
    this.logDirectory = p.get("autofetch.logDirectory", null);
    this.queryTuning = p.getBoolean("autofetch.querytuning", false);
    this.queryTuningAddVersion = p.getBoolean("autofetch.queryTuningAddVersion", false);
    
    this.profiling = p.getBoolean("autofetch.profiling", false);
    this.mode = ((AutofetchMode)p.getEnum(AutofetchMode.class, "autofetch.implicitmode", AutofetchMode.DEFAULT_ONIFEMPTY));
    
    this.profilingMin = p.getInt("autofetch.profiling.min", 1);
    this.profilingBase = p.getInt("autofetch.profiling.base", 10);
    
    String rate = p.get("autofetch.profiling.rate", "0.05");
    this.profilingRate = Double.parseDouble(rate);
    
    this.useFileLogging = p.getBoolean("autofetch.useFileLogging", this.profiling);
    this.profileUpdateFrequency = p.getInt("autofetch.profiling.updatefrequency", 60);
  }
}
