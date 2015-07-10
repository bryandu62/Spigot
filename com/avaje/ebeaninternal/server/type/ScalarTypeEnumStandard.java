package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.TextException;
import com.avaje.ebean.text.json.JsonValueAdapter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;
import java.util.EnumSet;

public class ScalarTypeEnumStandard
{
  public static class StringEnum
    extends ScalarTypeEnumStandard.EnumBase
    implements ScalarTypeEnum
  {
    private final int length;
    
    public StringEnum(Class enumType)
    {
      super(false, 12);
      this.length = maxValueLength(enumType);
    }
    
    public String getContraintInValues()
    {
      StringBuilder sb = new StringBuilder();
      
      sb.append("(");
      Object[] ea = this.enumType.getEnumConstants();
      for (int i = 0; i < ea.length; i++)
      {
        Enum<?> e = (Enum)ea[i];
        if (i > 0) {
          sb.append(",");
        }
        sb.append("'").append(e.toString()).append("'");
      }
      sb.append(")");
      
      return sb.toString();
    }
    
    private int maxValueLength(Class<?> enumType)
    {
      int maxLen = 0;
      
      Object[] ea = enumType.getEnumConstants();
      for (int i = 0; i < ea.length; i++)
      {
        Enum<?> e = (Enum)ea[i];
        maxLen = Math.max(maxLen, e.toString().length());
      }
      return maxLen;
    }
    
    public int getLength()
    {
      return this.length;
    }
    
    public void bind(DataBind b, Object value)
      throws SQLException
    {
      if (value == null) {
        b.setNull(12);
      } else {
        b.setString(value.toString());
      }
    }
    
    public Object read(DataReader dataReader)
      throws SQLException
    {
      String string = dataReader.getString();
      if (string == null) {
        return null;
      }
      return Enum.valueOf(this.enumType, string);
    }
    
    public Object toJdbcType(Object beanValue)
    {
      if (beanValue == null) {
        return null;
      }
      Enum<?> e = (Enum)beanValue;
      return e.toString();
    }
    
    public Object toBeanType(Object dbValue)
    {
      if (dbValue == null) {
        return null;
      }
      return Enum.valueOf(this.enumType, (String)dbValue);
    }
  }
  
  public static class OrdinalEnum
    extends ScalarTypeEnumStandard.EnumBase
    implements ScalarTypeEnum
  {
    private final Object[] enumArray;
    
    public OrdinalEnum(Class enumType)
    {
      super(false, 4);
      this.enumArray = EnumSet.allOf(enumType).toArray();
    }
    
    public String getContraintInValues()
    {
      StringBuilder sb = new StringBuilder();
      
      sb.append("(");
      for (int i = 0; i < this.enumArray.length; i++)
      {
        Enum<?> e = (Enum)this.enumArray[i];
        if (i > 0) {
          sb.append(",");
        }
        sb.append(e.ordinal());
      }
      sb.append(")");
      
      return sb.toString();
    }
    
    public void bind(DataBind b, Object value)
      throws SQLException
    {
      if (value == null)
      {
        b.setNull(4);
      }
      else
      {
        Enum<?> e = (Enum)value;
        b.setInt(e.ordinal());
      }
    }
    
    public Object read(DataReader dataReader)
      throws SQLException
    {
      Integer ordinal = dataReader.getInt();
      if (ordinal == null) {
        return null;
      }
      if ((ordinal.intValue() < 0) || (ordinal.intValue() >= this.enumArray.length))
      {
        String m = "Unexpected ordinal [" + ordinal + "] out of range [" + this.enumArray.length + "]";
        throw new IllegalStateException(m);
      }
      return this.enumArray[ordinal.intValue()];
    }
    
    public Object toJdbcType(Object beanValue)
    {
      if (beanValue == null) {
        return null;
      }
      Enum e = (Enum)beanValue;
      return Integer.valueOf(e.ordinal());
    }
    
    public Object toBeanType(Object dbValue)
    {
      if (dbValue == null) {
        return null;
      }
      int ordinal = ((Integer)dbValue).intValue();
      if ((ordinal < 0) || (ordinal >= this.enumArray.length))
      {
        String m = "Unexpected ordinal [" + ordinal + "] out of range [" + this.enumArray.length + "]";
        throw new IllegalStateException(m);
      }
      return this.enumArray[ordinal];
    }
  }
  
  public static abstract class EnumBase
    extends ScalarTypeBase
  {
    protected final Class enumType;
    
    public EnumBase(Class<?> type, boolean jdbcNative, int jdbcType)
    {
      super(jdbcNative, jdbcType);
      this.enumType = type;
    }
    
    public String format(Object t)
    {
      return t.toString();
    }
    
    public String formatValue(Object t)
    {
      return t.toString();
    }
    
    public Object parse(String value)
    {
      return Enum.valueOf(this.enumType, value);
    }
    
    public Object parseDateTime(long systemTimeMillis)
    {
      throw new TextException("Not Supported");
    }
    
    public boolean isDateTimeCapable()
    {
      return false;
    }
    
    public Object jsonFromString(String value, JsonValueAdapter ctx)
    {
      return parse(value);
    }
    
    public String jsonToString(Object value, JsonValueAdapter ctx)
    {
      return EscapeJson.escapeQuote(value.toString());
    }
    
    public Object readData(DataInput dataInput)
      throws IOException
    {
      if (!dataInput.readBoolean()) {
        return null;
      }
      String s = dataInput.readUTF();
      return parse(s);
    }
    
    public void writeData(DataOutput dataOutput, Object v)
      throws IOException
    {
      if (v == null)
      {
        dataOutput.writeBoolean(false);
      }
      else
      {
        dataOutput.writeBoolean(true);
        dataOutput.writeUTF(format(v));
      }
    }
  }
}
