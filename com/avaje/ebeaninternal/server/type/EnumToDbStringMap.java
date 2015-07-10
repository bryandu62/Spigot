package com.avaje.ebeaninternal.server.type;

import java.sql.SQLException;

public class EnumToDbStringMap
  extends EnumToDbValueMap<String>
{
  public int getDbType()
  {
    return 12;
  }
  
  public EnumToDbStringMap add(Object beanValue, String dbValue)
  {
    addInternal(beanValue, dbValue);
    return this;
  }
  
  public void bind(DataBind b, Object value)
    throws SQLException
  {
    if (value == null)
    {
      b.setNull(12);
    }
    else
    {
      String s = (String)getDbValue(value);
      b.setString(s);
    }
  }
  
  public Object read(DataReader dataReader)
    throws SQLException
  {
    String s = dataReader.getString();
    if (s == null) {
      return null;
    }
    return getBeanValue(s);
  }
}
