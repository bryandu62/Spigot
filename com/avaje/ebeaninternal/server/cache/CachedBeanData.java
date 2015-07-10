package com.avaje.ebeaninternal.server.cache;

import java.util.Set;

public class CachedBeanData
{
  private final Object sharableBean;
  private final Set<String> loadedProperties;
  private final Object[] data;
  private final int naturalKeyUpdate;
  
  public CachedBeanData(Object sharableBean, Set<String> loadedProperties, Object[] data, int naturalKeyUpdate)
  {
    this.sharableBean = sharableBean;
    this.loadedProperties = loadedProperties;
    this.data = data;
    this.naturalKeyUpdate = naturalKeyUpdate;
  }
  
  public Object getSharableBean()
  {
    return this.sharableBean;
  }
  
  public boolean isNaturalKeyUpdate()
  {
    return this.naturalKeyUpdate > -1;
  }
  
  public Object getNaturalKey()
  {
    return this.data[this.naturalKeyUpdate];
  }
  
  public boolean containsProperty(String propName)
  {
    return (this.loadedProperties == null) || (this.loadedProperties.contains(propName));
  }
  
  public Object getData(int i)
  {
    return this.data[i];
  }
  
  public Set<String> getLoadedProperties()
  {
    return this.loadedProperties;
  }
  
  public Object[] copyData()
  {
    Object[] dest = new Object[this.data.length];
    System.arraycopy(this.data, 0, dest, 0, this.data.length);
    return dest;
  }
}
