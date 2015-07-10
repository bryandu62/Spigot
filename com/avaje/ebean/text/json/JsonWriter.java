package com.avaje.ebean.text.json;

public abstract interface JsonWriter
{
  public abstract void appendRawValue(String paramString1, String paramString2);
  
  public abstract void appendQuoteEscapeValue(String paramString1, String paramString2);
}
