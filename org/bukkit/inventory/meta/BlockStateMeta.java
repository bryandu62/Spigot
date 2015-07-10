package org.bukkit.inventory.meta;

import org.bukkit.block.BlockState;

public abstract interface BlockStateMeta
  extends ItemMeta
{
  public abstract boolean hasBlockState();
  
  public abstract BlockState getBlockState();
  
  public abstract void setBlockState(BlockState paramBlockState);
}
