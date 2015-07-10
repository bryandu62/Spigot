package com.avaje.ebeaninternal.server.type;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public abstract class EnumToDbValueMap<T>
{
  final LinkedHashMap<Object, T> keyMap;
  final LinkedHashMap<T, Object> valueMap;
  final boolean allowNulls;
  final boolean isIntegerType;
  
  public static EnumToDbValueMap<?> create(boolean integerType)
  {
    return integerType ? new EnumToDbIntegerMap() : new EnumToDbStringMap();
  }
  
  public EnumToDbValueMap()
  {
    this(false, false);
  }
  
  public EnumToDbValueMap(boolean allowNulls, boolean isIntegerType)
  {
    this.allowNulls = allowNulls;
    this.isIntegerType = isIntegerType;
    this.keyMap = new LinkedHashMap();
    this.valueMap = new LinkedHashMap();
  }
  
  public boolean isIntegerType()
  {
    return this.isIntegerType;
  }
  
  public Iterator<T> dbValues()
  {
    return this.valueMap.keySet().iterator();
  }
  
  public Iterator<Object> beanValues()
  {
    return this.valueMap.values().iterator();
  }
  
  public abstract void bind(DataBind paramDataBind, Object paramObject)
    throws SQLException;
  
  public abstract Object read(DataReader paramDataReader)
    throws SQLException;
  
  public abstract int getDbType();
  
  public abstract EnumToDbValueMap<T> add(Object paramObject, String paramString);
  
  protected void addInternal(Object beanValue, T dbValue)
  {
    this.keyMap.put(beanValue, dbValue);
    this.valueMap.put(dbValue, beanValue);
  }
  
  public T getDbValue(Object beanValue)
  {
    if (beanValue == null) {
      return null;
    }
    T dbValue = this.keyMap.get(beanValue);
    if ((dbValue == null) && (!this.allowNulls))
    {
      String msg = "DB value for " + beanValue + " not found in " + this.valueMap;
      throw new IllegalArgumentException(msg);
    }
    return dbValue;
  }
  
  public Object getBeanValue(T dbValue)
  {
    if (dbValue == null) {
      return null;
    }
    Object beanValue = this.valueMap.get(dbValue);
    if ((beanValue == null) && (!this.allowNulls))
    {
      String msg = "Bean value for " + dbValue + " not found in " + this.valueMap;
      throw new IllegalArgumentException(msg);
    }
    return beanValue;
  }
}
