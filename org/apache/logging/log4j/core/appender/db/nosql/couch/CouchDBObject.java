package org.apache.logging.log4j.core.appender.db.nosql.couch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;

public final class CouchDBObject
  implements NoSQLObject<Map<String, Object>>
{
  private final Map<String, Object> map;
  
  public CouchDBObject()
  {
    this.map = new HashMap();
  }
  
  public void set(String field, Object value)
  {
    this.map.put(field, value);
  }
  
  public void set(String field, NoSQLObject<Map<String, Object>> value)
  {
    this.map.put(field, value.unwrap());
  }
  
  public void set(String field, Object[] values)
  {
    this.map.put(field, Arrays.asList(values));
  }
  
  public void set(String field, NoSQLObject<Map<String, Object>>[] values)
  {
    ArrayList<Map<String, Object>> list = new ArrayList();
    for (NoSQLObject<Map<String, Object>> value : values) {
      list.add(value.unwrap());
    }
    this.map.put(field, list);
  }
  
  public Map<String, Object> unwrap()
  {
    return this.map;
  }
}
