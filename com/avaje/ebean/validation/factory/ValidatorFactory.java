package com.avaje.ebean.validation.factory;

import java.lang.annotation.Annotation;

public abstract interface ValidatorFactory
{
  public abstract Validator create(Annotation paramAnnotation, Class<?> paramClass);
}
