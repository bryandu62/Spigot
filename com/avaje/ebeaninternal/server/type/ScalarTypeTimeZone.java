package com.avaje.ebeaninternal.server.type;

import java.util.TimeZone;

public class ScalarTypeTimeZone
  extends ScalarTypeBaseVarchar<TimeZone>
{
  public ScalarTypeTimeZone()
  {
    super(TimeZone.class);
  }
  
  public int getLength()
  {
    return 20;
  }
  
  public TimeZone convertFromDbString(String dbValue)
  {
    return TimeZone.getTimeZone(dbValue);
  }
  
  public String convertToDbString(TimeZone beanValue)
  {
    return beanValue.getID();
  }
  
  public String formatValue(TimeZone v)
  {
    return v.toString();
  }
  
  public TimeZone parse(String value)
  {
    return TimeZone.getTimeZone(value);
  }
}
