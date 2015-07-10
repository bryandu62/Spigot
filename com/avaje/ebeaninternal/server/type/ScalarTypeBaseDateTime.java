package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.text.json.WriteJsonBuffer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public abstract class ScalarTypeBaseDateTime<T>
  extends ScalarTypeBase<T>
{
  public ScalarTypeBaseDateTime(Class<T> type, boolean jdbcNative, int jdbcType)
  {
    super(type, jdbcNative, jdbcType);
  }
  
  public abstract Timestamp convertToTimestamp(T paramT);
  
  public abstract T convertFromTimestamp(Timestamp paramTimestamp);
  
  public void bind(DataBind b, T value)
    throws SQLException
  {
    if (value == null)
    {
      b.setNull(93);
    }
    else
    {
      Timestamp ts = convertToTimestamp(value);
      b.setTimestamp(ts);
    }
  }
  
  public T read(DataReader dataReader)
    throws SQLException
  {
    Timestamp ts = dataReader.getTimestamp();
    if (ts == null) {
      return null;
    }
    return (T)convertFromTimestamp(ts);
  }
  
  public String formatValue(T t)
  {
    Timestamp ts = convertToTimestamp(t);
    return ts.toString();
  }
  
  public T parse(String value)
  {
    Timestamp ts = Timestamp.valueOf(value);
    return (T)convertFromTimestamp(ts);
  }
  
  public T parseDateTime(long systemTimeMillis)
  {
    Timestamp ts = new Timestamp(systemTimeMillis);
    return (T)convertFromTimestamp(ts);
  }
  
  public boolean isDateTimeCapable()
  {
    return true;
  }
  
  public void jsonWrite(WriteJsonBuffer buffer, T value, JsonValueAdapter ctx)
  {
    String v = jsonToString(value, ctx);
    buffer.append(v);
  }
  
  public String jsonToString(T value, JsonValueAdapter ctx)
  {
    Timestamp ts = convertToTimestamp(value);
    return ctx.jsonFromTimestamp(ts);
  }
  
  public T jsonFromString(String value, JsonValueAdapter ctx)
  {
    Timestamp ts = ctx.jsonToTimestamp(value);
    return (T)convertFromTimestamp(ts);
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    if (!dataInput.readBoolean()) {
      return null;
    }
    long val = dataInput.readLong();
    Timestamp ts = new Timestamp(val);
    return convertFromTimestamp(ts);
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
      Timestamp ts = convertToTimestamp(value);
      dataOutput.writeLong(ts.getTime());
    }
  }
}
