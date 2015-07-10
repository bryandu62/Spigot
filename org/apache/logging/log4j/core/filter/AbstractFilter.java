package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractFilter
  implements Filter, LifeCycle
{
  protected static final org.apache.logging.log4j.Logger LOGGER = ;
  protected final Filter.Result onMatch;
  protected final Filter.Result onMismatch;
  private boolean started;
  
  protected AbstractFilter()
  {
    this(null, null);
  }
  
  protected AbstractFilter(Filter.Result onMatch, Filter.Result onMismatch)
  {
    this.onMatch = (onMatch == null ? Filter.Result.NEUTRAL : onMatch);
    this.onMismatch = (onMismatch == null ? Filter.Result.DENY : onMismatch);
  }
  
  public void start()
  {
    this.started = true;
  }
  
  public boolean isStarted()
  {
    return this.started;
  }
  
  public void stop()
  {
    this.started = false;
  }
  
  public final Filter.Result getOnMismatch()
  {
    return this.onMismatch;
  }
  
  public final Filter.Result getOnMatch()
  {
    return this.onMatch;
  }
  
  public String toString()
  {
    return getClass().getSimpleName();
  }
  
  public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String msg, Object... params)
  {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Object msg, Throwable t)
  {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Message msg, Throwable t)
  {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(LogEvent event)
  {
    return Filter.Result.NEUTRAL;
  }
}
