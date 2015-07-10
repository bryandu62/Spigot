package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.StringFormatter;
import com.avaje.ebean.text.StringParser;
import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.text.json.WriteJsonBuffer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;

public abstract interface ScalarType<T>
  extends StringParser, StringFormatter, ScalarDataReader<T>
{
  public abstract int getLength();
  
  public abstract boolean isJdbcNative();
  
  public abstract int getJdbcType();
  
  public abstract Class<T> getType();
  
  public abstract T read(DataReader paramDataReader)
    throws SQLException;
  
  public abstract void loadIgnore(DataReader paramDataReader);
  
  public abstract void bind(DataBind paramDataBind, T paramT)
    throws SQLException;
  
  public abstract Object toJdbcType(Object paramObject);
  
  public abstract T toBeanType(Object paramObject);
  
  public abstract String formatValue(T paramT);
  
  public abstract String format(Object paramObject);
  
  public abstract T parse(String paramString);
  
  public abstract T parseDateTime(long paramLong);
  
  public abstract boolean isDateTimeCapable();
  
  public abstract void jsonWrite(WriteJsonBuffer paramWriteJsonBuffer, T paramT, JsonValueAdapter paramJsonValueAdapter);
  
  public abstract String jsonToString(T paramT, JsonValueAdapter paramJsonValueAdapter);
  
  public abstract T jsonFromString(String paramString, JsonValueAdapter paramJsonValueAdapter);
  
  public abstract Object readData(DataInput paramDataInput)
    throws IOException;
  
  public abstract void writeData(DataOutput paramDataOutput, Object paramObject)
    throws IOException;
}
