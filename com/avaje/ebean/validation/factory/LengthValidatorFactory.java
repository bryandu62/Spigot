package com.avaje.ebean.validation.factory;

import com.avaje.ebean.validation.Length;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public final class LengthValidatorFactory
  implements ValidatorFactory
{
  private static final Map<String, Validator> cache = new HashMap();
  
  public Validator create(Annotation annotation, Class<?> type)
  {
    if (!type.equals(String.class))
    {
      String msg = "You can only specify @Length on String types";
      throw new RuntimeException(msg);
    }
    Length length = (Length)annotation;
    return create(length.min(), length.max());
  }
  
  public static synchronized Validator create(int min, int max)
  {
    String key = min + ":" + max;
    Validator validator = (Validator)cache.get(key);
    if (validator == null)
    {
      validator = new LengthValidator(min, max, null);
      cache.put(key, validator);
    }
    return validator;
  }
  
  public static final class LengthValidator
    implements Validator
  {
    private final int min;
    private final int max;
    private final String key;
    private final Object[] attributes;
    
    private LengthValidator(int min, int max)
    {
      this.min = min;
      this.max = max;
      this.key = determineKey(min, max);
      this.attributes = determineAttributes(min, max);
    }
    
    private String determineKey(int min, int max)
    {
      if ((min == 0) && (max > 0)) {
        return "length.max";
      }
      if ((min > 0) && (max == 0)) {
        return "length.min";
      }
      return "length.minmax";
    }
    
    private Object[] determineAttributes(int min, int max)
    {
      if ((min == 0) && (max > 0)) {
        return new Object[] { Integer.valueOf(max) };
      }
      if ((min > 0) && (max == 0)) {
        return new Object[] { Integer.valueOf(min) };
      }
      return new Object[] { Integer.valueOf(min), Integer.valueOf(max) };
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
      String s = (String)value;
      int len = s.length();
      return (len >= this.min) && (len <= this.max);
    }
  }
}
