package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.SqlRow;
import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public class DefaultSqlRow
  implements SqlRow
{
  static final long serialVersionUID = -3120927797041336242L;
  private final String dbTrueValue;
  Map<String, Object> map;
  
  public DefaultSqlRow(Map<String, Object> map, String dbTrueValue)
  {
    this.map = map;
    this.dbTrueValue = dbTrueValue;
  }
  
  public DefaultSqlRow(String dbTrueValue)
  {
    this.map = new LinkedHashMap();
    this.dbTrueValue = dbTrueValue;
  }
  
  public DefaultSqlRow(int initialCapacity, float loadFactor, String dbTrueValue)
  {
    this.map = new LinkedHashMap(initialCapacity, loadFactor);
    this.dbTrueValue = dbTrueValue;
  }
  
  public Iterator<String> keys()
  {
    return this.map.keySet().iterator();
  }
  
  public Object remove(Object name)
  {
    name = ((String)name).toLowerCase();
    return this.map.remove(name);
  }
  
  public Object get(Object name)
  {
    name = ((String)name).toLowerCase();
    return this.map.get(name);
  }
  
  public Object put(String name, Object value)
  {
    return setInternal(name, value);
  }
  
  public Object set(String name, Object value)
  {
    return setInternal(name, value);
  }
  
  private Object setInternal(String name, Object newValue)
  {
    name = name.toLowerCase();
    
    return this.map.put(name, newValue);
  }
  
  public UUID getUUID(String name)
  {
    Object val = get(name);
    return BasicTypeConverter.toUUID(val);
  }
  
  public Boolean getBoolean(String name)
  {
    Object val = get(name);
    return BasicTypeConverter.toBoolean(val, this.dbTrueValue);
  }
  
  public Integer getInteger(String name)
  {
    Object val = get(name);
    return BasicTypeConverter.toInteger(val);
  }
  
  public BigDecimal getBigDecimal(String name)
  {
    Object val = get(name);
    return BasicTypeConverter.toBigDecimal(val);
  }
  
  public Long getLong(String name)
  {
    Object val = get(name);
    return BasicTypeConverter.toLong(val);
  }
  
  public Double getDouble(String name)
  {
    Object val = get(name);
    return BasicTypeConverter.toDouble(val);
  }
  
  public Float getFloat(String name)
  {
    Object val = get(name);
    return BasicTypeConverter.toFloat(val);
  }
  
  public String getString(String name)
  {
    Object val = get(name);
    return BasicTypeConverter.toString(val);
  }
  
  public java.util.Date getUtilDate(String name)
  {
    Object val = get(name);
    return BasicTypeConverter.toUtilDate(val);
  }
  
  public java.sql.Date getDate(String name)
  {
    Object val = get(name);
    return BasicTypeConverter.toDate(val);
  }
  
  public Timestamp getTimestamp(String name)
  {
    Object val = get(name);
    return BasicTypeConverter.toTimestamp(val);
  }
  
  public String toString()
  {
    return this.map.toString();
  }
  
  public void clear()
  {
    this.map.clear();
  }
  
  public boolean containsKey(Object key)
  {
    key = ((String)key).toLowerCase();
    return this.map.containsKey(key);
  }
  
  public boolean containsValue(Object value)
  {
    return this.map.containsValue(value);
  }
  
  public Set<Map.Entry<String, Object>> entrySet()
  {
    return this.map.entrySet();
  }
  
  public boolean isEmpty()
  {
    return this.map.isEmpty();
  }
  
  public Set<String> keySet()
  {
    return this.map.keySet();
  }
  
  public void putAll(Map<? extends String, ? extends Object> t)
  {
    this.map.putAll(t);
  }
  
  public int size()
  {
    return this.map.size();
  }
  
  public Collection<Object> values()
  {
    return this.map.values();
  }
}
