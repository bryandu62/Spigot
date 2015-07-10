package com.avaje.ebean.annotation;

import com.avaje.ebean.TxIsolation;
import com.avaje.ebean.TxType;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional
{
  TxType type() default TxType.REQUIRED;
  
  TxIsolation isolation() default TxIsolation.DEFAULT;
  
  boolean readOnly() default false;
  
  String serverName() default "";
  
  Class<? extends Throwable>[] rollbackFor() default {};
  
  Class<? extends Throwable>[] noRollbackFor() default {};
}
