package org.bukkit.craftbukkit.libs.joptsimple.internal;

public final class Classes
{
  static
  {
    new Classes();
  }
  
  public static String shortNameOf(String className)
  {
    return className.substring(className.lastIndexOf('.') + 1);
  }
}
