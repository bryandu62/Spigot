package org.bukkit.command;

import java.util.List;

public abstract interface TabCompleter
{
  public abstract List<String> onTabComplete(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString);
}
