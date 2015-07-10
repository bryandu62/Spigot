package com.avaje.ebean.validation.factory;

import java.lang.annotation.Annotation;

public class NotNullValidatorFactory
  implements ValidatorFactory
{
  public static final Validator NOT_NULL = new NotNullValidator();
  
  public Validator create(Annotation annotation, Class<?> type)
  {
    return NOT_NULL;
  }
  
  public static class NotNullValidator
    extends NoAttributesValidator
  {
    public String getKey()
    {
      return "notnull";
    }
    
    public boolean isValid(Object value)
    {
      return value != null;
    }
  }
}
