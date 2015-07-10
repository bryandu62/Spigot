package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public class ScalarTypeLong
  extends ScalarTypeBase<Long>
{
  public ScalarTypeLong()
  {
    super(Long.class, true, -5);
  }
  
  public void bind(DataBind b, Long value)
    throws SQLException
  {
    if (value == null) {
      b.setNull(-5);
    } else {
      b.setLong(value.longValue());
    }
  }
  
  public Long read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getLong();
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toLong(value);
  }
  
  public Long toBeanType(Object value)
  {
    return BasicTypeConverter.toLong(value);
  }
  
  public String formatValue(Long t)
  {
    return t.toString();
  }
  
  public Long parse(String value)
  {
    return Long.valueOf(value);
  }
  
  public Long parseDateTime(long systemTimeMillis)
  {
    return Long.valueOf(systemTimeMillis);
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
    long val = dataInput.readLong();
    return Long.valueOf(val);
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    Long value = (Long)v;
    if (value == null)
    {
      dataOutput.writeBoolean(false);
    }
    else
    {
      dataOutput.writeBoolean(true);
      dataOutput.writeLong(value.longValue());
    }
  }
}
