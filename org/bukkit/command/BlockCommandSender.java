package org.bukkit.command;

import org.bukkit.block.Block;

public abstract interface BlockCommandSender
  extends CommandSender
{
  public abstract Block getBlock();
}
