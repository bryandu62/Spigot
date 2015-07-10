package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;

public abstract interface Item
  extends Entity
{
  public abstract ItemStack getItemStack();
  
  public abstract void setItemStack(ItemStack paramItemStack);
  
  public abstract int getPickupDelay();
  
  public abstract void setPickupDelay(int paramInt);
}
