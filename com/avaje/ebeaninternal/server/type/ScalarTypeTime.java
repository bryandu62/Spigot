package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;

public class ScalarTypeTime
  extends ScalarTypeBase<Time>
{
  public ScalarTypeTime()
  {
    super(Time.class, true, 92);
  }
  
  public void bind(DataBind b, Time value)
    throws SQLException
  {
    if (value == null) {
      b.setNull(92);
    } else {
      b.setTime(value);
    }
  }
  
  public Time read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getTime();
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toTime(value);
  }
  
  public Time toBeanType(Object value)
  {
    return BasicTypeConverter.toTime(value);
  }
  
  public String formatValue(Time v)
  {
    return v.toString();
  }
  
  public Time parse(String value)
  {
    return Time.valueOf(value);
  }
  
  public Time parseDateTime(long systemTimeMillis)
  {
    return new Time(systemTimeMillis);
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
