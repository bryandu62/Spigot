package com.avaje.ebeaninternal.server.type;

import javax.persistence.PersistenceException;

public class ScalarTypeClass
  extends ScalarTypeBaseVarchar<Class>
{
  public ScalarTypeClass()
  {
    super(Class.class);
  }
  
  public int getLength()
  {
    return 255;
  }
  
  public Class<?> convertFromDbString(String dbValue)
  {
    return parse(dbValue);
  }
  
  public String convertToDbString(Class beanValue)
  {
    return beanValue.getCanonicalName();
  }
  
  public String formatValue(Class v)
  {
    return v.getCanonicalName();
  }
  
  public Class<?> parse(String value)
  {
    try
    {
      return Class.forName(value);
    }
    catch (Exception e)
    {
      String msg = "Unable to find Class " + value;
      throw new PersistenceException(msg, e);
    }
  }
}
