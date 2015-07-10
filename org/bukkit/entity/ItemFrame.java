package org.bukkit.entity;

import org.bukkit.Rotation;
import org.bukkit.inventory.ItemStack;

public abstract interface ItemFrame
  extends Hanging
{
  public abstract ItemStack getItem();
  
  public abstract void setItem(ItemStack paramItemStack);
  
  public abstract Rotation getRotation();
  
  public abstract void setRotation(Rotation paramRotation)
    throws IllegalArgumentException;
}
