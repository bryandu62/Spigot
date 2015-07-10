package org.bukkit.inventory.meta;

public abstract interface Repairable
{
  public abstract boolean hasRepairCost();
  
  public abstract int getRepairCost();
  
  public abstract void setRepairCost(int paramInt);
  
  public abstract Repairable clone();
}
