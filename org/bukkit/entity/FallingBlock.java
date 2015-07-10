package org.bukkit.entity;

import org.bukkit.Material;

public abstract interface FallingBlock
  extends Entity
{
  public abstract Material getMaterial();
  
  @Deprecated
  public abstract int getBlockId();
  
  @Deprecated
  public abstract byte getBlockData();
  
  public abstract boolean getDropItem();
  
  public abstract void setDropItem(boolean paramBoolean);
}
