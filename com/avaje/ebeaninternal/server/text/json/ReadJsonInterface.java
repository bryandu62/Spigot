package com.avaje.ebeaninternal.server.text.json;

public abstract interface ReadJsonInterface
{
  public abstract void ignoreWhiteSpace();
  
  public abstract char nextChar();
  
  public abstract String getTokenKey();
  
  public abstract boolean readKeyNext();
  
  public abstract boolean readValueNext();
  
  public abstract boolean readArrayNext();
  
  public abstract String readQuotedValue();
  
  public abstract String readUnquotedValue(char paramChar);
}
