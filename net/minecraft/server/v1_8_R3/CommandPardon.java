package net.minecraft.server.v1_8_R3;

import com.mojang.authlib.GameProfile;
import java.util.List;

public class CommandPardon
  extends CommandAbstract
{
  public String getCommand()
  {
    return "pardon";
  }
  
  public int a()
  {
    return 3;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.unban.usage";
  }
  
  public boolean canUse(ICommandListener ☃)
  {
    return (MinecraftServer.getServer().getPlayerList().getProfileBans().isEnabled()) && (super.canUse(☃));
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if ((☃.length != 1) || (☃[0].length() <= 0)) {
      throw new ExceptionUsage("commands.unban.usage", new Object[0]);
    }
    MinecraftServer ☃ = MinecraftServer.getServer();
    GameProfile ☃ = ☃.getPlayerList().getProfileBans().a(☃[0]);
    if (☃ == null) {
      throw new CommandException("commands.unban.failed", new Object[] { ☃[0] });
    }
    ☃.getPlayerList().getProfileBans().remove(☃);
    a(☃, this, "commands.unban.success", new Object[] { ☃[0] });
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, MinecraftServer.getServer().getPlayerList().getProfileBans().getEntries());
    }
    return null;
  }
}
