package org.bukkit.material;

import org.bukkit.block.BlockFace;

public abstract interface Directional
{
  public abstract void setFacingDirection(BlockFace paramBlockFace);
  
  public abstract BlockFace getFacing();
}
