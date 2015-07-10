package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.TextException;
import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.text.json.WriteJsonBuffer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public abstract class ScalarTypeBaseVarchar<T>
  extends ScalarTypeBase<T>
{
  public ScalarTypeBaseVarchar(Class<T> type)
  {
    super(type, false, 12);
  }
  
  public ScalarTypeBaseVarchar(Class<T> type, boolean jdbcNative, int jdbcType)
  {
    super(type, jdbcNative, jdbcType);
  }
  
  public abstract String formatValue(T paramT);
  
  public abstract T parse(String paramString);
  
  public abstract T convertFromDbString(String paramString);
  
  public abstract String convertToDbString(T paramT);
  
  public void bind(DataBind b, T value)
    throws SQLException
  {
    if (value == null)
    {
      b.setNull(12);
    }
    else
    {
      String s = convertToDbString(value);
      b.setString(s);
    }
  }
  
  public T read(DataReader dataReader)
    throws SQLException
  {
    String s = dataReader.getString();
    if (s == null) {
      return null;
    }
    return (T)convertFromDbString(s);
  }
  
  public T toBeanType(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof String)) {
      return (T)parse((String)value);
    }
    return (T)value;
  }
  
  public Object toJdbcType(Object value)
  {
    if ((value instanceof String)) {
      return parse((String)value);
    }
    return value;
  }
  
  public T parseDateTime(long systemTimeMillis)
  {
    throw new TextException("Not Supported");
  }
  
  public boolean isDateTimeCapable()
  {
    return false;
  }
  
  public String format(Object v)
  {
    return formatValue(v);
  }
  
  public T jsonFromString(String value, JsonValueAdapter ctx)
  {
    return (T)parse(value);
  }
  
  public void jsonWrite(WriteJsonBuffer buffer, T value, JsonValueAdapter ctx)
  {
    String s = format(value);
    EscapeJson.escapeQuote(s, buffer);
  }
  
  public String toJsonString(Object value, JsonValueAdapter ctx)
  {
    String s = format(value);
    return EscapeJson.escapeQuote(s);
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    if (!dataInput.readBoolean()) {
      return null;
    }
    String val = dataInput.readUTF();
    return convertFromDbString(val);
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    T value = (T)v;
    if (value == null)
    {
      dataOutput.writeBoolean(false);
    }
    else
    {
      dataOutput.writeBoolean(true);
      String s = convertToDbString(value);
      dataOutput.writeUTF(s);
    }
  }
}
