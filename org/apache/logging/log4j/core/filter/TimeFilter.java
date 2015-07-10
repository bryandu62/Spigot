package org.apache.logging.log4j.core.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name="TimeFilter", category="Core", elementType="filter", printObject=true)
public final class TimeFilter
  extends AbstractFilter
{
  private static final long HOUR_MS = 3600000L;
  private static final long MINUTE_MS = 60000L;
  private static final long SECOND_MS = 1000L;
  private final long start;
  private final long end;
  private final TimeZone timezone;
  
  private TimeFilter(long start, long end, TimeZone tz, Filter.Result onMatch, Filter.Result onMismatch)
  {
    super(onMatch, onMismatch);
    this.start = start;
    this.end = end;
    this.timezone = tz;
  }
  
  public Filter.Result filter(LogEvent event)
  {
    Calendar calendar = Calendar.getInstance(this.timezone);
    calendar.setTimeInMillis(event.getMillis());
    
    long apparentOffset = calendar.get(11) * 3600000L + calendar.get(12) * 60000L + calendar.get(13) * 1000L + calendar.get(14);
    
    return (apparentOffset >= this.start) && (apparentOffset < this.end) ? this.onMatch : this.onMismatch;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("start=").append(this.start);
    sb.append(", end=").append(this.end);
    sb.append(", timezone=").append(this.timezone.toString());
    return sb.toString();
  }
  
  @PluginFactory
  public static TimeFilter createFilter(@PluginAttribute("start") String start, @PluginAttribute("end") String end, @PluginAttribute("timezone") String tz, @PluginAttribute("onMatch") String match, @PluginAttribute("onMismatch") String mismatch)
  {
    SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
    long s = 0L;
    if (start != null)
    {
      stf.setTimeZone(TimeZone.getTimeZone("UTC"));
      try
      {
        s = stf.parse(start).getTime();
      }
      catch (ParseException ex)
      {
        LOGGER.warn("Error parsing start value " + start, ex);
      }
    }
    long e = Long.MAX_VALUE;
    if (end != null)
    {
      stf.setTimeZone(TimeZone.getTimeZone("UTC"));
      try
      {
        e = stf.parse(end).getTime();
      }
      catch (ParseException ex)
      {
        LOGGER.warn("Error parsing start value " + end, ex);
      }
    }
    TimeZone timezone = tz == null ? TimeZone.getDefault() : TimeZone.getTimeZone(tz);
    Filter.Result onMatch = Filter.Result.toResult(match, Filter.Result.NEUTRAL);
    Filter.Result onMismatch = Filter.Result.toResult(mismatch, Filter.Result.DENY);
    return new TimeFilter(s, e, timezone, onMatch, onMismatch);
  }
}
