package com.avaje.ebean;

public abstract interface QueryListener<T>
{
  public abstract void process(T paramT);
}
