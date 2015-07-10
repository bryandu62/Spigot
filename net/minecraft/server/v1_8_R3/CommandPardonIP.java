package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandPardonIP
  extends CommandAbstract
{
  public String getCommand()
  {
    return "pardon-ip";
  }
  
  public int a()
  {
    return 3;
  }
  
  public boolean canUse(ICommandListener ☃)
  {
    return (MinecraftServer.getServer().getPlayerList().getIPBans().isEnabled()) && (super.canUse(☃));
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.unbanip.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if ((☃.length != 1) || (☃[0].length() <= 1)) {
      throw new ExceptionUsage("commands.unbanip.usage", new Object[0]);
    }
    Matcher ☃ = CommandBanIp.a.matcher(☃[0]);
    if (☃.matches())
    {
      MinecraftServer.getServer().getPlayerList().getIPBans().remove(☃[0]);
      a(☃, this, "commands.unbanip.success", new Object[] { ☃[0] });
    }
    else
    {
      throw new ExceptionInvalidSyntax("commands.unbanip.invalid", new Object[0]);
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, MinecraftServer.getServer().getPlayerList().getIPBans().getEntries());
    }
    return null;
  }
}
