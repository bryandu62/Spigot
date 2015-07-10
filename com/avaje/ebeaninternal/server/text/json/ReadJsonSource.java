package com.avaje.ebeaninternal.server.text.json;

public abstract interface ReadJsonSource
{
  public abstract char nextChar(String paramString);
  
  public abstract void ignoreWhiteSpace();
  
  public abstract void back();
  
  public abstract int pos();
  
  public abstract String getErrorHelp();
}
