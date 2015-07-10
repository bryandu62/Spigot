package org.bukkit.craftbukkit.v1_8_R3.util;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class ServerShutdownThread
  extends Thread
{
  private final MinecraftServer server;
  
  public ServerShutdownThread(MinecraftServer server)
  {
    this.server = server;
  }
  
  /* Error */
  public void run()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 14	org/bukkit/craftbukkit/v1_8_R3/util/ServerShutdownThread:server	Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   4: invokevirtual 26	net/minecraft/server/v1_8_R3/MinecraftServer:stop	()V
    //   7: goto +52 -> 59
    //   10: astore_1
    //   11: aload_1
    //   12: invokevirtual 29	net/minecraft/server/v1_8_R3/ExceptionWorldConflict:printStackTrace	()V
    //   15: aload_0
    //   16: getfield 14	org/bukkit/craftbukkit/v1_8_R3/util/ServerShutdownThread:server	Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   19: getfield 33	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   22: invokevirtual 39	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   25: invokeinterface 44 1 0
    //   30: goto +48 -> 78
    //   33: pop
    //   34: goto +44 -> 78
    //   37: astore_2
    //   38: aload_0
    //   39: getfield 14	org/bukkit/craftbukkit/v1_8_R3/util/ServerShutdownThread:server	Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   42: getfield 33	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   45: invokevirtual 39	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   48: invokeinterface 44 1 0
    //   53: goto +4 -> 57
    //   56: pop
    //   57: aload_2
    //   58: athrow
    //   59: aload_0
    //   60: getfield 14	org/bukkit/craftbukkit/v1_8_R3/util/ServerShutdownThread:server	Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   63: getfield 33	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   66: invokevirtual 39	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   69: invokeinterface 44 1 0
    //   74: goto +4 -> 78
    //   77: pop
    //   78: return
    // Line number table:
    //   Java source line #16	-> byte code offset #0
    //   Java source line #17	-> byte code offset #7
    //   Java source line #18	-> byte code offset #11
    //   Java source line #21	-> byte code offset #15
    //   Java source line #22	-> byte code offset #30
    //   Java source line #19	-> byte code offset #37
    //   Java source line #21	-> byte code offset #38
    //   Java source line #22	-> byte code offset #53
    //   Java source line #24	-> byte code offset #57
    //   Java source line #21	-> byte code offset #59
    //   Java source line #22	-> byte code offset #74
    //   Java source line #25	-> byte code offset #78
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	79	0	this	ServerShutdownThread
    //   10	2	1	ex	net.minecraft.server.v1_8_R3.ExceptionWorldConflict
    //   37	21	2	localObject	Object
    //   33	1	3	localException1	Exception
    //   56	1	4	localException2	Exception
    //   77	1	5	localException3	Exception
    // Exception table:
    //   from	to	target	type
    //   0	7	10	net/minecraft/server/v1_8_R3/ExceptionWorldConflict
    //   15	30	33	java/lang/Exception
    //   0	15	37	finally
    //   38	53	56	java/lang/Exception
    //   59	74	77	java/lang/Exception
  }
}
