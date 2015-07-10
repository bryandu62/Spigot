package net.minecraft.server.v1_8_R3;

import com.mojang.authlib.GameProfile;
import java.util.List;

public class CommandBan
  extends CommandAbstract
{
  public String getCommand()
  {
    return "ban";
  }
  
  public int a()
  {
    return 3;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.ban.usage";
  }
  
  public boolean canUse(ICommandListener ☃)
  {
    return (MinecraftServer.getServer().getPlayerList().getProfileBans().isEnabled()) && (super.canUse(☃));
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if ((☃.length < 1) || (☃[0].length() <= 0)) {
      throw new ExceptionUsage("commands.ban.usage", new Object[0]);
    }
    MinecraftServer ☃ = MinecraftServer.getServer();
    GameProfile ☃ = ☃.getUserCache().getProfile(☃[0]);
    if (☃ == null) {
      throw new CommandException("commands.ban.failed", new Object[] { ☃[0] });
    }
    String ☃ = null;
    if (☃.length >= 2) {
      ☃ = a(☃, ☃, 1).c();
    }
    GameProfileBanEntry ☃ = new GameProfileBanEntry(☃, null, ☃.getName(), null, ☃);
    ☃.getPlayerList().getProfileBans().add(☃);
    
    EntityPlayer ☃ = ☃.getPlayerList().getPlayer(☃[0]);
    if (☃ != null) {
      ☃.playerConnection.disconnect("You are banned from this server.");
    }
    a(☃, this, "commands.ban.success", new Object[] { ☃[0] });
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length >= 1) {
      return a(☃, MinecraftServer.getServer().getPlayers());
    }
    return null;
  }
}
