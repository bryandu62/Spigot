package org.bukkit.craftbukkit.v1_8_R3.enchantments;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class CraftEnchantment
  extends org.bukkit.enchantments.Enchantment
{
  private final net.minecraft.server.v1_8_R3.Enchantment target;
  
  public CraftEnchantment(net.minecraft.server.v1_8_R3.Enchantment target)
  {
    super(target.id);
    this.target = target;
  }
  
  public int getMaxLevel()
  {
    return this.target.getMaxLevel();
  }
  
  public int getStartLevel()
  {
    return this.target.getStartLevel();
  }
  
  public EnchantmentTarget getItemTarget()
  {
    switch (this.target.slot)
    {
    case ALL: 
      return EnchantmentTarget.ALL;
    case ARMOR: 
      return EnchantmentTarget.ARMOR;
    case ARMOR_FEET: 
      return EnchantmentTarget.ARMOR_FEET;
    case ARMOR_TORSO: 
      return EnchantmentTarget.ARMOR_HEAD;
    case ARMOR_HEAD: 
      return EnchantmentTarget.ARMOR_LEGS;
    case ARMOR_LEGS: 
      return EnchantmentTarget.ARMOR_TORSO;
    case BREAKABLE: 
      return EnchantmentTarget.TOOL;
    case BOW: 
      return EnchantmentTarget.WEAPON;
    case WEAPON: 
      return EnchantmentTarget.BOW;
    case DIGGER: 
      return EnchantmentTarget.FISHING_ROD;
    }
    return null;
  }
  
  public boolean canEnchantItem(ItemStack item)
  {
    return this.target.canEnchant(CraftItemStack.asNMSCopy(item));
  }
  
  public String getName()
  {
    switch (this.target.id)
    {
    case 0: 
      return "PROTECTION_ENVIRONMENTAL";
    case 1: 
      return "PROTECTION_FIRE";
    case 2: 
      return "PROTECTION_FALL";
    case 3: 
      return "PROTECTION_EXPLOSIONS";
    case 4: 
      return "PROTECTION_PROJECTILE";
    case 5: 
      return "OXYGEN";
    case 6: 
      return "WATER_WORKER";
    case 7: 
      return "THORNS";
    case 8: 
      return "DEPTH_STRIDER";
    case 16: 
      return "DAMAGE_ALL";
    case 17: 
      return "DAMAGE_UNDEAD";
    case 18: 
      return "DAMAGE_ARTHROPODS";
    case 19: 
      return "KNOCKBACK";
    case 20: 
      return "FIRE_ASPECT";
    case 21: 
      return "LOOT_BONUS_MOBS";
    case 32: 
      return "DIG_SPEED";
    case 33: 
      return "SILK_TOUCH";
    case 34: 
      return "DURABILITY";
    case 35: 
      return "LOOT_BONUS_BLOCKS";
    case 48: 
      return "ARROW_DAMAGE";
    case 49: 
      return "ARROW_KNOCKBACK";
    case 50: 
      return "ARROW_FIRE";
    case 51: 
      return "ARROW_INFINITE";
    case 61: 
      return "LUCK";
    case 62: 
      return "LURE";
    }
    return "UNKNOWN_ENCHANT_" + this.target.id;
  }
  
  public static net.minecraft.server.v1_8_R3.Enchantment getRaw(org.bukkit.enchantments.Enchantment enchantment)
  {
    if ((enchantment instanceof EnchantmentWrapper)) {
      enchantment = ((EnchantmentWrapper)enchantment).getEnchantment();
    }
    if ((enchantment instanceof CraftEnchantment)) {
      return ((CraftEnchantment)enchantment).target;
    }
    return null;
  }
  
  public boolean conflictsWith(org.bukkit.enchantments.Enchantment other)
  {
    if ((other instanceof EnchantmentWrapper)) {
      other = ((EnchantmentWrapper)other).getEnchantment();
    }
    if (!(other instanceof CraftEnchantment)) {
      return false;
    }
    CraftEnchantment ench = (CraftEnchantment)other;
    return !this.target.a(ench.target);
  }
}
