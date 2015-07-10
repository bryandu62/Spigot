package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.text.json.WriteJsonBuffer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public class ScalarTypeEncryptedWrapper<T>
  implements ScalarType<T>
{
  private final ScalarType<T> wrapped;
  private final DataEncryptSupport dataEncryptSupport;
  private final ScalarTypeBytesBase byteArrayType;
  
  public ScalarTypeEncryptedWrapper(ScalarType<T> wrapped, ScalarTypeBytesBase byteArrayType, DataEncryptSupport dataEncryptSupport)
  {
    this.wrapped = wrapped;
    this.byteArrayType = byteArrayType;
    this.dataEncryptSupport = dataEncryptSupport;
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    return this.wrapped.readData(dataInput);
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    this.wrapped.writeData(dataOutput, v);
  }
  
  public T read(DataReader dataReader)
    throws SQLException
  {
    byte[] data = dataReader.getBytes();
    String formattedValue = this.dataEncryptSupport.decryptObject(data);
    if (formattedValue == null) {
      return null;
    }
    return (T)this.wrapped.parse(formattedValue);
  }
  
  private byte[] encrypt(T value)
  {
    String formatValue = this.wrapped.formatValue(value);
    return this.dataEncryptSupport.encryptObject(formatValue);
  }
  
  public void bind(DataBind b, T value)
    throws SQLException
  {
    byte[] encryptedValue = encrypt(value);
    this.byteArrayType.bind(b, encryptedValue);
  }
  
  public int getJdbcType()
  {
    return this.byteArrayType.getJdbcType();
  }
  
  public int getLength()
  {
    return this.byteArrayType.getLength();
  }
  
  public Class<T> getType()
  {
    return this.wrapped.getType();
  }
  
  public boolean isDateTimeCapable()
  {
    return this.wrapped.isDateTimeCapable();
  }
  
  public boolean isJdbcNative()
  {
    return false;
  }
  
  public void loadIgnore(DataReader dataReader)
  {
    this.wrapped.loadIgnore(dataReader);
  }
  
  public String format(Object v)
  {
    return formatValue(v);
  }
  
  public String formatValue(T v)
  {
    return this.wrapped.formatValue(v);
  }
  
  public T parse(String value)
  {
    return (T)this.wrapped.parse(value);
  }
  
  public T parseDateTime(long systemTimeMillis)
  {
    return (T)this.wrapped.parseDateTime(systemTimeMillis);
  }
  
  public T toBeanType(Object value)
  {
    return (T)this.wrapped.toBeanType(value);
  }
  
  public Object toJdbcType(Object value)
  {
    return this.wrapped.toJdbcType(value);
  }
  
  public void accumulateScalarTypes(String propName, CtCompoundTypeScalarList list)
  {
    this.wrapped.accumulateScalarTypes(propName, list);
  }
  
  public String jsonToString(T value, JsonValueAdapter ctx)
  {
    return this.wrapped.jsonToString(value, ctx);
  }
  
  public void jsonWrite(WriteJsonBuffer buffer, T value, JsonValueAdapter ctx)
  {
    this.wrapped.jsonWrite(buffer, value, ctx);
  }
  
  public T jsonFromString(String value, JsonValueAdapter ctx)
  {
    return (T)this.wrapped.jsonFromString(value, ctx);
  }
}
