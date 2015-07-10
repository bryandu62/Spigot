package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.List;

public class CommandOp
  extends CommandAbstract
{
  public String getCommand()
  {
    return "op";
  }
  
  public int a()
  {
    return 3;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.op.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if ((☃.length != 1) || (☃[0].length() <= 0)) {
      throw new ExceptionUsage("commands.op.usage", new Object[0]);
    }
    MinecraftServer ☃ = MinecraftServer.getServer();
    GameProfile ☃ = ☃.getUserCache().getProfile(☃[0]);
    if (☃ == null) {
      throw new CommandException("commands.op.failed", new Object[] { ☃[0] });
    }
    ☃.getPlayerList().addOp(☃);
    a(☃, this, "commands.op.success", new Object[] { ☃[0] });
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1)
    {
      String ☃ = ☃[(☃.length - 1)];
      List<String> ☃ = Lists.newArrayList();
      for (GameProfile ☃ : MinecraftServer.getServer().L()) {
        if ((!MinecraftServer.getServer().getPlayerList().isOp(☃)) && (a(☃, ☃.getName()))) {
          ☃.add(☃.getName());
        }
      }
      return ☃;
    }
    return null;
  }
}
