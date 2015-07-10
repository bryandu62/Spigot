package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.TextException;
import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public class ScalarTypeByte
  extends ScalarTypeBase<Byte>
{
  public ScalarTypeByte()
  {
    super(Byte.class, true, -6);
  }
  
  public void bind(DataBind b, Byte value)
    throws SQLException
  {
    if (value == null) {
      b.setNull(-6);
    } else {
      b.setByte(value.byteValue());
    }
  }
  
  public Byte read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getByte();
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toByte(value);
  }
  
  public Byte toBeanType(Object value)
  {
    return BasicTypeConverter.toByte(value);
  }
  
  public String formatValue(Byte t)
  {
    return t.toString();
  }
  
  public Byte parse(String value)
  {
    throw new TextException("Not supported");
  }
  
  public Byte parseDateTime(long systemTimeMillis)
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
    byte val = dataInput.readByte();
    return Byte.valueOf(val);
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    Byte val = (Byte)v;
    if (val == null)
    {
      dataOutput.writeBoolean(false);
    }
    else
    {
      dataOutput.writeBoolean(true);
      dataOutput.writeByte(val.byteValue());
    }
  }
}
