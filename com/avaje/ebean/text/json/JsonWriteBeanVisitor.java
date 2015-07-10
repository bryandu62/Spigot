package com.avaje.ebean.text.json;

public abstract interface JsonWriteBeanVisitor<T>
{
  public abstract void visit(T paramT, JsonWriter paramJsonWriter);
}
