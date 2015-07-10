package org.bukkit.entity;

public abstract interface FishHook
  extends Projectile
{
  public abstract double getBiteChance();
  
  public abstract void setBiteChance(double paramDouble)
    throws IllegalArgumentException;
}
