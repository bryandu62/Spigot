package org.bukkit.entity;

public abstract interface PigZombie
  extends Zombie
{
  public abstract int getAnger();
  
  public abstract void setAnger(int paramInt);
  
  public abstract void setAngry(boolean paramBoolean);
  
  public abstract boolean isAngry();
}
