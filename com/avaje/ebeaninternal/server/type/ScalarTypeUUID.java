package com.avaje.ebeaninternal.server.type;

import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.util.UUID;

public class ScalarTypeUUID
  extends ScalarTypeBaseVarchar<UUID>
{
  public ScalarTypeUUID()
  {
    super(UUID.class);
  }
  
  public int getLength()
  {
    return 40;
  }
  
  public UUID convertFromDbString(String dbValue)
  {
    return UUID.fromString(dbValue);
  }
  
  public String convertToDbString(UUID beanValue)
  {
    return formatValue(beanValue);
  }
  
  public UUID toBeanType(Object value)
  {
    return BasicTypeConverter.toUUID(value);
  }
  
  public Object toJdbcType(Object value)
  {
    return BasicTypeConverter.convert(value, this.jdbcType);
  }
  
  public String formatValue(UUID v)
  {
    return v.toString();
  }
  
  public UUID parse(String value)
  {
    return UUID.fromString(value);
  }
}
