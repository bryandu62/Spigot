package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import com.avaje.ebeaninternal.server.text.json.WriteJsonBuffer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public class ScalarTypeString
  extends ScalarTypeBase<String>
{
  public ScalarTypeString()
  {
    super(String.class, true, 12);
  }
  
  public void bind(DataBind b, String value)
    throws SQLException
  {
    if (value == null) {
      b.setNull(12);
    } else {
      b.setString(value);
    }
  }
  
  public String read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getString();
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toString(value);
  }
  
  public String toBeanType(Object value)
  {
    return BasicTypeConverter.toString(value);
  }
  
  public String formatValue(String t)
  {
    return t;
  }
  
  public String parse(String value)
  {
    return value;
  }
  
  public String parseDateTime(long systemTimeMillis)
  {
    return String.valueOf(systemTimeMillis);
  }
  
  public boolean isDateTimeCapable()
  {
    return true;
  }
  
  public void jsonWrite(WriteJsonBuffer buffer, String value, JsonValueAdapter ctx)
  {
    String s = format(value);
    EscapeJson.escapeQuote(s, buffer);
  }
  
  public String jsonFromString(String value, JsonValueAdapter ctx)
  {
    return value;
  }
  
  public String jsonToString(String value, JsonValueAdapter ctx)
  {
    return EscapeJson.escapeQuote(value);
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    if (!dataInput.readBoolean()) {
      return null;
    }
    return dataInput.readUTF();
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    String value = (String)v;
    if (value == null)
    {
      dataOutput.writeBoolean(false);
    }
    else
    {
      dataOutput.writeBoolean(true);
      dataOutput.writeUTF(value);
    }
  }
}
