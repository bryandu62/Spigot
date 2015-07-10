package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.text.json.WriteJsonBuffer;

public abstract class ScalarTypeBase<T>
  implements ScalarType<T>
{
  protected final Class<T> type;
  protected final boolean jdbcNative;
  protected final int jdbcType;
  
  public ScalarTypeBase(Class<T> type, boolean jdbcNative, int jdbcType)
  {
    this.type = type;
    this.jdbcNative = jdbcNative;
    this.jdbcType = jdbcType;
  }
  
  public int getLength()
  {
    return 0;
  }
  
  public boolean isJdbcNative()
  {
    return this.jdbcNative;
  }
  
  public int getJdbcType()
  {
    return this.jdbcType;
  }
  
  public Class<T> getType()
  {
    return this.type;
  }
  
  public String format(Object v)
  {
    return formatValue(v);
  }
  
  public boolean isDbNull(Object value)
  {
    return value == null;
  }
  
  public Object getDbNullValue(Object value)
  {
    return value;
  }
  
  public void loadIgnore(DataReader dataReader)
  {
    dataReader.incrementPos(1);
  }
  
  public void accumulateScalarTypes(String propName, CtCompoundTypeScalarList list)
  {
    list.addScalarType(propName, this);
  }
  
  public void jsonWrite(WriteJsonBuffer buffer, T value, JsonValueAdapter ctx)
  {
    String v = jsonToString(value, ctx);
    buffer.append(v);
  }
  
  public String jsonToString(T value, JsonValueAdapter ctx)
  {
    return formatValue(value);
  }
  
  public T jsonFromString(String value, JsonValueAdapter ctx)
  {
    return (T)parse(value);
  }
}
