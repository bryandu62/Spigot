package org.bukkit.command;

public abstract interface ProxiedCommandSender
  extends CommandSender
{
  public abstract CommandSender getCaller();
  
  public abstract CommandSender getCallee();
}
