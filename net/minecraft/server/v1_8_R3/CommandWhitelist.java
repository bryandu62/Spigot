package net.minecraft.server.v1_8_R3;

import com.mojang.authlib.GameProfile;
import java.util.List;

public class CommandWhitelist
  extends CommandAbstract
{
  public String getCommand()
  {
    return "whitelist";
  }
  
  public int a()
  {
    return 3;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.whitelist.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 1) {
      throw new ExceptionUsage("commands.whitelist.usage", new Object[0]);
    }
    MinecraftServer ☃ = MinecraftServer.getServer();
    if (☃[0].equals("on"))
    {
      ☃.getPlayerList().setHasWhitelist(true);
      a(☃, this, "commands.whitelist.enabled", new Object[0]);
    }
    else if (☃[0].equals("off"))
    {
      ☃.getPlayerList().setHasWhitelist(false);
      a(☃, this, "commands.whitelist.disabled", new Object[0]);
    }
    else if (☃[0].equals("list"))
    {
      ☃.sendMessage(new ChatMessage("commands.whitelist.list", new Object[] { Integer.valueOf(☃.getPlayerList().getWhitelisted().length), Integer.valueOf(☃.getPlayerList().getSeenPlayers().length) }));
      String[] ☃ = ☃.getPlayerList().getWhitelisted();
      ☃.sendMessage(new ChatComponentText(a(☃)));
    }
    else if (☃[0].equals("add"))
    {
      if (☃.length < 2) {
        throw new ExceptionUsage("commands.whitelist.add.usage", new Object[0]);
      }
      GameProfile ☃ = ☃.getUserCache().getProfile(☃[1]);
      if (☃ == null) {
        throw new CommandException("commands.whitelist.add.failed", new Object[] { ☃[1] });
      }
      ☃.getPlayerList().addWhitelist(☃);
      a(☃, this, "commands.whitelist.add.success", new Object[] { ☃[1] });
    }
    else if (☃[0].equals("remove"))
    {
      if (☃.length < 2) {
        throw new ExceptionUsage("commands.whitelist.remove.usage", new Object[0]);
      }
      GameProfile ☃ = ☃.getPlayerList().getWhitelist().a(☃[1]);
      if (☃ == null) {
        throw new CommandException("commands.whitelist.remove.failed", new Object[] { ☃[1] });
      }
      ☃.getPlayerList().removeWhitelist(☃);
      a(☃, this, "commands.whitelist.remove.success", new Object[] { ☃[1] });
    }
    else if (☃[0].equals("reload"))
    {
      ☃.getPlayerList().reloadWhitelist();
      a(☃, this, "commands.whitelist.reloaded", new Object[0]);
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, new String[] { "on", "off", "list", "add", "remove", "reload" });
    }
    if (☃.length == 2)
    {
      if (☃[0].equals("remove")) {
        return a(☃, MinecraftServer.getServer().getPlayerList().getWhitelisted());
      }
      if (☃[0].equals("add")) {
        return a(☃, MinecraftServer.getServer().getUserCache().a());
      }
    }
    return null;
  }
}
