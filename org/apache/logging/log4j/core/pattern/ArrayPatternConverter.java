package org.apache.logging.log4j.core.pattern;

public abstract interface ArrayPatternConverter
  extends PatternConverter
{
  public abstract void format(StringBuilder paramStringBuilder, Object... paramVarArgs);
}
