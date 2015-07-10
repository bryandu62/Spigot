package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class ScalarTypeBigDecimal
  extends ScalarTypeBase<BigDecimal>
{
  public ScalarTypeBigDecimal()
  {
    super(BigDecimal.class, true, 3);
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    if (!dataInput.readBoolean()) {
      return null;
    }
    double val = dataInput.readDouble();
    return new BigDecimal(val);
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    BigDecimal b = (BigDecimal)v;
    if (b == null)
    {
      dataOutput.writeBoolean(false);
    }
    else
    {
      dataOutput.writeBoolean(true);
      dataOutput.writeDouble(b.doubleValue());
    }
  }
  
  public void bind(DataBind b, BigDecimal value)
    throws SQLException
  {
    if (value == null) {
      b.setNull(3);
    } else {
      b.setBigDecimal(value);
    }
  }
  
  public BigDecimal read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getBigDecimal();
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toBigDecimal(value);
  }
  
  public BigDecimal toBeanType(Object value)
  {
    return BasicTypeConverter.toBigDecimal(value);
  }
  
  public String formatValue(BigDecimal t)
  {
    return t.toPlainString();
  }
  
  public BigDecimal parse(String value)
  {
    return new BigDecimal(value);
  }
  
  public BigDecimal parseDateTime(long systemTimeMillis)
  {
    return BigDecimal.valueOf(systemTimeMillis);
  }
  
  public boolean isDateTimeCapable()
  {
    return true;
  }
}
