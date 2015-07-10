package com.avaje.ebean.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheTuning
{
  int maxSize() default 0;
  
  int maxIdleSecs() default 0;
  
  int maxSecsToLive() default 0;
}
