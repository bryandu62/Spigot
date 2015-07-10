package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.text.json.WriteJsonBuffer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public abstract class ScalarTypeBaseDate<T>
  extends ScalarTypeBase<T>
{
  public ScalarTypeBaseDate(Class<T> type, boolean jdbcNative, int jdbcType)
  {
    super(type, jdbcNative, jdbcType);
  }
  
  public abstract Date convertToDate(T paramT);
  
  public abstract T convertFromDate(Date paramDate);
  
  public void bind(DataBind b, T value)
    throws SQLException
  {
    if (value == null)
    {
      b.setNull(91);
    }
    else
    {
      Date date = convertToDate(value);
      b.setDate(date);
    }
  }
  
  public T read(DataReader dataReader)
    throws SQLException
  {
    Date ts = dataReader.getDate();
    if (ts == null) {
      return null;
    }
    return (T)convertFromDate(ts);
  }
  
  public String formatValue(T t)
  {
    Date date = convertToDate(t);
    return date.toString();
  }
  
  public T parse(String value)
  {
    Date date = Date.valueOf(value);
    return (T)convertFromDate(date);
  }
  
  public T parseDateTime(long systemTimeMillis)
  {
    Date ts = new Date(systemTimeMillis);
    return (T)convertFromDate(ts);
  }
  
  public boolean isDateTimeCapable()
  {
    return true;
  }
  
  public String jsonToString(T value, JsonValueAdapter ctx)
  {
    Date date = convertToDate(value);
    return ctx.jsonFromDate(date);
  }
  
  public void jsonWrite(WriteJsonBuffer buffer, T value, JsonValueAdapter ctx)
  {
    String s = jsonToString(value, ctx);
    buffer.append(s);
  }
  
  public T jsonFromString(String value, JsonValueAdapter ctx)
  {
    Date ts = ctx.jsonToDate(value);
    return (T)convertFromDate(ts);
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    if (!dataInput.readBoolean()) {
      return null;
    }
    long val = dataInput.readLong();
    Date date = new Date(val);
    return convertFromDate(date);
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
      Date date = convertToDate(value);
      dataOutput.writeLong(date.getTime());
    }
  }
}
