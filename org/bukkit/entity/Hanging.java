package org.bukkit.entity;

import org.bukkit.block.BlockFace;
import org.bukkit.material.Attachable;

public abstract interface Hanging
  extends Entity, Attachable
{
  public abstract boolean setFacingDirection(BlockFace paramBlockFace, boolean paramBoolean);
}
