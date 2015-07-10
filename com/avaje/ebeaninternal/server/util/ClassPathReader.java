package com.avaje.ebeaninternal.server.util;

public abstract interface ClassPathReader
{
  public abstract Object[] readPath(ClassLoader paramClassLoader);
}
