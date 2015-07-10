package com.avaje.ebeaninternal.server.lib.thread;

public class Work
{
  private Runnable runnable;
  private long exitQueueTime;
  private long enterQueueTime;
  private long startTime;
  
  public Work(Runnable runnable)
  {
    this.runnable = runnable;
  }
  
  public Runnable getRunnable()
  {
    return this.runnable;
  }
  
  public long getStartTime()
  {
    return this.startTime;
  }
  
  public void setStartTime(long startTime)
  {
    this.startTime = startTime;
  }
  
  public long getEnterQueueTime()
  {
    return this.enterQueueTime;
  }
  
  public void setEnterQueueTime(long enterQueueTime)
  {
    this.enterQueueTime = enterQueueTime;
  }
  
  public long getExitQueueTime()
  {
    return this.exitQueueTime;
  }
  
  public void setExitQueueTime(long exitQueueTime)
  {
    this.exitQueueTime = exitQueueTime;
  }
  
  public String toString()
  {
    return getDescription();
  }
  
  public String getDescription()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("Work[");
    if (this.runnable != null) {
      sb.append(this.runnable.toString());
    }
    sb.append("]");
    return sb.toString();
  }
}
