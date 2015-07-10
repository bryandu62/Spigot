package org.bukkit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Target({java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Warning
{
  boolean value() default false;
  
  String reason() default "";
  
  public static enum WarningState
  {
    ON,  OFF,  DEFAULT;
    
    private static final Map<String, WarningState> values = ImmutableMap.builder()
      .put("off", OFF)
      .put("false", OFF)
      .put("f", OFF)
      .put("no", OFF)
      .put("n", OFF)
      .put("on", ON)
      .put("true", ON)
      .put("t", ON)
      .put("yes", ON)
      .put("y", ON)
      .put("", DEFAULT)
      .put("d", DEFAULT)
      .put("default", DEFAULT)
      .build();
    
    public boolean printFor(Warning warning)
    {
      if (this == DEFAULT) {
        return (warning == null) || (warning.value());
      }
      return this == ON;
    }
    
    public static WarningState value(String value)
    {
      if (value == null) {
        return DEFAULT;
      }
      WarningState state = (WarningState)values.get(value.toLowerCase());
      if (state == null) {
        return DEFAULT;
      }
      return state;
    }
  }
}
