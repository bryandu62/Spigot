package com.avaje.ebean.text;

import java.sql.Time;

public final class TimeStringParser
  implements StringParser
{
  private static final TimeStringParser SHARED = new TimeStringParser();
  
  public static TimeStringParser get()
  {
    return SHARED;
  }
  
  public Object parse(String value)
  {
    if ((value == null) || (value.trim().length() == 0)) {
      return null;
    }
    String s = value.trim();
    
    int firstColon = s.indexOf(':');
    int secondColon = s.indexOf(':', firstColon + 1);
    if (firstColon == -1) {
      throw new IllegalArgumentException("No ':' in value [" + s + "]");
    }
    try
    {
      int hour = Integer.parseInt(s.substring(0, firstColon));
      int second;
      int minute;
      int second;
      if (secondColon == -1)
      {
        int minute = Integer.parseInt(s.substring(firstColon + 1, s.length()));
        second = 0;
      }
      else
      {
        minute = Integer.parseInt(s.substring(firstColon + 1, secondColon));
        second = Integer.parseInt(s.substring(secondColon + 1));
      }
      return new Time(hour, minute, second);
    }
    catch (NumberFormatException e)
    {
      throw new IllegalArgumentException("Number format Error parsing time [" + s + "] " + e.getMessage(), e);
    }
  }
}
