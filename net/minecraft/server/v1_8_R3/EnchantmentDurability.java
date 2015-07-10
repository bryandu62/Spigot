package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class EnchantmentDurability
  extends Enchantment
{
  protected EnchantmentDurability(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.BREAKABLE);
    
    c("durability");
  }
  
  public int a(int ☃)
  {
    return 5 + (☃ - 1) * 8;
  }
  
  public int b(int ☃)
  {
    return super.a(☃) + 50;
  }
  
  public int getMaxLevel()
  {
    return 3;
  }
  
  public boolean canEnchant(ItemStack ☃)
  {
    if (☃.e()) {
      return true;
    }
    return super.canEnchant(☃);
  }
  
  public static boolean a(ItemStack ☃, int ☃, Random ☃)
  {
    if (((☃.getItem() instanceof ItemArmor)) && (☃.nextFloat() < 0.6F)) {
      return false;
    }
    return ☃.nextInt(☃ + 1) > 0;
  }
}
