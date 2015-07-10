package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandKick
  extends CommandAbstract
{
  public String getCommand()
  {
    return "kick";
  }
  
  public int a()
  {
    return 3;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.kick.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if ((☃.length <= 0) || (☃[0].length() <= 1)) {
      throw new ExceptionUsage("commands.kick.usage", new Object[0]);
    }
    EntityPlayer ☃ = MinecraftServer.getServer().getPlayerList().getPlayer(☃[0]);
    String ☃ = "Kicked by an operator.";
    boolean ☃ = false;
    if (☃ == null) {
      throw new ExceptionPlayerNotFound();
    }
    if (☃.length >= 2)
    {
      ☃ = a(☃, ☃, 1).c();
      ☃ = true;
    }
    ☃.playerConnection.disconnect(☃);
    if (☃) {
      a(☃, this, "commands.kick.success.reason", new Object[] { ☃.getName(), ☃ });
    } else {
      a(☃, this, "commands.kick.success", new Object[] { ☃.getName() });
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length >= 1) {
      return a(☃, MinecraftServer.getServer().getPlayers());
    }
    return null;
  }
}
