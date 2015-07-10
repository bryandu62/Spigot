package com.avaje.ebean.annotation;

import com.avaje.ebean.Query.UseIndex;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheStrategy
{
  boolean useBeanCache() default true;
  
  String naturalKey() default "";
  
  boolean readOnly() default false;
  
  String warmingQuery() default "";
  
  Query.UseIndex useIndex() default Query.UseIndex.DEFAULT;
}
