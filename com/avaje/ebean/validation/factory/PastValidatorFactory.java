package com.avaje.ebean.validation.factory;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Date;

public class PastValidatorFactory
  implements ValidatorFactory
{
  Validator DATE;
  Validator CALENDAR;
  
  public PastValidatorFactory()
  {
    this.DATE = new DateValidator(null);
    this.CALENDAR = new CalendarValidator(null);
  }
  
  public Validator create(Annotation annotation, Class<?> type)
  {
    if (Date.class.isAssignableFrom(type)) {
      return this.DATE;
    }
    if (Calendar.class.isAssignableFrom(type)) {
      return this.CALENDAR;
    }
    String msg = "Can not use @Past on type " + type;
    throw new RuntimeException(msg);
  }
  
  private static class DateValidator
    extends NoAttributesValidator
  {
    public String getKey()
    {
      return "past";
    }
    
    public boolean isValid(Object value)
    {
      if (value == null) {
        return true;
      }
      Date date = (Date)value;
      return date.before(new Date());
    }
  }
  
  private static class CalendarValidator
    extends NoAttributesValidator
  {
    public String getKey()
    {
      return "past";
    }
    
    public boolean isValid(Object value)
    {
      if (value == null) {
        return true;
      }
      Calendar cal = (Calendar)value;
      return cal.before(Calendar.getInstance());
    }
  }
}
