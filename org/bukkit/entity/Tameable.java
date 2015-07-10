package org.bukkit.entity;

public abstract interface Tameable
{
  public abstract boolean isTamed();
  
  public abstract void setTamed(boolean paramBoolean);
  
  public abstract AnimalTamer getOwner();
  
  public abstract void setOwner(AnimalTamer paramAnimalTamer);
}
