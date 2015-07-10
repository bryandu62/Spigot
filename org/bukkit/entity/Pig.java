package org.bukkit.entity;

public abstract interface Pig
  extends Animals, Vehicle
{
  public abstract boolean hasSaddle();
  
  public abstract void setSaddle(boolean paramBoolean);
}
