package com.avaje.ebeaninternal.server.text.json;

import com.avaje.ebean.text.json.JsonValueAdapter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DefaultJsonValueAdapter
  implements JsonValueAdapter
{
  private final SimpleDateFormat dateTimeProto;
  
  public DefaultJsonValueAdapter(String dateTimeFormat)
  {
    this.dateTimeProto = new SimpleDateFormat(dateTimeFormat);
    this.dateTimeProto.setTimeZone(TimeZone.getTimeZone("UTC"));
  }
  
  public DefaultJsonValueAdapter()
  {
    this("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  }
  
  private SimpleDateFormat dtFormat()
  {
    return (SimpleDateFormat)this.dateTimeProto.clone();
  }
  
  public String jsonFromDate(java.sql.Date date)
  {
    return "\"" + date.toString() + "\"";
  }
  
  public String jsonFromTimestamp(Timestamp date)
  {
    return "\"" + dtFormat().format(date) + "\"";
  }
  
  public java.sql.Date jsonToDate(String jsonDate)
  {
    return java.sql.Date.valueOf(jsonDate);
  }
  
  public Timestamp jsonToTimestamp(String jsonDateTime)
  {
    try
    {
      java.util.Date d = dtFormat().parse(jsonDateTime);
      return new Timestamp(d.getTime());
    }
    catch (Exception e)
    {
      String m = "Error parsing Datetime[" + jsonDateTime + "]";
      throw new RuntimeException(m, e);
    }
  }
}
