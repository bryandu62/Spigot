package com.avaje.ebeaninternal.server.type;

import java.sql.SQLException;
import java.util.Iterator;

public class ScalarTypeEnumWithMapping
  extends ScalarTypeEnumStandard.EnumBase
  implements ScalarType, ScalarTypeEnum
{
  private final EnumToDbValueMap beanDbMap;
  private final int length;
  
  public ScalarTypeEnumWithMapping(EnumToDbValueMap<?> beanDbMap, Class<?> enumType, int length)
  {
    super(enumType, false, beanDbMap.getDbType());
    this.beanDbMap = beanDbMap;
    this.length = length;
  }
  
  public String getContraintInValues()
  {
    StringBuilder sb = new StringBuilder();
    
    int i = 0;
    
    sb.append("(");
    
    Iterator<?> it = this.beanDbMap.dbValues();
    while (it.hasNext())
    {
      Object dbValue = it.next();
      if (i++ > 0) {
        sb.append(",");
      }
      if (!this.beanDbMap.isIntegerType()) {
        sb.append("'");
      }
      sb.append(dbValue.toString());
      if (!this.beanDbMap.isIntegerType()) {
        sb.append("'");
      }
    }
    sb.append(")");
    
    return sb.toString();
  }
  
  public int getLength()
  {
    return this.length;
  }
  
  public void bind(DataBind b, Object value)
    throws SQLException
  {
    this.beanDbMap.bind(b, value);
  }
  
  public Object read(DataReader dataReader)
    throws SQLException
  {
    return this.beanDbMap.read(dataReader);
  }
  
  public Object toBeanType(Object dbValue)
  {
    return this.beanDbMap.getBeanValue(dbValue);
  }
  
  public Object toJdbcType(Object beanValue)
  {
    return this.beanDbMap.getDbValue(beanValue);
  }
}
