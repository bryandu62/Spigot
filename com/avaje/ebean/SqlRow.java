package com.avaje.ebean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public abstract interface SqlRow
  extends Serializable, Map<String, Object>
{
  public abstract Iterator<String> keys();
  
  public abstract Object remove(Object paramObject);
  
  public abstract Object get(Object paramObject);
  
  public abstract Object put(String paramString, Object paramObject);
  
  public abstract Object set(String paramString, Object paramObject);
  
  public abstract Boolean getBoolean(String paramString);
  
  public abstract UUID getUUID(String paramString);
  
  public abstract Integer getInteger(String paramString);
  
  public abstract BigDecimal getBigDecimal(String paramString);
  
  public abstract Long getLong(String paramString);
  
  public abstract Double getDouble(String paramString);
  
  public abstract Float getFloat(String paramString);
  
  public abstract String getString(String paramString);
  
  public abstract java.util.Date getUtilDate(String paramString);
  
  public abstract java.sql.Date getDate(String paramString);
  
  public abstract Timestamp getTimestamp(String paramString);
  
  public abstract String toString();
  
  public abstract void clear();
  
  public abstract boolean containsKey(Object paramObject);
  
  public abstract boolean containsValue(Object paramObject);
  
  public abstract Set<Map.Entry<String, Object>> entrySet();
  
  public abstract boolean isEmpty();
  
  public abstract Set<String> keySet();
  
  public abstract void putAll(Map<? extends String, ? extends Object> paramMap);
  
  public abstract int size();
  
  public abstract Collection<Object> values();
}
