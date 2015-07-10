package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.config.ScalarTypeConverter;
import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.text.json.WriteJsonBuffer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public class ScalarTypeWrapper<B, S>
  implements ScalarType<B>
{
  private final ScalarType<S> scalarType;
  private final ScalarTypeConverter<B, S> converter;
  private final Class<B> wrapperType;
  private final B nullValue;
  
  public ScalarTypeWrapper(Class<B> wrapperType, ScalarType<S> scalarType, ScalarTypeConverter<B, S> converter)
  {
    this.scalarType = scalarType;
    this.converter = converter;
    this.nullValue = converter.getNullValue();
    this.wrapperType = wrapperType;
  }
  
  public String toString()
  {
    return "ScalarTypeWrapper " + this.wrapperType + " to " + this.scalarType.getType();
  }
  
  public Object readData(DataInput dataInput)
    throws IOException
  {
    Object v = this.scalarType.readData(dataInput);
    return this.converter.wrapValue(v);
  }
  
  public void writeData(DataOutput dataOutput, Object v)
    throws IOException
  {
    S sv = this.converter.unwrapValue(v);
    this.scalarType.writeData(dataOutput, sv);
  }
  
  public void bind(DataBind b, B value)
    throws SQLException
  {
    if (value == null)
    {
      this.scalarType.bind(b, null);
    }
    else
    {
      S sv = this.converter.unwrapValue(value);
      this.scalarType.bind(b, sv);
    }
  }
  
  public int getJdbcType()
  {
    return this.scalarType.getJdbcType();
  }
  
  public int getLength()
  {
    return this.scalarType.getLength();
  }
  
  public Class<B> getType()
  {
    return this.wrapperType;
  }
  
  public boolean isDateTimeCapable()
  {
    return this.scalarType.isDateTimeCapable();
  }
  
  public boolean isJdbcNative()
  {
    return false;
  }
  
  public String format(Object v)
  {
    return formatValue(v);
  }
  
  public String formatValue(B v)
  {
    S sv = this.converter.unwrapValue(v);
    return this.scalarType.formatValue(sv);
  }
  
  public B parse(String value)
  {
    S sv = this.scalarType.parse(value);
    if (sv == null) {
      return (B)this.nullValue;
    }
    return (B)this.converter.wrapValue(sv);
  }
  
  public B parseDateTime(long systemTimeMillis)
  {
    S sv = this.scalarType.parseDateTime(systemTimeMillis);
    if (sv == null) {
      return (B)this.nullValue;
    }
    return (B)this.converter.wrapValue(sv);
  }
  
  public void loadIgnore(DataReader dataReader)
  {
    dataReader.incrementPos(1);
  }
  
  public B read(DataReader dataReader)
    throws SQLException
  {
    S sv = this.scalarType.read(dataReader);
    if (sv == null) {
      return (B)this.nullValue;
    }
    return (B)this.converter.wrapValue(sv);
  }
  
  public B toBeanType(Object value)
  {
    if (value == null) {
      return (B)this.nullValue;
    }
    if (getType().isAssignableFrom(value.getClass())) {
      return (B)value;
    }
    if ((value instanceof String)) {
      return (B)parse((String)value);
    }
    S sv = this.scalarType.toBeanType(value);
    return (B)this.converter.wrapValue(sv);
  }
  
  public Object toJdbcType(Object value)
  {
    Object sv = this.converter.unwrapValue(value);
    if (sv == null) {
      return this.nullValue;
    }
    return this.scalarType.toJdbcType(sv);
  }
  
  public void accumulateScalarTypes(String propName, CtCompoundTypeScalarList list)
  {
    list.addScalarType(propName, this);
  }
  
  public ScalarType<?> getScalarType()
  {
    return this;
  }
  
  public String jsonToString(B value, JsonValueAdapter ctx)
  {
    S sv = this.converter.unwrapValue(value);
    return this.scalarType.jsonToString(sv, ctx);
  }
  
  public void jsonWrite(WriteJsonBuffer buffer, B value, JsonValueAdapter ctx)
  {
    S sv = this.converter.unwrapValue(value);
    this.scalarType.jsonWrite(buffer, sv, ctx);
  }
  
  public B jsonFromString(String value, JsonValueAdapter ctx)
  {
    S s = this.scalarType.jsonFromString(value, ctx);
    return (B)this.converter.wrapValue(s);
  }
}
