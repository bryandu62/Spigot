package org.bukkit.inventory;

import org.bukkit.entity.HumanEntity;

public abstract interface PlayerInventory
  extends Inventory
{
  public abstract ItemStack[] getArmorContents();
  
  public abstract ItemStack getHelmet();
  
  public abstract ItemStack getChestplate();
  
  public abstract ItemStack getLeggings();
  
  public abstract ItemStack getBoots();
  
  public abstract void setItem(int paramInt, ItemStack paramItemStack);
  
  public abstract void setArmorContents(ItemStack[] paramArrayOfItemStack);
  
  public abstract void setHelmet(ItemStack paramItemStack);
  
  public abstract void setChestplate(ItemStack paramItemStack);
  
  public abstract void setLeggings(ItemStack paramItemStack);
  
  public abstract void setBoots(ItemStack paramItemStack);
  
  public abstract ItemStack getItemInHand();
  
  public abstract void setItemInHand(ItemStack paramItemStack);
  
  public abstract int getHeldItemSlot();
  
  public abstract void setHeldItemSlot(int paramInt);
  
  @Deprecated
  public abstract int clear(int paramInt1, int paramInt2);
  
  public abstract HumanEntity getHolder();
}
