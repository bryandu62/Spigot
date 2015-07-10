package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.text.json.WriteJsonBuffer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public class ScalarTypeBytesEncrypted
  implements ScalarType<byte[]>
{
  private final ScalarTypeBytesBase baseType;
  private final DataEncryptSupport dataEncryptSupport;
  
  public ScalarTypeBytesEncrypted(ScalarTypeBytesBase baseType, DataEncryptSupport dataEncryptSupport)
  {
    this.baseType = baseType;
    this.dataEncryptSupport = dataEncryptSupport;
  }
  
  public void bind(DataBind b, byte[] value)
    throws SQLException
  {
    value = this.dataEncryptSupport.encrypt(value);
    this.baseType.bind(b, value);
  }
  
  public int getJdbcType()
  {
    return this.baseType.getJdbcType();
  }
  
  public int getLength()
  {
    return this.baseType.getLength();
  }
  
  public Class<byte[]> getType()
  {
    return byte[].class;
  }
  
  public boolean isDateTimeCapable()
  {
    return this.baseType.isDateTimeCapable();
  }
  
  public boolean isJdbcNative()
  {
    return this.baseType.isJdbcNative();
  }
  
  public void loadIgnore(DataReader dataReader)
  {
    this.baseType.loadIgnore(dataReader);
  }
  
  public String format(Object v)
  {
    throw new RuntimeException("Not used");
  }
  
  public String formatValue(byte[] v)
  {
    throw new RuntimeException("Not used");
  }
  
  public byte[] parse(String value)
  {
    return this.baseType.parse(value);
  }
  
  public byte[] parseDateTime(long systemTimeMillis)
  {
    return this.baseType.parseDateTime(systemTimeMillis);
  }
  
  public byte[] read(DataReader dataReader)
    throws SQLException
  {
    byte[] data = (byte[])this.baseType.read(dataReader);
    data = this.dataEncryptSupport.decrypt(data);
    return data;
  }
  
  public byte[] toBeanType(Object value)
  {
    return this.baseType.toBeanType(value);
  }
  
  public Object toJdbcType(Object value)
  {
    return this.baseType.toJdbcType(value);
  }
  
  public void accumulateScalarTypes(String propName, CtCompoundTypeScalarList list)
  {
    this.baseType.accumulateScalarTypes(propName, list);
  }
  
  public void jsonWrite(WriteJsonBuffer buffer, byte[] value, JsonValueAdapter ctx)
  {
    this.baseType.jsonWrite(buffer, value, ctx);
  }
  
  public String jsonToString(byte[] value, JsonValueAdapter ctx)
  {
    return this.baseType.jsonToString(value, ctx);
  }
  
  public byte[] jsonFromString(String value, JsonValueAdapter ctx)
  {
    return (byte[])this.baseType.jsonFromString(value, ctx);
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    int len = dataInput.readInt();
    byte[] value = new byte[len];
    dataInput.readFully(value);
    return value;
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    byte[] value = (byte[])v;
    dataOutput.writeInt(value.length);
    dataOutput.write(value);
  }
}
