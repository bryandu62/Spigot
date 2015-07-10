package com.avaje.ebeaninternal.server.lib;

public class BackgroundRunnable
{
  Runnable runnable;
  int freqInSecs;
  int runCount = 0;
  long totalRunTime = 0L;
  long startTimeTemp;
  long startAfter;
  boolean isActive = true;
  
  public BackgroundRunnable(Runnable runnable, int freqInSecs)
  {
    this(runnable, freqInSecs, System.currentTimeMillis() + 1000 * (freqInSecs + 10));
  }
  
  public BackgroundRunnable(Runnable runnable, int freqInSecs, long startAfter)
  {
    this.runnable = runnable;
    this.freqInSecs = freqInSecs;
    this.startAfter = startAfter;
  }
  
  public boolean runNow(long now)
  {
    return now > this.startAfter;
  }
  
  public boolean isActive()
  {
    return this.isActive;
  }
  
  public void setActive(boolean isActive)
  {
    this.isActive = isActive;
  }
  
  protected void runStart()
  {
    this.startTimeTemp = System.currentTimeMillis();
  }
  
  protected void runEnd()
  {
    this.runCount += 1;
    long exeTime = System.currentTimeMillis() - this.startTimeTemp;
    this.totalRunTime += exeTime;
  }
  
  public int getRunCount()
  {
    return this.runCount;
  }
  
  public long getAverageRunTime()
  {
    if (this.runCount == 0) {
      return 0L;
    }
    return this.totalRunTime / this.runCount;
  }
  
  public int getFreqInSecs()
  {
    return this.freqInSecs;
  }
  
  public void setFreqInSecs(int freqInSecs)
  {
    this.freqInSecs = freqInSecs;
  }
  
  public Runnable getRunnable()
  {
    return this.runnable;
  }
  
  public void setRunnable(Runnable runnable)
  {
    this.runnable = runnable;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append(this.runnable.getClass().getName());
    sb.append(" freq:").append(this.freqInSecs);
    sb.append(" count:").append(getRunCount());
    sb.append(" avgTime:").append(getAverageRunTime());
    sb.append("]");
    return sb.toString();
  }
}
