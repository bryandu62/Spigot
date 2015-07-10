package com.avaje.ebean.validation.factory;

import java.lang.annotation.Annotation;

public class AssertFalseValidatorFactory
  implements ValidatorFactory
{
  public static final Validator ASSERT_FALSE = new AssertFalseValidator();
  
  public Validator create(Annotation annotation, Class<?> type)
  {
    return ASSERT_FALSE;
  }
  
  public static class AssertFalseValidator
    extends NoAttributesValidator
  {
    public String getKey()
    {
      return "assertfalse";
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
