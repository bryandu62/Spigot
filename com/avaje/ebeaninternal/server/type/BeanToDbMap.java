package com.avaje.ebeaninternal.server.type;

import java.util.HashMap;

public class BeanToDbMap<B, D>
{
  final HashMap<B, D> keyMap;
  final HashMap<D, B> valueMap;
  final boolean allowNulls;
  
  public BeanToDbMap()
  {
    this(false);
  }
  
  public BeanToDbMap(boolean allowNulls)
  {
    this.allowNulls = allowNulls;
    this.keyMap = new HashMap();
    this.valueMap = new HashMap();
  }
  
  public BeanToDbMap<B, D> add(B beanValue, D dbValue)
  {
    this.keyMap.put(beanValue, dbValue);
    this.valueMap.put(dbValue, beanValue);
    return this;
  }
  
  public D getDbValue(B beanValue)
  {
    if (beanValue == null) {
      return null;
    }
    D dbValue = this.keyMap.get(beanValue);
    if ((dbValue == null) && (!this.allowNulls))
    {
      String msg = "DB value for " + beanValue + " not found in " + this.valueMap;
      throw new IllegalArgumentException(msg);
    }
    return dbValue;
  }
  
  public B getBeanValue(D dbValue)
  {
    if (dbValue == null) {
      return null;
    }
    B beanValue = this.valueMap.get(dbValue);
    if ((beanValue == null) && (!this.allowNulls))
    {
      String msg = "Bean value for " + dbValue + " not found in " + this.valueMap;
      throw new IllegalArgumentException(msg);
    }
    return beanValue;
  }
}
