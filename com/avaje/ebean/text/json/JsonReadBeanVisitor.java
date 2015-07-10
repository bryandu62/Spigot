package com.avaje.ebean.text.json;

import java.util.Map;

public abstract interface JsonReadBeanVisitor<T>
{
  public abstract void visit(T paramT, Map<String, JsonElement> paramMap);
}
