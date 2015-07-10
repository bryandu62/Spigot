package com.avaje.ebean.enhance.ant;

public abstract interface TransformationListener
{
  public abstract void logEvent(String paramString);
  
  public abstract void logError(String paramString);
}
