package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;

public class ScalarTypeJodaLocalDate
  extends ScalarTypeBaseDate<LocalDate>
{
  public ScalarTypeJodaLocalDate()
  {
    super(LocalDate.class, false, 91);
  }
  
  public LocalDate convertFromDate(java.sql.Date ts)
  {
    return new LocalDate(ts.getTime());
  }
  
  public java.sql.Date convertToDate(LocalDate t)
  {
    return new java.sql.Date(t.toDateMidnight().getMillis());
  }
  
  public Object toJdbcType(Object value)
  {
    if ((value instanceof LocalDate)) {
      return new java.sql.Date(((LocalDate)value).toDateMidnight().getMillis());
    }
    return BasicTypeConverter.toDate(value);
  }
  
  public LocalDate toBeanType(Object value)
  {
    if ((value instanceof java.util.Date)) {
      return new LocalDate(((java.util.Date)value).getTime());
    }
    return (LocalDate)value;
  }
  
  public LocalDate parseDateTime(long systemTimeMillis)
  {
    return new LocalDate(systemTimeMillis);
  }
}
