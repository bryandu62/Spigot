package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.TextException;
import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public class ScalarTypeBoolean
{
  public static class Native
    extends ScalarTypeBoolean.BooleanBase
  {
    public Native()
    {
      super(16);
    }
    
    public Boolean toBeanType(Object value)
    {
      return BasicTypeConverter.toBoolean(value);
    }
    
    public Object toJdbcType(Object value)
    {
      return BasicTypeConverter.convert(value, this.jdbcType);
    }
    
    public void bind(DataBind b, Boolean value)
      throws SQLException
    {
      if (value == null) {
        b.setNull(16);
      } else {
        b.setBoolean(value.booleanValue());
      }
    }
    
    public Boolean read(DataReader dataReader)
      throws SQLException
    {
      return dataReader.getBoolean();
    }
  }
  
  public static class BitBoolean
    extends ScalarTypeBoolean.BooleanBase
  {
    public BitBoolean()
    {
      super(-7);
    }
    
    public Boolean toBeanType(Object value)
    {
      return BasicTypeConverter.toBoolean(value);
    }
    
    public Object toJdbcType(Object value)
    {
      return BasicTypeConverter.toBoolean(value);
    }
    
    public void bind(DataBind b, Boolean value)
      throws SQLException
    {
      if (value == null) {
        b.setNull(-7);
      } else {
        b.setBoolean(value.booleanValue());
      }
    }
    
    public Boolean read(DataReader dataReader)
      throws SQLException
    {
      return dataReader.getBoolean();
    }
  }
  
  public static class IntBoolean
    extends ScalarTypeBoolean.BooleanBase
  {
    private final Integer trueValue;
    private final Integer falseValue;
    
    public IntBoolean(Integer trueValue, Integer falseValue)
    {
      super(4);
      this.trueValue = trueValue;
      this.falseValue = falseValue;
    }
    
    public int getLength()
    {
      return 1;
    }
    
    public void bind(DataBind b, Boolean value)
      throws SQLException
    {
      if (value == null) {
        b.setNull(4);
      } else {
        b.setInt(toInteger(value).intValue());
      }
    }
    
    public Boolean read(DataReader dataReader)
      throws SQLException
    {
      Integer i = dataReader.getInt();
      if (i == null) {
        return null;
      }
      if (i.equals(this.trueValue)) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
    
    public Object toJdbcType(Object value)
    {
      return toInteger(value);
    }
    
    public Integer toInteger(Object value)
    {
      if (value == null) {
        return null;
      }
      Boolean b = (Boolean)value;
      if (b.booleanValue()) {
        return this.trueValue;
      }
      return this.falseValue;
    }
    
    public Boolean toBeanType(Object value)
    {
      if (value == null) {
        return null;
      }
      if ((value instanceof Boolean)) {
        return (Boolean)value;
      }
      if (this.trueValue.equals(value)) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
  }
  
  public static class StringBoolean
    extends ScalarTypeBoolean.BooleanBase
  {
    private final String trueValue;
    private final String falseValue;
    
    public StringBoolean(String trueValue, String falseValue)
    {
      super(12);
      this.trueValue = trueValue;
      this.falseValue = falseValue;
    }
    
    public int getLength()
    {
      return Math.max(this.trueValue.length(), this.falseValue.length());
    }
    
    public void bind(DataBind b, Boolean value)
      throws SQLException
    {
      if (value == null) {
        b.setNull(12);
      } else {
        b.setString(toString(value));
      }
    }
    
    public Boolean read(DataReader dataReader)
      throws SQLException
    {
      String string = dataReader.getString();
      if (string == null) {
        return null;
      }
      if (string.equals(this.trueValue)) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
    
    public Object toJdbcType(Object value)
    {
      return toString(value);
    }
    
    public String toString(Object value)
    {
      if (value == null) {
        return null;
      }
      Boolean b = (Boolean)value;
      if (b.booleanValue()) {
        return this.trueValue;
      }
      return this.falseValue;
    }
    
    public Boolean toBeanType(Object value)
    {
      if (value == null) {
        return null;
      }
      if ((value instanceof Boolean)) {
        return (Boolean)value;
      }
      if (this.trueValue.equals(value)) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
  }
  
  public static abstract class BooleanBase
    extends ScalarTypeBase<Boolean>
  {
    public BooleanBase(boolean jdbcNative, int jdbcType)
    {
      super(jdbcNative, jdbcType);
    }
    
    public String formatValue(Boolean t)
    {
      return t.toString();
    }
    
    public Boolean parse(String value)
    {
      return Boolean.valueOf(value);
    }
    
    public Boolean parseDateTime(long systemTimeMillis)
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
      boolean val = dataInput.readBoolean();
      return Boolean.valueOf(val);
    }
    
    public void writeData(DataOutput dataOutput, Object v)
      throws IOException
    {
      Boolean val = (Boolean)v;
      if (val == null)
      {
        dataOutput.writeBoolean(false);
      }
      else
      {
        dataOutput.writeBoolean(true);
        dataOutput.writeBoolean(val.booleanValue());
      }
    }
  }
}
