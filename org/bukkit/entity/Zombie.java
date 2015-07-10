package org.bukkit.entity;

public abstract interface Zombie
  extends Monster
{
  public abstract boolean isBaby();
  
  public abstract void setBaby(boolean paramBoolean);
  
  public abstract boolean isVillager();
  
  public abstract void setVillager(boolean paramBoolean);
}
