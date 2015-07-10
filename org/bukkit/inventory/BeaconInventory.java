package org.bukkit.inventory;

public abstract interface BeaconInventory
  extends Inventory
{
  public abstract void setItem(ItemStack paramItemStack);
  
  public abstract ItemStack getItem();
}
