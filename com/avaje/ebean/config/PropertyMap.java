package com.avaje.ebean.config;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

final class PropertyMap
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private LinkedHashMap<String, String> map = new LinkedHashMap();
  
  public String toString()
  {
    return this.map.toString();
  }
  
  public void evaluateProperties()
  {
    for (Map.Entry<String, String> e : entrySet())
    {
      String key = (String)e.getKey();
      String val = (String)e.getValue();
      String eval = eval(val);
      if ((eval != null) && (!eval.equals(val))) {
        put(key, eval);
      }
    }
  }
  
  public synchronized String eval(String val)
  {
    return PropertyExpression.eval(val, this);
  }
  
  public synchronized boolean getBoolean(String key, boolean defaultValue)
  {
    String value = get(key);
    if (value == null) {
      return defaultValue;
    }
    return Boolean.parseBoolean(value);
  }
  
  public synchronized int getInt(String key, int defaultValue)
  {
    String value = get(key);
    if (value == null) {
      return defaultValue;
    }
    return Integer.parseInt(value);
  }
  
  public synchronized String get(String key, String defaultValue)
  {
    String value = (String)this.map.get(key.toLowerCase());
    return value == null ? defaultValue : value;
  }
  
  public synchronized String get(String key)
  {
    return (String)this.map.get(key.toLowerCase());
  }
  
  synchronized void putAll(Map<String, String> keyValueMap)
  {
    Iterator<Map.Entry<String, String>> it = keyValueMap.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry<String, String> entry = (Map.Entry)it.next();
      put((String)entry.getKey(), (String)entry.getValue());
    }
  }
  
  synchronized String putEval(String key, String value)
  {
    value = PropertyExpression.eval(value, this);
    return (String)this.map.put(key.toLowerCase(), value);
  }
  
  synchronized String put(String key, String value)
  {
    return (String)this.map.put(key.toLowerCase(), value);
  }
  
  synchronized String remove(String key)
  {
    return (String)this.map.remove(key.toLowerCase());
  }
  
  synchronized Set<Map.Entry<String, String>> entrySet()
  {
    return this.map.entrySet();
  }
}
