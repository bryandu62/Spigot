package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ScalarTypeUtilDate
{
  public static class TimestampType
    extends ScalarTypeBaseDateTime<java.util.Date>
  {
    public TimestampType()
    {
      super(false, 93);
    }
    
    public java.util.Date read(DataReader dataReader)
      throws SQLException
    {
      Timestamp timestamp = dataReader.getTimestamp();
      if (timestamp == null) {
        return null;
      }
      return new java.util.Date(timestamp.getTime());
    }
    
    public void bind(DataBind b, java.util.Date value)
      throws SQLException
    {
      if (value == null)
      {
        b.setNull(93);
      }
      else
      {
        Timestamp timestamp = new Timestamp(value.getTime());
        b.setTimestamp(timestamp);
      }
    }
    
    public Object toJdbcType(Object value)
    {
      return BasicTypeConverter.toTimestamp(value);
    }
    
    public java.util.Date toBeanType(Object value)
    {
      return BasicTypeConverter.toUtilDate(value);
    }
    
    public java.util.Date convertFromTimestamp(Timestamp ts)
    {
      return new java.util.Date(ts.getTime());
    }
    
    public Timestamp convertToTimestamp(java.util.Date t)
    {
      return new Timestamp(t.getTime());
    }
    
    public java.util.Date parseDateTime(long systemTimeMillis)
    {
      return new java.util.Date(systemTimeMillis);
    }
    
    public Object luceneFromIndexValue(Object value)
    {
      Long l = (Long)value;
      return new java.util.Date(l.longValue());
    }
    
    public Object luceneToIndexValue(Object value)
    {
      return Long.valueOf(((java.util.Date)value).getTime());
    }
  }
  
  public static class DateType
    extends ScalarTypeBaseDate<java.util.Date>
  {
    public DateType()
    {
      super(false, 91);
    }
    
    public java.util.Date convertFromDate(java.sql.Date ts)
    {
      return new java.util.Date(ts.getTime());
    }
    
    public java.sql.Date convertToDate(java.util.Date t)
    {
      return new java.sql.Date(t.getTime());
    }
    
    public Object toJdbcType(Object value)
    {
      return BasicTypeConverter.toDate(value);
    }
    
    public java.util.Date toBeanType(Object value)
    {
      return BasicTypeConverter.toUtilDate(value);
    }
  }
}
