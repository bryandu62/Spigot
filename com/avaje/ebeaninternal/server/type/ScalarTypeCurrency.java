package com.avaje.ebeaninternal.server.type;

import java.util.Currency;

public class ScalarTypeCurrency
  extends ScalarTypeBaseVarchar<Currency>
{
  public ScalarTypeCurrency()
  {
    super(Currency.class);
  }
  
  public int getLength()
  {
    return 3;
  }
  
  public Currency convertFromDbString(String dbValue)
  {
    return Currency.getInstance(dbValue);
  }
  
  public String convertToDbString(Currency beanValue)
  {
    return beanValue.getCurrencyCode();
  }
  
  public String formatValue(Currency v)
  {
    return v.toString();
  }
  
  public Currency parse(String value)
  {
    return Currency.getInstance(value);
  }
}
