package com.avaje.ebeaninternal.server.type;

import java.sql.SQLException;
import javax.persistence.PersistenceException;

public class EnumToDbIntegerMap
  extends EnumToDbValueMap<Integer>
{
  public int getDbType()
  {
    return 4;
  }
  
  public EnumToDbIntegerMap add(Object beanValue, String stringDbValue)
  {
    try
    {
      Integer value = Integer.valueOf(stringDbValue);
      addInternal(beanValue, value);
      
      return this;
    }
    catch (Exception e)
    {
      String msg = "Error converted enum type[" + beanValue.getClass().getName();
      msg = msg + "] enum value[" + beanValue + "] string value [" + stringDbValue + "]";
      msg = msg + " to an Integer.";
      throw new PersistenceException(msg, e);
    }
  }
  
  public void bind(DataBind b, Object value)
    throws SQLException
  {
    if (value == null)
    {
      b.setNull(4);
    }
    else
    {
      Integer s = (Integer)getDbValue(value);
      b.setInt(s.intValue());
    }
  }
  
  public Object read(DataReader dataReader)
    throws SQLException
  {
    Integer i = dataReader.getInt();
    if (i == null) {
      return null;
    }
    return getBeanValue(i);
  }
}
