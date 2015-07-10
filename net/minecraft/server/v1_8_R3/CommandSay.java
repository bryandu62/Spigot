package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandSay
  extends CommandAbstract
{
  public String getCommand()
  {
    return "say";
  }
  
  public int a()
  {
    return 1;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.say.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if ((☃.length <= 0) || (☃[0].length() <= 0)) {
      throw new ExceptionUsage("commands.say.usage", new Object[0]);
    }
    IChatBaseComponent ☃ = b(☃, ☃, 0, true);
    MinecraftServer.getServer().getPlayerList().sendMessage(new ChatMessage("chat.type.announcement", new Object[] { ☃.getScoreboardDisplayName(), ☃ }));
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length >= 1) {
      return a(☃, MinecraftServer.getServer().getPlayers());
    }
    return null;
  }
}
