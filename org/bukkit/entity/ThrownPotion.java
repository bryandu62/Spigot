package org.bukkit.entity;

import java.util.Collection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public abstract interface ThrownPotion
  extends Projectile
{
  public abstract Collection<PotionEffect> getEffects();
  
  public abstract ItemStack getItem();
  
  public abstract void setItem(ItemStack paramItemStack);
}
