package net.minecraft.server.v1_8_R3;

import java.util.Arrays;
import java.util.List;

public class CommandTell
  extends CommandAbstract
{
  public List<String> b()
  {
    return Arrays.asList(new String[] { "w", "msg" });
  }
  
  public String getCommand()
  {
    return "tell";
  }
  
  public int a()
  {
    return 0;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.message.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 2) {
      throw new ExceptionUsage("commands.message.usage", new Object[0]);
    }
    EntityHuman ☃ = a(☃, ☃[0]);
    if (☃ == ☃) {
      throw new ExceptionPlayerNotFound("commands.message.sameTarget", new Object[0]);
    }
    IChatBaseComponent ☃ = b(☃, ☃, 1, !(☃ instanceof EntityHuman));
    ChatMessage ☃ = new ChatMessage("commands.message.display.incoming", new Object[] { ☃.getScoreboardDisplayName(), ☃.f() });
    ChatMessage ☃ = new ChatMessage("commands.message.display.outgoing", new Object[] { ☃.getScoreboardDisplayName(), ☃.f() });
    ☃.getChatModifier().setColor(EnumChatFormat.GRAY).setItalic(Boolean.valueOf(true));
    ☃.getChatModifier().setColor(EnumChatFormat.GRAY).setItalic(Boolean.valueOf(true));
    ☃.sendMessage(☃);
    ☃.sendMessage(☃);
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    return a(☃, MinecraftServer.getServer().getPlayers());
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 0;
  }
}
