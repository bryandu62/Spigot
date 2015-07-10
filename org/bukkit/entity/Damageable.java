package org.bukkit.entity;

public abstract interface Damageable
  extends Entity
{
  public abstract void damage(double paramDouble);
  
  public abstract void damage(double paramDouble, Entity paramEntity);
  
  public abstract double getHealth();
  
  public abstract void setHealth(double paramDouble);
  
  public abstract double getMaxHealth();
  
  public abstract void setMaxHealth(double paramDouble);
  
  public abstract void resetMaxHealth();
}
