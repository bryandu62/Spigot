package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.sql.Timestamp;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

public class ScalarTypeJodaLocalDateTime
  extends ScalarTypeBaseDateTime<LocalDateTime>
{
  public ScalarTypeJodaLocalDateTime()
  {
    super(LocalDateTime.class, false, 93);
  }
  
  public LocalDateTime convertFromTimestamp(Timestamp ts)
  {
    return new LocalDateTime(ts.getTime());
  }
  
  public Timestamp convertToTimestamp(LocalDateTime t)
  {
    return new Timestamp(t.toDateTime().getMillis());
  }
  
  public Object toJdbcType(Object value)
  {
    if ((value instanceof LocalDateTime)) {
      return new Timestamp(((LocalDateTime)value).toDateTime().getMillis());
    }
    return BasicTypeConverter.toTimestamp(value);
  }
  
  public LocalDateTime toBeanType(Object value)
  {
    if ((value instanceof Date)) {
      return new LocalDateTime(((Date)value).getTime());
    }
    return (LocalDateTime)value;
  }
  
  public LocalDateTime parseDateTime(long systemTimeMillis)
  {
    return new LocalDateTime(systemTimeMillis);
  }
}
