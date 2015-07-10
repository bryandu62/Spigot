package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.sql.SQLException;

public class ScalarTypeClob
  extends ScalarTypeBaseVarchar<String>
{
  static final int clobBufferSize = 512;
  static final int stringInitialSize = 512;
  
  protected ScalarTypeClob(boolean jdbcNative, int jdbcType)
  {
    super(String.class, jdbcNative, jdbcType);
  }
  
  public ScalarTypeClob()
  {
    super(String.class, true, 2005);
  }
  
  public String convertFromDbString(String dbValue)
  {
    return dbValue;
  }
  
  public String convertToDbString(String beanValue)
  {
    return beanValue;
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
    return dataReader.getStringClob();
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
  
  public String jsonFromString(String value, JsonValueAdapter ctx)
  {
    return value;
  }
  
  public String jsonToString(String value, JsonValueAdapter ctx)
  {
    return EscapeJson.escapeQuote(value);
  }
}
