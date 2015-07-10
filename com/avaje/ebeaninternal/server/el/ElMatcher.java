package com.avaje.ebeaninternal.server.el;

public abstract interface ElMatcher<T>
{
  public abstract boolean isMatch(T paramT);
}
