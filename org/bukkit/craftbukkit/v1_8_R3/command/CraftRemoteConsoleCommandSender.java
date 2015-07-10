package org.bukkit.craftbukkit.v1_8_R3.command;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.RemoteControlCommandListener;
import org.bukkit.command.RemoteConsoleCommandSender;

public class CraftRemoteConsoleCommandSender
  extends ServerCommandSender
  implements RemoteConsoleCommandSender
{
  public void sendMessage(String message)
  {
    RemoteControlCommandListener.getInstance().sendMessage(new ChatComponentText(message + "\n"));
  }
  
  public void sendMessage(String[] messages)
  {
    String[] arrayOfString;
    int i = (arrayOfString = messages).length;
    for (int j = 0; j < i; j++)
    {
      String message = arrayOfString[j];
      sendMessage(message);
    }
  }
  
  public String getName()
  {
    return "Rcon";
  }
  
  public boolean isOp()
  {
    return true;
  }
  
  public void setOp(boolean value)
  {
    throw new UnsupportedOperationException("Cannot change operator status of remote controller.");
  }
}
