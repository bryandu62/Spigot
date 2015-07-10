package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.TextException;
import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public class ScalarTypeShort
  extends ScalarTypeBase<Short>
{
  public ScalarTypeShort()
  {
    super(Short.class, true, 5);
  }
  
  public void bind(DataBind b, Short value)
    throws SQLException
  {
    if (value == null) {
      b.setNull(5);
    } else {
      b.setShort(value.shortValue());
    }
  }
  
  public Short read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getShort();
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toShort(value);
  }
  
  public Short toBeanType(Object value)
  {
    return BasicTypeConverter.toShort(value);
  }
  
  public String formatValue(Short v)
  {
    return v.toString();
  }
  
  public Short parse(String value)
  {
    return Short.valueOf(value);
  }
  
  public Short parseDateTime(long systemTimeMillis)
  {
    throw new TextException("Not Supported");
  }
  
  public boolean isDateTimeCapable()
  {
    return false;
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    if (!dataInput.readBoolean()) {
      return null;
    }
    short val = dataInput.readShort();
    return Short.valueOf(val);
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    Short value = (Short)v;
    if (value == null)
    {
      dataOutput.writeBoolean(false);
    }
    else
    {
      dataOutput.writeBoolean(true);
      dataOutput.writeShort(value.shortValue());
    }
  }
}
