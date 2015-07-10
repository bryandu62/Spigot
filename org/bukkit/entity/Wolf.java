package org.bukkit.entity;

import org.bukkit.DyeColor;

public abstract interface Wolf
  extends Animals, Tameable
{
  public abstract boolean isAngry();
  
  public abstract void setAngry(boolean paramBoolean);
  
  public abstract boolean isSitting();
  
  public abstract void setSitting(boolean paramBoolean);
  
  public abstract DyeColor getCollarColor();
  
  public abstract void setCollarColor(DyeColor paramDyeColor);
}
