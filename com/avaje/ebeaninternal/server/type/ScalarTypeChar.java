package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.sql.SQLException;

public class ScalarTypeChar
  extends ScalarTypeBaseVarchar<Character>
{
  public ScalarTypeChar()
  {
    super(Character.TYPE, false, 12);
  }
  
  public Character convertFromDbString(String dbValue)
  {
    return Character.valueOf(dbValue.charAt(0));
  }
  
  public String convertToDbString(Character beanValue)
  {
    return beanValue.toString();
  }
  
  public void bind(DataBind b, Character value)
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
  
  public Character read(DataReader dataReader)
    throws SQLException
  {
    String string = dataReader.getString();
    if ((string == null) || (string.length() == 0)) {
      return null;
    }
    return Character.valueOf(string.charAt(0));
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toString(value);
  }
  
  public Character toBeanType(Object value)
  {
    String s = BasicTypeConverter.toString(value);
    return Character.valueOf(s.charAt(0));
  }
  
  public String formatValue(Character t)
  {
    return t.toString();
  }
  
  public Character parse(String value)
  {
    return Character.valueOf(value.charAt(0));
  }
  
  public Character jsonFromString(String value, JsonValueAdapter ctx)
  {
    return Character.valueOf(value.charAt(0));
  }
  
  public String jsonToString(Character value, JsonValueAdapter ctx)
  {
    return EscapeJson.escapeQuote(value.toString());
  }
}
