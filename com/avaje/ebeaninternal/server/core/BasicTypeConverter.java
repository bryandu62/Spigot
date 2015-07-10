package com.avaje.ebeaninternal.server.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

public final class BasicTypeConverter
  implements Serializable
{
  private static final long serialVersionUID = 7691463236204070311L;
  public static final int UTIL_CALENDAR = -999998986;
  public static final int UTIL_DATE = -999998988;
  public static final int MATH_BIGINTEGER = -999998987;
  public static final int ENUM = -999998989;
  
  public static Object convert(Object value, int toDataType)
  {
    try
    {
      switch (toDataType)
      {
      case -999998988: 
        return toUtilDate(value);
      case -999998986: 
        return toCalendar(value);
      case -5: 
        return toLong(value);
      case 4: 
        return toInteger(value);
      case -7: 
        return toBoolean(value);
      case -6: 
        return toByte(value);
      case 5: 
        return toShort(value);
      case 2: 
        return toBigDecimal(value);
      case 3: 
        return toBigDecimal(value);
      case 7: 
        return toFloat(value);
      case 8: 
        return toDouble(value);
      case 6: 
        return toDouble(value);
      case 16: 
        return toBoolean(value);
      case 93: 
        return toTimestamp(value);
      case 91: 
        return toDate(value);
      case 12: 
        return toString(value);
      case 1: 
        return toString(value);
      case 1111: 
        return value;
      case 2000: 
        return value;
      case -4: 
      case -2: 
      case 2004: 
        return value;
      case -1: 
      case 2005: 
        return value;
      }
      String msg = "Unhandled data type [" + toDataType + "] converting [" + value + "]";
      throw new RuntimeException(msg);
    }
    catch (ClassCastException e)
    {
      String m = "ClassCastException converting to data type [" + toDataType + "] value [" + value + "]";
      throw new RuntimeException(m);
    }
  }
  
  public static String toString(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof String)) {
      return (String)value;
    }
    if ((value instanceof char[])) {
      return String.valueOf((char[])value);
    }
    return value.toString();
  }
  
  public static Boolean toBoolean(Object value, String dbTrueValue)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Boolean)) {
      return (Boolean)value;
    }
    String s = value.toString();
    return Boolean.valueOf(s.equalsIgnoreCase(dbTrueValue));
  }
  
  public static Boolean toBoolean(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Boolean)) {
      return (Boolean)value;
    }
    return Boolean.valueOf(value.toString());
  }
  
  public static UUID toUUID(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof String)) {
      return UUID.fromString((String)value);
    }
    return (UUID)value;
  }
  
  public static BigDecimal toBigDecimal(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof BigDecimal)) {
      return (BigDecimal)value;
    }
    return new BigDecimal(value.toString());
  }
  
  public static Float toFloat(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Float)) {
      return (Float)value;
    }
    if ((value instanceof Number)) {
      return Float.valueOf(((Number)value).floatValue());
    }
    return Float.valueOf(value.toString());
  }
  
  public static Short toShort(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Short)) {
      return (Short)value;
    }
    if ((value instanceof Number)) {
      return Short.valueOf(((Number)value).shortValue());
    }
    return Short.valueOf(value.toString());
  }
  
  public static Byte toByte(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Byte)) {
      return (Byte)value;
    }
    return Byte.valueOf(value.toString());
  }
  
  public static Integer toInteger(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Integer)) {
      return (Integer)value;
    }
    if ((value instanceof Number)) {
      return Integer.valueOf(((Number)value).intValue());
    }
    return Integer.valueOf(value.toString());
  }
  
  public static Long toLong(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Long)) {
      return (Long)value;
    }
    if ((value instanceof String)) {
      return Long.valueOf((String)value);
    }
    if ((value instanceof Number)) {
      return Long.valueOf(((Number)value).longValue());
    }
    if ((value instanceof java.util.Date)) {
      return Long.valueOf(((java.util.Date)value).getTime());
    }
    if ((value instanceof Calendar)) {
      return Long.valueOf(((Calendar)value).getTime().getTime());
    }
    return Long.valueOf(value.toString());
  }
  
  public static BigInteger toMathBigInteger(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof BigInteger)) {
      return (BigInteger)value;
    }
    return new BigInteger(value.toString());
  }
  
  public static Double toDouble(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Double)) {
      return (Double)value;
    }
    if ((value instanceof Number)) {
      return Double.valueOf(((Number)value).doubleValue());
    }
    return Double.valueOf(value.toString());
  }
  
  public static Timestamp toTimestamp(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Timestamp)) {
      return (Timestamp)value;
    }
    if ((value instanceof java.util.Date)) {
      return new Timestamp(((java.util.Date)value).getTime());
    }
    if ((value instanceof Calendar)) {
      return new Timestamp(((Calendar)value).getTime().getTime());
    }
    if ((value instanceof String)) {
      return Timestamp.valueOf((String)value);
    }
    if ((value instanceof Number)) {
      return new Timestamp(((Number)value).longValue());
    }
    String msg = "Unable to convert [" + value.getClass().getName() + "] into a Timestamp.";
    throw new RuntimeException(msg);
  }
  
  public static Time toTime(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Time)) {
      return (Time)value;
    }
    if ((value instanceof String)) {
      return Time.valueOf((String)value);
    }
    String m = "Unable to convert [" + value.getClass().getName() + "] into a java.sql.Date.";
    throw new RuntimeException(m);
  }
  
  public static java.sql.Date toDate(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof java.sql.Date)) {
      return (java.sql.Date)value;
    }
    if ((value instanceof java.util.Date)) {
      return new java.sql.Date(((java.util.Date)value).getTime());
    }
    if ((value instanceof Calendar)) {
      return new java.sql.Date(((Calendar)value).getTime().getTime());
    }
    if ((value instanceof String)) {
      return java.sql.Date.valueOf((String)value);
    }
    if ((value instanceof Number)) {
      return new java.sql.Date(((Number)value).longValue());
    }
    String m = "Unable to convert [" + value.getClass().getName() + "] into a java.sql.Date.";
    throw new RuntimeException(m);
  }
  
  public static java.util.Date toUtilDate(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Timestamp)) {
      return new java.util.Date(((Timestamp)value).getTime());
    }
    if ((value instanceof java.sql.Date)) {
      return new java.util.Date(((java.sql.Date)value).getTime());
    }
    if ((value instanceof java.util.Date)) {
      return (java.util.Date)value;
    }
    if ((value instanceof Calendar)) {
      return ((Calendar)value).getTime();
    }
    if ((value instanceof String)) {
      return new java.util.Date(Timestamp.valueOf((String)value).getTime());
    }
    if ((value instanceof Number)) {
      return new java.util.Date(((Number)value).longValue());
    }
    throw new RuntimeException("Unable to convert [" + value.getClass().getName() + "] into a java.util.Date");
  }
  
  public static Calendar toCalendar(Object value)
  {
    if (value == null) {
      return null;
    }
    if ((value instanceof Calendar)) {
      return (Calendar)value;
    }
    if ((value instanceof java.util.Date))
    {
      java.util.Date date = (java.util.Date)value;
      return toCalendarFromDate(date);
    }
    if ((value instanceof String))
    {
      java.util.Date date = toUtilDate(value);
      return toCalendarFromDate(date);
    }
    if ((value instanceof Number))
    {
      long timeMillis = ((Number)value).longValue();
      java.util.Date date = new java.util.Date(timeMillis);
      return toCalendarFromDate(date);
    }
    String m = "Unable to convert [" + value.getClass().getName() + "] into a java.util.Date";
    throw new RuntimeException(m);
  }
  
  private static Calendar toCalendarFromDate(java.util.Date date)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    
    return cal;
  }
}
