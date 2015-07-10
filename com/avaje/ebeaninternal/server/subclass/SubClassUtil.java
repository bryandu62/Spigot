package com.avaje.ebeaninternal.server.subclass;

public class SubClassUtil
  implements GenSuffix
{
  public static boolean isSubClass(String className)
  {
    return className.lastIndexOf("$$EntityBean") != -1;
  }
  
  public static String getSuperClassName(String className)
  {
    int dPos = className.lastIndexOf("$$EntityBean");
    if (dPos > -1) {
      return className.substring(0, dPos);
    }
    return className;
  }
}
