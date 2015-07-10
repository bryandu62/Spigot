package com.avaje.ebean.enhance.agent;

public class AlreadyEnhancedException
  extends RuntimeException
{
  private static final long serialVersionUID = -831705721822834774L;
  final String className;
  
  public AlreadyEnhancedException(String className)
  {
    this.className = className;
  }
  
  public String getClassName()
  {
    return this.className;
  }
}
