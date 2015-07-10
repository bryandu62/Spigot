package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonParseException;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandTitle
  extends CommandAbstract
{
  private static final Logger a = ;
  
  public String getCommand()
  {
    return "title";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.title.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 2) {
      throw new ExceptionUsage("commands.title.usage", new Object[0]);
    }
    if (☃.length < 3)
    {
      if (("title".equals(☃[1])) || ("subtitle".equals(☃[1]))) {
        throw new ExceptionUsage("commands.title.usage.title", new Object[0]);
      }
      if ("times".equals(☃[1])) {
        throw new ExceptionUsage("commands.title.usage.times", new Object[0]);
      }
    }
    EntityPlayer ☃ = a(☃, ☃[0]);
    PacketPlayOutTitle.EnumTitleAction ☃ = PacketPlayOutTitle.EnumTitleAction.a(☃[1]);
    if ((☃ == PacketPlayOutTitle.EnumTitleAction.CLEAR) || (☃ == PacketPlayOutTitle.EnumTitleAction.RESET))
    {
      if (☃.length != 2) {
        throw new ExceptionUsage("commands.title.usage", new Object[0]);
      }
      PacketPlayOutTitle ☃ = new PacketPlayOutTitle(☃, null);
      ☃.playerConnection.sendPacket(☃);
      a(☃, this, "commands.title.success", new Object[0]);
      return;
    }
    if (☃ == PacketPlayOutTitle.EnumTitleAction.TIMES)
    {
      if (☃.length != 5) {
        throw new ExceptionUsage("commands.title.usage", new Object[0]);
      }
      int ☃ = a(☃[2]);
      int ☃ = a(☃[3]);
      int ☃ = a(☃[4]);
      PacketPlayOutTitle ☃ = new PacketPlayOutTitle(☃, ☃, ☃);
      ☃.playerConnection.sendPacket(☃);
      a(☃, this, "commands.title.success", new Object[0]);
      return;
    }
    if (☃.length < 3) {
      throw new ExceptionUsage("commands.title.usage", new Object[0]);
    }
    String ☃ = a(☃, 2);
    IChatBaseComponent ☃;
    try
    {
      ☃ = IChatBaseComponent.ChatSerializer.a(☃);
    }
    catch (JsonParseException ☃)
    {
      Throwable ☃ = ExceptionUtils.getRootCause(☃);
      throw new ExceptionInvalidSyntax("commands.tellraw.jsonException", new Object[] { ☃ == null ? "" : ☃.getMessage() });
    }
    PacketPlayOutTitle ☃ = new PacketPlayOutTitle(☃, ChatComponentUtils.filterForDisplay(☃, ☃, ☃));
    ☃.playerConnection.sendPacket(☃);
    a(☃, this, "commands.title.success", new Object[0]);
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, MinecraftServer.getServer().getPlayers());
    }
    if (☃.length == 2) {
      return a(☃, PacketPlayOutTitle.EnumTitleAction.a());
    }
    return null;
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 0;
  }
}
