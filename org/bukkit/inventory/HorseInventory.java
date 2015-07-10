package org.bukkit.inventory;

public abstract interface HorseInventory
  extends Inventory
{
  public abstract ItemStack getSaddle();
  
  public abstract ItemStack getArmor();
  
  public abstract void setSaddle(ItemStack paramItemStack);
  
  public abstract void setArmor(ItemStack paramItemStack);
}
