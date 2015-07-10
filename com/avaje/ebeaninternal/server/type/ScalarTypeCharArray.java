package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.sql.SQLException;

public class ScalarTypeCharArray
  extends ScalarTypeBaseVarchar<char[]>
{
  public ScalarTypeCharArray()
  {
    super(char[].class, false, 12);
  }
  
  public char[] convertFromDbString(String dbValue)
  {
    return dbValue.toCharArray();
  }
  
  public String convertToDbString(char[] beanValue)
  {
    return new String(beanValue);
  }
  
  public void bind(DataBind b, char[] value)
    throws SQLException
  {
    if (value == null)
    {
      b.setNull(12);
    }
    else
    {
      String s = BasicTypeConverter.toString(value);
      b.setString(s);
    }
  }
  
  public char[] read(DataReader dataReader)
    throws SQLException
  {
    String string = dataReader.getString();
    if (string == null) {
      return null;
    }
    return string.toCharArray();
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toString(value);
  }
  
  public char[] toBeanType(Object value)
  {
    String s = BasicTypeConverter.toString(value);
    return s.toCharArray();
  }
  
  public String formatValue(char[] t)
  {
    return String.valueOf(t);
  }
  
  public char[] parse(String value)
  {
    return value.toCharArray();
  }
  
  public char[] jsonFromString(String value, JsonValueAdapter ctx)
  {
    return value.toCharArray();
  }
  
  public String jsonToString(char[] value, JsonValueAdapter ctx)
  {
    return EscapeJson.escapeQuote(String.valueOf(value));
  }
}
