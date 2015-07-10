package com.avaje.ebean.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LdapAttribute
{
  String name() default "";
  
  Class<?> adapter() default void.class;
  
  boolean insertable() default true;
  
  boolean updatable() default true;
}
