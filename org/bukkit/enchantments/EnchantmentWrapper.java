package org.bukkit.enchantments;

import org.bukkit.inventory.ItemStack;

public class EnchantmentWrapper
  extends Enchantment
{
  public EnchantmentWrapper(int id)
  {
    super(id);
  }
  
  public Enchantment getEnchantment()
  {
    return Enchantment.getById(getId());
  }
  
  public int getMaxLevel()
  {
    return getEnchantment().getMaxLevel();
  }
  
  public int getStartLevel()
  {
    return getEnchantment().getStartLevel();
  }
  
  public EnchantmentTarget getItemTarget()
  {
    return getEnchantment().getItemTarget();
  }
  
  public boolean canEnchantItem(ItemStack item)
  {
    return getEnchantment().canEnchantItem(item);
  }
  
  public String getName()
  {
    return getEnchantment().getName();
  }
  
  public boolean conflictsWith(Enchantment other)
  {
    return getEnchantment().conflictsWith(other);
  }
}
