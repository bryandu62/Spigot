package org.bukkit.material;

import org.bukkit.block.BlockFace;

public abstract interface Attachable
  extends Directional
{
  public abstract BlockFace getAttachedFace();
}
