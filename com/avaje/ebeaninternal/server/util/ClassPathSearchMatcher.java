package com.avaje.ebeaninternal.server.util;

public abstract interface ClassPathSearchMatcher
{
  public abstract boolean isMatch(Class<?> paramClass);
}
