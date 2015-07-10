package com.avaje.ebeaninternal.server.util;

public class InternalAssert
{
  public static void notNull(Object o, String msg)
  {
    if (o == null) {
      throw new IllegalStateException(msg);
    }
  }
  
  public static void isTrue(boolean b, String msg)
  {
    if (!b) {
      throw new IllegalStateException(msg);
    }
  }
}
