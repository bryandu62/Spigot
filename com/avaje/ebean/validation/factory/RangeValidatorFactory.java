package com.avaje.ebean.validation.factory;

import com.avaje.ebean.validation.Range;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class RangeValidatorFactory
  implements ValidatorFactory
{
  private static final Map<String, Validator> cache = new HashMap();
  
  public Validator create(Annotation annotation, Class<?> type)
  {
    Range range = (Range)annotation;
    return create(range.min(), range.max(), type);
  }
  
  public synchronized Validator create(long min, long max, Class<?> type)
  {
    String key = type + ":" + min + ":" + max;
    Validator validator = (Validator)cache.get(key);
    if (validator != null) {
      return validator;
    }
    if (type.equals(String.class))
    {
      validator = new StringValidator(min, max, null);
    }
    else if (useDouble(type))
    {
      validator = new DoubleValidator(min, max, null);
    }
    else if (useLong(type))
    {
      validator = new LongValidator(min, max, null);
    }
    else
    {
      String msg = "@Range annotation not assignable to type " + type;
      throw new RuntimeException(msg);
    }
    cache.put(key, validator);
    return validator;
  }
  
  private static boolean useLong(Class<?> type)
  {
    if ((type.equals(Integer.TYPE)) || (type.equals(Long.TYPE)) || (type.equals(Short.TYPE))) {
      return true;
    }
    if (Number.class.isAssignableFrom(type)) {
      return true;
    }
    return false;
  }
  
  private static boolean useDouble(Class<?> type)
  {
    if ((type.equals(Float.TYPE)) || (type.equals(Double.TYPE))) {
      return true;
    }
    if (type.equals(BigDecimal.class)) {
      return true;
    }
    if (Double.class.isAssignableFrom(type)) {
      return true;
    }
    if (Float.class.isAssignableFrom(type)) {
      return true;
    }
    return false;
  }
  
  private static class DoubleValidator
    implements Validator
  {
    final long min;
    final long max;
    final String key;
    final Object[] attributes;
    
    private DoubleValidator(long min, long max)
    {
      this.min = min;
      this.max = max;
      this.key = determineKey(min, max);
      this.attributes = determineAttributes(min, max);
    }
    
    private String determineKey(long min, long max)
    {
      if ((min > Long.MIN_VALUE) && (max < Long.MAX_VALUE)) {
        return "range.minmax";
      }
      if (min > Long.MIN_VALUE) {
        return "range.min";
      }
      return "range.max";
    }
    
    private Object[] determineAttributes(long min, long max)
    {
      if ((min > Long.MIN_VALUE) && (max < Long.MAX_VALUE)) {
        return new Object[] { Long.valueOf(min), Long.valueOf(max) };
      }
      if (min > Long.MIN_VALUE) {
        return new Object[] { Long.valueOf(min) };
      }
      return new Object[] { Long.valueOf(max) };
    }
    
    public Object[] getAttributes()
    {
      return this.attributes;
    }
    
    public String getKey()
    {
      return this.key;
    }
    
    public boolean isValid(Object value)
    {
      if (value == null) {
        return true;
      }
      Number n = (Number)value;
      double dv = n.doubleValue();
      return (dv >= this.min) && (dv <= this.max);
    }
    
    public String toString()
    {
      return getClass().getName() + "key:" + this.key + " min:" + this.min + " max:" + this.max;
    }
  }
  
  private static class LongValidator
    extends RangeValidatorFactory.DoubleValidator
  {
    private LongValidator(long min, long max)
    {
      super(max, null);
    }
    
    public boolean isValid(Object value)
    {
      if (value == null) {
        return true;
      }
      Number n = (Number)value;
      long lv = n.longValue();
      return (lv >= this.min) && (lv <= this.max);
    }
  }
  
  private static class StringValidator
    extends RangeValidatorFactory.DoubleValidator
  {
    private StringValidator(long min, long max)
    {
      super(max, null);
    }
    
    public boolean isValid(Object value)
    {
      if (value == null) {
        return true;
      }
      BigDecimal bd = new BigDecimal((String)value);
      double dv = bd.doubleValue();
      return (dv >= this.min) && (dv <= this.max);
    }
  }
}
