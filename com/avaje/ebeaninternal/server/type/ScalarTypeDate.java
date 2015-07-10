package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.sql.Date;
import java.sql.SQLException;

public class ScalarTypeDate
  extends ScalarTypeBaseDate<Date>
{
  public ScalarTypeDate()
  {
    super(Date.class, true, 91);
  }
  
  public Date convertFromDate(Date date)
  {
    return date;
  }
  
  public Date convertToDate(Date t)
  {
    return t;
  }
  
  public void bind(DataBind b, Date value)
    throws SQLException
  {
    if (value == null) {
      b.setNull(91);
    } else {
      b.setDate(value);
    }
  }
  
  public Date read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getDate();
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.toDate(value);
  }
  
  public Date toBeanType(Object value)
  {
    return BasicTypeConverter.toDate(value);
  }
}
