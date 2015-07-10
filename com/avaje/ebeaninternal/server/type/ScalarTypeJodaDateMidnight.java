package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import org.joda.time.DateMidnight;

public class ScalarTypeJodaDateMidnight
  extends ScalarTypeBaseDate<DateMidnight>
{
  public ScalarTypeJodaDateMidnight()
  {
    super(DateMidnight.class, false, 91);
  }
  
  public DateMidnight convertFromDate(java.sql.Date ts)
  {
    return new DateMidnight(ts.getTime());
  }
  
  public java.sql.Date convertToDate(DateMidnight t)
  {
    return new java.sql.Date(t.getMillis());
  }
  
  public Object toJdbcType(Object value)
  {
    if ((value instanceof DateMidnight)) {
      return new java.sql.Date(((DateMidnight)value).getMillis());
    }
    return BasicTypeConverter.toDate(value);
  }
  
  public DateMidnight toBeanType(Object value)
  {
    if ((value instanceof java.util.Date)) {
      return new DateMidnight(((java.util.Date)value).getTime());
    }
    return (DateMidnight)value;
  }
}
