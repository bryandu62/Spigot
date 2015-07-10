package org.bukkit.entity;

public abstract interface TNTPrimed
  extends Explosive
{
  public abstract void setFuseTicks(int paramInt);
  
  public abstract int getFuseTicks();
  
  public abstract Entity getSource();
}
