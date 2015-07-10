package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebeaninternal.server.core.BasicTypeConverter;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

public class DefaultTypeFactory
{
  private final ServerConfig serverConfig;
  
  public DefaultTypeFactory(ServerConfig serverConfig)
  {
    this.serverConfig = serverConfig;
  }
  
  private ScalarType<Boolean> createBoolean(String trueValue, String falseValue)
  {
    try
    {
      Integer intTrue = BasicTypeConverter.toInteger(trueValue);
      Integer intFalse = BasicTypeConverter.toInteger(falseValue);
      
      return new ScalarTypeBoolean.IntBoolean(intTrue, intFalse);
    }
    catch (NumberFormatException e) {}
    return new ScalarTypeBoolean.StringBoolean(trueValue, falseValue);
  }
  
  public ScalarType<Boolean> createBoolean()
  {
    if (this.serverConfig == null) {
      return new ScalarTypeBoolean.Native();
    }
    String trueValue = this.serverConfig.getDatabaseBooleanTrue();
    String falseValue = this.serverConfig.getDatabaseBooleanFalse();
    if ((falseValue != null) && (trueValue != null)) {
      return createBoolean(trueValue, falseValue);
    }
    int booleanDbType = this.serverConfig.getDatabasePlatform().getBooleanDbType();
    if (booleanDbType == -7) {
      return new ScalarTypeBoolean.BitBoolean();
    }
    if (booleanDbType == 4) {
      return new ScalarTypeBoolean.IntBoolean(Integer.valueOf(1), Integer.valueOf(0));
    }
    if (booleanDbType == 12) {
      return new ScalarTypeBoolean.StringBoolean("T", "F");
    }
    if (booleanDbType == 16) {
      return new ScalarTypeBoolean.Native();
    }
    return new ScalarTypeBoolean.Native();
  }
  
  public ScalarType<Date> createUtilDate()
  {
    int utilDateType = getTemporalMapType("timestamp");
    
    return createUtilDate(utilDateType);
  }
  
  public ScalarType<Date> createUtilDate(int utilDateType)
  {
    switch (utilDateType)
    {
    case 91: 
      return new ScalarTypeUtilDate.DateType();
    case 93: 
      return new ScalarTypeUtilDate.TimestampType();
    }
    throw new RuntimeException("Invalid type " + utilDateType);
  }
  
  public ScalarType<Calendar> createCalendar()
  {
    int jdbcType = getTemporalMapType("timestamp");
    
    return createCalendar(jdbcType);
  }
  
  public ScalarType<Calendar> createCalendar(int jdbcType)
  {
    return new ScalarTypeCalendar(jdbcType);
  }
  
  private int getTemporalMapType(String mapType)
  {
    if (mapType.equalsIgnoreCase("date")) {
      return 91;
    }
    return 93;
  }
  
  public ScalarType<BigInteger> createMathBigInteger()
  {
    return new ScalarTypeMathBigInteger();
  }
}
