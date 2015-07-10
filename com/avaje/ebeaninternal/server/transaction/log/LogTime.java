package com.avaje.ebeaninternal.server.transaction.log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class LogTime
{
  private static final String[] sep = { ":", "." };
  private static LogTime day = new LogTime();
  private final String ymd;
  private final long startMidnight;
  private final long startTomorrow;
  
  public static LogTime get()
  {
    return day;
  }
  
  public static LogTime nextDay()
  {
    LogTime d = new LogTime();
    day = d;
    return d;
  }
  
  public static LogTime getWithCheck()
  {
    LogTime d = day;
    if (d.isNextDay()) {
      return nextDay();
    }
    return d;
  }
  
  private LogTime()
  {
    GregorianCalendar now = new GregorianCalendar();
    
    now.set(11, 0);
    now.set(12, 0);
    now.set(13, 0);
    now.set(14, 0);
    
    this.startMidnight = now.getTime().getTime();
    this.ymd = getDayDerived(now);
    
    now.add(5, 1);
    this.startTomorrow = now.getTime().getTime();
  }
  
  public boolean isNextDay()
  {
    return System.currentTimeMillis() >= this.startTomorrow;
  }
  
  public String getYMD()
  {
    return this.ymd;
  }
  
  public String getNow(String[] separators)
  {
    return getTimestamp(System.currentTimeMillis(), separators);
  }
  
  public String getTimestamp(long systime)
  {
    StringBuilder sb = new StringBuilder();
    getTime(sb, systime, this.startMidnight, sep);
    return sb.toString();
  }
  
  public String getTimestamp(long systime, String[] separators)
  {
    StringBuilder sb = new StringBuilder();
    getTime(sb, systime, this.startMidnight, separators);
    return sb.toString();
  }
  
  public String getNow()
  {
    return getNow(sep);
  }
  
  private String getDayDerived(Calendar now)
  {
    int nowyear = now.get(1);
    int nowmonth = now.get(2);
    int nowday = now.get(5);
    
    nowmonth++;
    
    StringBuilder sb = new StringBuilder();
    
    format(sb, nowyear, 4);
    format(sb, nowmonth, 2);
    format(sb, nowday, 2);
    
    return sb.toString();
  }
  
  private void getTime(StringBuilder sb, long time, long midnight, String[] separator)
  {
    long rem = time - midnight;
    
    long millis = rem % 1000L;
    rem /= 1000L;
    long secs = rem % 60L;
    rem /= 60L;
    long mins = rem % 60L;
    rem /= 60L;
    long hrs = rem;
    
    format(sb, hrs, 2);
    sb.append(separator[0]);
    format(sb, mins, 2);
    sb.append(separator[0]);
    format(sb, secs, 2);
    sb.append(separator[1]);
    format(sb, millis, 3);
  }
  
  private void format(StringBuilder sb, long value, int places)
  {
    String format = Long.toString(value);
    
    int pad = places - format.length();
    for (int i = 0; i < pad; i++) {
      sb.append("0");
    }
    sb.append(format);
  }
}
