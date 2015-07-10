package com.avaje.ebean.enhance.agent;

public class NoEnhancementRequiredException
  extends RuntimeException
{
  private static final long serialVersionUID = 7222178323991228946L;
  
  public NoEnhancementRequiredException() {}
  
  public NoEnhancementRequiredException(String msg)
  {
    super(msg);
  }
}
