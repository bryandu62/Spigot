package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandBanList
  extends CommandAbstract
{
  public String getCommand()
  {
    return "banlist";
  }
  
  public int a()
  {
    return 3;
  }
  
  public boolean canUse(ICommandListener ☃)
  {
    return ((MinecraftServer.getServer().getPlayerList().getIPBans().isEnabled()) || (MinecraftServer.getServer().getPlayerList().getProfileBans().isEnabled())) && (super.canUse(☃));
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.banlist.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if ((☃.length >= 1) && (☃[0].equalsIgnoreCase("ips")))
    {
      ☃.sendMessage(new ChatMessage("commands.banlist.ips", new Object[] { Integer.valueOf(MinecraftServer.getServer().getPlayerList().getIPBans().getEntries().length) }));
      ☃.sendMessage(new ChatComponentText(a(MinecraftServer.getServer().getPlayerList().getIPBans().getEntries())));
    }
    else
    {
      ☃.sendMessage(new ChatMessage("commands.banlist.players", new Object[] { Integer.valueOf(MinecraftServer.getServer().getPlayerList().getProfileBans().getEntries().length) }));
      ☃.sendMessage(new ChatComponentText(a(MinecraftServer.getServer().getPlayerList().getProfileBans().getEntries())));
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, new String[] { "players", "ips" });
    }
    return null;
  }
}
