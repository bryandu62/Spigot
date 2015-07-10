package org.spigotmc;

public class SneakyThrow
{
  public static void sneaky(Throwable t)
  {
    throw ((RuntimeException)superSneaky(t));
  }
  
  private static <T extends Throwable> T superSneaky(Throwable t)
    throws Throwable
  {
    throw t;
  }
}
