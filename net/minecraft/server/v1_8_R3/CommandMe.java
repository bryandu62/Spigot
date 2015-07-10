package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandMe
  extends CommandAbstract
{
  public String getCommand()
  {
    return "me";
  }
  
  public int a()
  {
    return 0;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.me.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length <= 0) {
      throw new ExceptionUsage("commands.me.usage", new Object[0]);
    }
    IChatBaseComponent ☃ = b(☃, ☃, 0, !(☃ instanceof EntityHuman));
    MinecraftServer.getServer().getPlayerList().sendMessage(new ChatMessage("chat.type.emote", new Object[] { ☃.getScoreboardDisplayName(), ☃ }));
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    return a(☃, MinecraftServer.getServer().getPlayers());
  }
}
