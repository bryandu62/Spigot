package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonParseException;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class CommandTellRaw
  extends CommandAbstract
{
  public String getCommand()
  {
    return "tellraw";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.tellraw.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 2) {
      throw new ExceptionUsage("commands.tellraw.usage", new Object[0]);
    }
    EntityHuman ☃ = a(☃, ☃[0]);
    String ☃ = a(☃, 1);
    try
    {
      IChatBaseComponent ☃ = IChatBaseComponent.ChatSerializer.a(☃);
      ☃.sendMessage(ChatComponentUtils.filterForDisplay(☃, ☃, ☃));
    }
    catch (JsonParseException ☃)
    {
      Throwable ☃ = ExceptionUtils.getRootCause(☃);
      throw new ExceptionInvalidSyntax("commands.tellraw.jsonException", new Object[] { ☃ == null ? "" : ☃.getMessage() });
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, MinecraftServer.getServer().getPlayers());
    }
    return null;
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 0;
  }
}
