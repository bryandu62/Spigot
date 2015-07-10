package org.bukkit.command;

import java.util.List;

@Deprecated
public abstract interface TabCommandExecutor
  extends CommandExecutor
{
  public abstract List<String> onTabComplete();
}
