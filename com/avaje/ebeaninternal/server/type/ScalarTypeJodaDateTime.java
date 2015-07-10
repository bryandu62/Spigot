package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.sql.Timestamp;
import java.util.Date;
import org.joda.time.DateTime;

public class ScalarTypeJodaDateTime
  extends ScalarTypeBaseDateTime<DateTime>
{
  public ScalarTypeJodaDateTime()
  {
    super(DateTime.class, false, 93);
  }
  
  public DateTime convertFromTimestamp(Timestamp ts)
  {
    return new DateTime(ts.getTime());
  }
  
  public Timestamp convertToTimestamp(DateTime t)
  {
    return new Timestamp(t.getMillis());
  }
  
  public Object toJdbcType(Object value)
  {
    if ((value instanceof DateTime)) {
      return new Timestamp(((DateTime)value).getMillis());
    }
    return BasicTypeConverter.toTimestamp(value);
  }
  
  public DateTime toBeanType(Object value)
  {
    if ((value instanceof Date)) {
      return new DateTime(((Date)value).getTime());
    }
    return (DateTime)value;
  }
}
