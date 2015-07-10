package javax.persistence;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne
{
  Class targetEntity() default void.class;
  
  CascadeType[] cascade() default {};
  
  FetchType fetch() default FetchType.EAGER;
  
  boolean optional() default true;
}
