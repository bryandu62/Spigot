package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public class ScalarTypeFloat
  extends ScalarTypeBase<Float>
{
  public ScalarTypeFloat()
  {
    super(Float.class, true, 7);
  }
  
  public void bind(DataBind b, Float value)
    throws SQLException
  {
    if (value == null) {
      b.setNull(7);
    } else {
      b.setFloat(value.floatValue());
    }
  }
  
  public Float read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getFloat();
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toFloat(value);
  }
  
  public Float toBeanType(Object value)
  {
    return BasicTypeConverter.toFloat(value);
  }
  
  public String formatValue(Float t)
  {
    return t.toString();
  }
  
  public Float parse(String value)
  {
    return Float.valueOf(value);
  }
  
  public Float parseDateTime(long systemTimeMillis)
  {
    return Float.valueOf((float)systemTimeMillis);
  }
  
  public boolean isDateTimeCapable()
  {
    return true;
  }
  
  public String toJsonString(Float value)
  {
    if ((value.isInfinite()) || (value.isNaN())) {
      return "null";
    }
    return value.toString();
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    if (!dataInput.readBoolean()) {
      return null;
    }
    float val = dataInput.readFloat();
    return Float.valueOf(val);
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    Float value = (Float)v;
    if (value == null)
    {
      dataOutput.writeBoolean(false);
    }
    else
    {
      dataOutput.writeBoolean(true);
      dataOutput.writeFloat(value.floatValue());
    }
  }
}
