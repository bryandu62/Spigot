package com.avaje.ebean.validation.factory;

import java.lang.annotation.Annotation;

public class EmailValidatorFactory
  implements ValidatorFactory
{
  public static final Validator EMAIL = new EmailValidator();
  
  public Validator create(Annotation annotation, Class<?> type)
  {
    if (!type.equals(String.class)) {
      throw new RuntimeException("Can only apply this annotation to String types, not " + type);
    }
    return EMAIL;
  }
  
  public static class EmailValidator
    extends NoAttributesValidator
  {
    private final EmailValidation emailValidation;
    
    EmailValidator()
    {
      this.emailValidation = EmailValidation.create(false, false);
    }
    
    public String getKey()
    {
      return "email";
    }
    
    public boolean isValid(Object value)
    {
      if (value == null) {
        return true;
      }
      return this.emailValidation.isValid((String)value);
    }
  }
}
