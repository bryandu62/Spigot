package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.text.json.WriteJsonBuffer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.PersistenceException;

public class ScalarTypeLdapTimestamp<T>
  implements ScalarType<T>
{
  private static final String timestampLDAPFormat = "yyyyMMddHHmmss'Z'";
  private final ScalarType<T> baseType;
  
  public ScalarTypeLdapTimestamp(ScalarType<T> baseType)
  {
    this.baseType = baseType;
  }
  
  public T toBeanType(Object value)
  {
    if (value == null) {
      return null;
    }
    if (!(value instanceof String))
    {
      String msg = "Expecting a String type but got " + value.getClass() + " value[" + value + "]";
      throw new PersistenceException(msg);
    }
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
      Date date = sdf.parse((String)value);
      
      return (T)this.baseType.parseDateTime(date.getTime());
    }
    catch (Exception e)
    {
      String msg = "Error parsing LDAP timestamp " + value;
      throw new PersistenceException(msg, e);
    }
  }
  
  public Object toJdbcType(Object value)
  {
    if (value == null) {
      return null;
    }
    Object ts = this.baseType.toJdbcType(value);
    if (!(ts instanceof Timestamp))
    {
      String msg = "Expecting a Timestamp type but got " + value.getClass() + " value[" + value + "]";
      throw new PersistenceException(msg);
    }
    Timestamp t = (Timestamp)ts;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
    return sdf.format(t);
  }
  
  public void bind(DataBind b, T value)
    throws SQLException
  {
    this.baseType.bind(b, value);
  }
  
  public int getJdbcType()
  {
    return 12;
  }
  
  public int getLength()
  {
    return this.baseType.getLength();
  }
  
  public Class<T> getType()
  {
    return this.baseType.getType();
  }
  
  public boolean isDateTimeCapable()
  {
    return this.baseType.isDateTimeCapable();
  }
  
  public boolean isJdbcNative()
  {
    return false;
  }
  
  public void loadIgnore(DataReader dataReader)
  {
    this.baseType.loadIgnore(dataReader);
  }
  
  public String format(Object v)
  {
    return this.baseType.format(v);
  }
  
  public String formatValue(T t)
  {
    return this.baseType.formatValue(t);
  }
  
  public T parse(String value)
  {
    return (T)this.baseType.parse(value);
  }
  
  public T parseDateTime(long systemTimeMillis)
  {
    return (T)this.baseType.parseDateTime(systemTimeMillis);
  }
  
  public T read(DataReader dataReader)
    throws SQLException
  {
    return (T)this.baseType.read(dataReader);
  }
  
  public void accumulateScalarTypes(String propName, CtCompoundTypeScalarList list)
  {
    this.baseType.accumulateScalarTypes(propName, list);
  }
  
  public String jsonToString(T value, JsonValueAdapter ctx)
  {
    return this.baseType.jsonToString(value, ctx);
  }
  
  public void jsonWrite(WriteJsonBuffer buffer, T value, JsonValueAdapter ctx)
  {
    this.baseType.jsonWrite(buffer, value, ctx);
  }
  
  public T jsonFromString(String value, JsonValueAdapter ctx)
  {
    return (T)this.baseType.jsonFromString(value, ctx);
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    return this.baseType.readData(dataInput);
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    this.baseType.writeData(dataOutput, v);
  }
}
