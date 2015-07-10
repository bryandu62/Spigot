package com.avaje.ebean.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumMapping
{
  String nameValuePairs();
  
  boolean integerType() default false;
  
  int length() default 0;
}
