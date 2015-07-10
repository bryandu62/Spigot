package net.minecraft.server.v1_8_R3;

import com.mojang.authlib.GameProfile;
import java.util.List;

public class CommandDeop
  extends CommandAbstract
{
  public String getCommand()
  {
    return "deop";
  }
  
  public int a()
  {
    return 3;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.deop.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if ((☃.length != 1) || (☃[0].length() <= 0)) {
      throw new ExceptionUsage("commands.deop.usage", new Object[0]);
    }
    MinecraftServer ☃ = MinecraftServer.getServer();
    GameProfile ☃ = ☃.getPlayerList().getOPs().a(☃[0]);
    if (☃ == null) {
      throw new CommandException("commands.deop.failed", new Object[] { ☃[0] });
    }
    ☃.getPlayerList().removeOp(☃);
    a(☃, this, "commands.deop.success", new Object[] { ☃[0] });
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, MinecraftServer.getServer().getPlayerList().n());
    }
    return null;
  }
}
