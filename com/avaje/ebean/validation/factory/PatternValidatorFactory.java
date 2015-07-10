package com.avaje.ebean.validation.factory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public final class PatternValidatorFactory
  implements ValidatorFactory
{
  private static final Map<String, Validator> cache = new HashMap();
  
  public Validator create(Annotation annotation, Class<?> type)
  {
    if (!type.equals(String.class))
    {
      String msg = "You can only specify @Pattern on String types";
      throw new RuntimeException(msg);
    }
    com.avaje.ebean.validation.Pattern pattern = (com.avaje.ebean.validation.Pattern)annotation;
    return create(pattern.regex(), pattern.flags());
  }
  
  public static synchronized Validator create(String regex, int flags)
  {
    regex = regex.trim();
    if (regex.length() == 0) {
      throw new RuntimeException("Missing regex attribute on @Pattern");
    }
    String key = regex;
    Validator validator = (Validator)cache.get(key);
    if (validator == null)
    {
      validator = new PatternValidator(regex, flags, null);
      cache.put(key, validator);
    }
    return validator;
  }
  
  private static final class PatternValidator
    implements Validator
  {
    private final java.util.regex.Pattern pattern;
    private final Object[] attributes;
    
    private PatternValidator(String regex, int flags)
    {
      this.pattern = java.util.regex.Pattern.compile(regex, flags);
      this.attributes = new Object[] { regex, Integer.valueOf(flags) };
    }
    
    public Object[] getAttributes()
    {
      return this.attributes;
    }
    
    public String getKey()
    {
      return "pattern";
    }
    
    public boolean isValid(Object value)
    {
      if (value == null) {
        return true;
      }
      String string = (String)value;
      Matcher m = this.pattern.matcher(string);
      return m.matches();
    }
  }
}
