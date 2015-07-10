package org.bukkit.block;

import org.bukkit.SkullType;

public abstract interface Skull
  extends BlockState
{
  public abstract boolean hasOwner();
  
  public abstract String getOwner();
  
  public abstract boolean setOwner(String paramString);
  
  public abstract BlockFace getRotation();
  
  public abstract void setRotation(BlockFace paramBlockFace);
  
  public abstract SkullType getSkullType();
  
  public abstract void setSkullType(SkullType paramSkullType);
}
