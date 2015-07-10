package org.bukkit.entity;

public abstract interface Explosive
  extends Entity
{
  public abstract void setYield(float paramFloat);
  
  public abstract float getYield();
  
  public abstract void setIsIncendiary(boolean paramBoolean);
  
  public abstract boolean isIncendiary();
}
