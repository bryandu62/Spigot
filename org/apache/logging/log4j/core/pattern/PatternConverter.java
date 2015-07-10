package org.apache.logging.log4j.core.pattern;

public abstract interface PatternConverter
{
  public abstract void format(Object paramObject, StringBuilder paramStringBuilder);
  
  public abstract String getName();
  
  public abstract String getStyleClass(Object paramObject);
}
