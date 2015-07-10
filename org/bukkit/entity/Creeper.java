package org.bukkit.entity;

public abstract interface Creeper
  extends Monster
{
  public abstract boolean isPowered();
  
  public abstract void setPowered(boolean paramBoolean);
}
