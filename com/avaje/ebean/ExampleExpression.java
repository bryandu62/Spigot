package com.avaje.ebean;

public abstract interface ExampleExpression
  extends Expression
{
  public abstract ExampleExpression includeZeros();
  
  public abstract ExampleExpression caseInsensitive();
  
  public abstract ExampleExpression useStartsWith();
  
  public abstract ExampleExpression useContains();
  
  public abstract ExampleExpression useEndsWith();
  
  public abstract ExampleExpression useEqualTo();
}
