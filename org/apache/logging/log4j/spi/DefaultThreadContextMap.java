package org.apache.logging.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultThreadContextMap
  implements ThreadContextMap
{
  private final boolean useMap;
  private final ThreadLocal<Map<String, String>> localMap = new InheritableThreadLocal()
  {
    protected Map<String, String> childValue(Map<String, String> parentValue)
    {
      return (parentValue == null) || (!DefaultThreadContextMap.this.useMap) ? null : Collections.unmodifiableMap(new HashMap(parentValue));
    }
  };
  
  public DefaultThreadContextMap(boolean useMap)
  {
    this.useMap = useMap;
  }
  
  public void put(String key, String value)
  {
    if (!this.useMap) {
      return;
    }
    Map<String, String> map = (Map)this.localMap.get();
    map = map == null ? new HashMap() : new HashMap(map);
    map.put(key, value);
    this.localMap.set(Collections.unmodifiableMap(map));
  }
  
  public String get(String key)
  {
    Map<String, String> map = (Map)this.localMap.get();
    return map == null ? null : (String)map.get(key);
  }
  
  public void remove(String key)
  {
    Map<String, String> map = (Map)this.localMap.get();
    if (map != null)
    {
      Map<String, String> copy = new HashMap(map);
      copy.remove(key);
      this.localMap.set(Collections.unmodifiableMap(copy));
    }
  }
  
  public void clear()
  {
    this.localMap.remove();
  }
  
  public boolean containsKey(String key)
  {
    Map<String, String> map = (Map)this.localMap.get();
    return (map != null) && (map.containsKey(key));
  }
  
  public Map<String, String> getCopy()
  {
    Map<String, String> map = (Map)this.localMap.get();
    return map == null ? new HashMap() : new HashMap(map);
  }
  
  public Map<String, String> getImmutableMapOrNull()
  {
    return (Map)this.localMap.get();
  }
  
  public boolean isEmpty()
  {
    Map<String, String> map = (Map)this.localMap.get();
    return (map == null) || (map.size() == 0);
  }
  
  public String toString()
  {
    Map<String, String> map = (Map)this.localMap.get();
    return map == null ? "{}" : map.toString();
  }
}
