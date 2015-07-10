package org.bukkit.inventory;

import org.bukkit.entity.Entity;

public abstract interface EntityEquipment
{
  public abstract ItemStack getItemInHand();
  
  public abstract void setItemInHand(ItemStack paramItemStack);
  
  public abstract ItemStack getHelmet();
  
  public abstract void setHelmet(ItemStack paramItemStack);
  
  public abstract ItemStack getChestplate();
  
  public abstract void setChestplate(ItemStack paramItemStack);
  
  public abstract ItemStack getLeggings();
  
  public abstract void setLeggings(ItemStack paramItemStack);
  
  public abstract ItemStack getBoots();
  
  public abstract void setBoots(ItemStack paramItemStack);
  
  public abstract ItemStack[] getArmorContents();
  
  public abstract void setArmorContents(ItemStack[] paramArrayOfItemStack);
  
  public abstract void clear();
  
  public abstract float getItemInHandDropChance();
  
  public abstract void setItemInHandDropChance(float paramFloat);
  
  public abstract float getHelmetDropChance();
  
  public abstract void setHelmetDropChance(float paramFloat);
  
  public abstract float getChestplateDropChance();
  
  public abstract void setChestplateDropChance(float paramFloat);
  
  public abstract float getLeggingsDropChance();
  
  public abstract void setLeggingsDropChance(float paramFloat);
  
  public abstract float getBootsDropChance();
  
  public abstract void setBootsDropChance(float paramFloat);
  
  public abstract Entity getHolder();
}
