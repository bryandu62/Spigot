package org.bukkit.entity;

public abstract interface Guardian
  extends Monster
{
  public abstract boolean isElder();
  
  public abstract void setElder(boolean paramBoolean);
}
