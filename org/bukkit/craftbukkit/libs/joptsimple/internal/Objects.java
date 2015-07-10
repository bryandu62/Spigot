package org.bukkit.craftbukkit.libs.joptsimple.internal;

public final class Objects
{
  static
  {
    new Objects();
  }
  
  public static void ensureNotNull(Object target)
  {
    if (target == null) {
      throw new NullPointerException();
    }
  }
}
