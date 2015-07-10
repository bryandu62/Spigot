package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public class ScalarTypeDouble
  extends ScalarTypeBase<Double>
{
  public ScalarTypeDouble()
  {
    super(Double.class, true, 8);
  }
  
  public void bind(DataBind b, Double value)
    throws SQLException
  {
    if (value == null) {
      b.setNull(8);
    } else {
      b.setDouble(value.doubleValue());
    }
  }
  
  public Double read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getDouble();
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toDouble(value);
  }
  
  public Double toBeanType(Object value)
  {
    return BasicTypeConverter.toDouble(value);
  }
  
  public String formatValue(Double t)
  {
    return t.toString();
  }
  
  public Double parse(String value)
  {
    return Double.valueOf(value);
  }
  
  public Double parseDateTime(long systemTimeMillis)
  {
    return Double.valueOf(systemTimeMillis);
  }
  
  public boolean isDateTimeCapable()
  {
    return true;
  }
  
  public String toJsonString(Double value)
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
    double val = dataInput.readDouble();
    return Double.valueOf(val);
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    Double value = (Double)v;
    if (value == null)
    {
      dataOutput.writeBoolean(false);
    }
    else
    {
      dataOutput.writeBoolean(true);
      dataOutput.writeDouble(value.doubleValue());
    }
  }
}
