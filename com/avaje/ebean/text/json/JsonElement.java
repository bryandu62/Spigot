package com.avaje.ebean.text.json;

public abstract interface JsonElement
{
  public abstract boolean isPrimitive();
  
  public abstract String toPrimitiveString();
  
  public abstract Object eval(String paramString);
  
  public abstract int evalInt(String paramString);
  
  public abstract String evalString(String paramString);
  
  public abstract boolean evalBoolean(String paramString);
}
