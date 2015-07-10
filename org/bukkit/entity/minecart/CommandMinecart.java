package org.bukkit.entity.minecart;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;

public abstract interface CommandMinecart
  extends Minecart, CommandSender
{
  public abstract String getCommand();
  
  public abstract void setCommand(String paramString);
  
  public abstract void setName(String paramString);
}
