package org.spigotmc;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class AsyncCatcher
{
  public static boolean enabled = true;
  
  public static void catchOp(String reason)
  {
    if ((enabled) && (Thread.currentThread() != MinecraftServer.getServer().primaryThread)) {
      throw new IllegalStateException("Asynchronous " + reason + "!");
    }
  }
}
