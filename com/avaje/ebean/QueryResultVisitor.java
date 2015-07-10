package com.avaje.ebean;

public abstract interface QueryResultVisitor<T>
{
  public abstract boolean accept(T paramT);
}
