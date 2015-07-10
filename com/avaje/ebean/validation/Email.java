package com.avaje.ebean.validation;

import com.avaje.ebean.validation.factory.EmailValidatorFactory;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ValidatorMeta(factory=EmailValidatorFactory.class)
@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {}
