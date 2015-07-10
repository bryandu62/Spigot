package org.bukkit.entity;

import org.bukkit.material.Colorable;

public abstract interface Sheep
  extends Animals, Colorable
{
  public abstract boolean isSheared();
  
  public abstract void setSheared(boolean paramBoolean);
}
