package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandKill
  extends CommandAbstract
{
  public String getCommand()
  {
    return "kill";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.kill.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length == 0)
    {
      EntityHuman ☃ = b(☃);
      ☃.G();
      a(☃, this, "commands.kill.successful", new Object[] { ☃.getScoreboardDisplayName() });
      return;
    }
    Entity ☃ = b(☃, ☃[0]);
    ☃.G();
    a(☃, this, "commands.kill.successful", new Object[] { ☃.getScoreboardDisplayName() });
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 0;
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, MinecraftServer.getServer().getPlayers());
    }
    return null;
  }
}
