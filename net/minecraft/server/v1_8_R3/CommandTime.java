package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandTime
  extends CommandAbstract
{
  public String getCommand()
  {
    return "time";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.time.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length > 1)
    {
      if (☃[0].equals("set"))
      {
        int ☃;
        int ☃;
        if (☃[1].equals("day"))
        {
          ☃ = 1000;
        }
        else
        {
          int ☃;
          if (☃[1].equals("night")) {
            ☃ = 13000;
          } else {
            ☃ = a(☃[1], 0);
          }
        }
        a(☃, ☃);
        a(☃, this, "commands.time.set", new Object[] { Integer.valueOf(☃) });
        return;
      }
      if (☃[0].equals("add"))
      {
        int ☃ = a(☃[1], 0);
        b(☃, ☃);
        a(☃, this, "commands.time.added", new Object[] { Integer.valueOf(☃) });
        return;
      }
      if (☃[0].equals("query"))
      {
        if (☃[1].equals("daytime"))
        {
          int ☃ = (int)(☃.getWorld().getDayTime() % 2147483647L);
          ☃.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, ☃);
          a(☃, this, "commands.time.query", new Object[] { Integer.valueOf(☃) });
          return;
        }
        if (☃[1].equals("gametime"))
        {
          int ☃ = (int)(☃.getWorld().getTime() % 2147483647L);
          ☃.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, ☃);
          a(☃, this, "commands.time.query", new Object[] { Integer.valueOf(☃) });
          return;
        }
      }
    }
    throw new ExceptionUsage("commands.time.usage", new Object[0]);
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, new String[] { "set", "add", "query" });
    }
    if ((☃.length == 2) && (☃[0].equals("set"))) {
      return a(☃, new String[] { "day", "night" });
    }
    if ((☃.length == 2) && (☃[0].equals("query"))) {
      return a(☃, new String[] { "daytime", "gametime" });
    }
    return null;
  }
  
  protected void a(ICommandListener ☃, int ☃)
  {
    for (int ☃ = 0; ☃ < MinecraftServer.getServer().worldServer.length; ☃++) {
      MinecraftServer.getServer().worldServer[☃].setDayTime(☃);
    }
  }
  
  protected void b(ICommandListener ☃, int ☃)
  {
    for (int ☃ = 0; ☃ < MinecraftServer.getServer().worldServer.length; ☃++)
    {
      WorldServer ☃ = MinecraftServer.getServer().worldServer[☃];
      ☃.setDayTime(☃.getDayTime() + ☃);
    }
  }
}
