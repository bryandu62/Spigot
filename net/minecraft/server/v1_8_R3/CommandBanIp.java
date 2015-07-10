package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandBanIp
  extends CommandAbstract
{
  public static final Pattern a = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
  
  public String getCommand()
  {
    return "ban-ip";
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
    return "commands.banip.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if ((☃.length < 1) || (☃[0].length() <= 1)) {
      throw new ExceptionUsage("commands.banip.usage", new Object[0]);
    }
    IChatBaseComponent ☃ = ☃.length >= 2 ? a(☃, ☃, 1) : null;
    
    Matcher ☃ = a.matcher(☃[0]);
    if (☃.matches())
    {
      a(☃, ☃[0], ☃ == null ? null : ☃.c());
    }
    else
    {
      EntityPlayer ☃ = MinecraftServer.getServer().getPlayerList().getPlayer(☃[0]);
      if (☃ == null) {
        throw new ExceptionPlayerNotFound("commands.banip.invalid", new Object[0]);
      }
      a(☃, ☃.w(), ☃ == null ? null : ☃.c());
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, MinecraftServer.getServer().getPlayers());
    }
    return null;
  }
  
  protected void a(ICommandListener ☃, String ☃, String ☃)
  {
    IpBanEntry ☃ = new IpBanEntry(☃, null, ☃.getName(), null, ☃);
    MinecraftServer.getServer().getPlayerList().getIPBans().add(☃);
    
    List<EntityPlayer> ☃ = MinecraftServer.getServer().getPlayerList().b(☃);
    String[] ☃ = new String[☃.size()];
    int ☃ = 0;
    for (EntityPlayer ☃ : ☃)
    {
      ☃.playerConnection.disconnect("You have been IP banned.");
      ☃[(☃++)] = ☃.getName();
    }
    if (☃.isEmpty()) {
      a(☃, this, "commands.banip.success", new Object[] { ☃ });
    } else {
      a(☃, this, "commands.banip.success.players", new Object[] { ☃, a(☃) });
    }
  }
}
