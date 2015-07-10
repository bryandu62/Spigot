package com.avaje.ebean.validation.factory;

import java.lang.annotation.Annotation;

public class AssertTrueValidatorFactory
  implements ValidatorFactory
{
  public static final Validator ASSERT_TRUE = new AssertTrueValidator();
  
  public Validator create(Annotation annotation, Class<?> type)
  {
    return ASSERT_TRUE;
  }
  
  public static class AssertTrueValidator
    extends NoAttributesValidator
  {
    public String getKey()
    {
      return "asserttrue";
    }
    
    public boolean isValid(Object value)
    {
      if (value == null) {
        return true;
      }
      return ((Boolean)value).booleanValue();
    }
  }
}
