package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

public class ScalarTypeJodaLocalTime
  extends ScalarTypeBase<LocalTime>
{
  public ScalarTypeJodaLocalTime()
  {
    super(LocalTime.class, false, 92);
  }
  
  public void bind(DataBind b, LocalTime value)
    throws SQLException
  {
    if (value == null)
    {
      b.setNull(92);
    }
    else
    {
      Time sqlTime = new Time(value.getMillisOfDay());
      b.setTime(sqlTime);
    }
  }
  
  public LocalTime read(DataReader dataReader)
    throws SQLException
  {
    Time sqlTime = dataReader.getTime();
    if (sqlTime == null) {
      return null;
    }
    return new LocalTime(sqlTime, DateTimeZone.UTC);
  }
  
  public Object toJdbcType(Object value)
  {
    if ((value instanceof LocalTime)) {
      return new Time(((LocalTime)value).getMillisOfDay());
    }
    return BasicTypeConverter.toTime(value);
  }
  
  public LocalTime toBeanType(Object value)
  {
    if ((value instanceof Date)) {
      return new LocalTime(value, DateTimeZone.UTC);
    }
    return (LocalTime)value;
  }
  
  public String formatValue(LocalTime v)
  {
    return v.toString();
  }
  
  public LocalTime parse(String value)
  {
    return new LocalTime(value);
  }
  
  public LocalTime parseDateTime(long systemTimeMillis)
  {
    return new LocalTime(systemTimeMillis);
  }
  
  public boolean isDateTimeCapable()
  {
    return true;
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    if (!dataInput.readBoolean()) {
      return null;
    }
    String val = dataInput.readUTF();
    return parse(val);
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    Time value = (Time)v;
    if (value == null)
    {
      dataOutput.writeBoolean(false);
    }
    else
    {
      dataOutput.writeBoolean(true);
      dataOutput.writeUTF(format(value));
    }
  }
}
