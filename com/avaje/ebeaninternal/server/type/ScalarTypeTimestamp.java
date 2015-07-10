package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ScalarTypeTimestamp
  extends ScalarTypeBaseDateTime<Timestamp>
{
  public ScalarTypeTimestamp()
  {
    super(Timestamp.class, true, 93);
  }
  
  public Timestamp convertFromTimestamp(Timestamp ts)
  {
    return ts;
  }
  
  public Timestamp convertToTimestamp(Timestamp t)
  {
    return t;
  }
  
  public void bind(DataBind b, Timestamp value)
    throws SQLException
  {
    if (value == null) {
      b.setNull(93);
    } else {
      b.setTimestamp(value);
    }
  }
  
  public Timestamp read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getTimestamp();
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toTimestamp(value);
  }
  
  public Timestamp toBeanType(Object value)
  {
    return BasicTypeConverter.toTimestamp(value);
  }
}
