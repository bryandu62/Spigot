package org.bukkit.event;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler
{
  EventPriority priority() default EventPriority.NORMAL;
  
  boolean ignoreCancelled() default false;
}
