package org.apache.logging.log4j.core.config.plugins;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface Plugin
{
  public static final String EMPTY = "";
  
  String name();
  
  String category();
  
  String elementType() default "";
  
  boolean printObject() default false;
  
  boolean deferChildren() default false;
}
