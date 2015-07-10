package org.bukkit.enchantments;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum EnchantmentTarget
{
  ALL,  ARMOR,  ARMOR_FEET,  ARMOR_LEGS,  ARMOR_TORSO,  ARMOR_HEAD,  WEAPON,  TOOL,  BOW,  FISHING_ROD;
  
  public abstract boolean includes(Material paramMaterial);
  
  public boolean includes(ItemStack item)
  {
    return includes(item.getType());
  }
}
