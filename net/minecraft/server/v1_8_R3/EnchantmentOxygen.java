package net.minecraft.server.v1_8_R3;

public class EnchantmentOxygen
  extends Enchantment
{
  public EnchantmentOxygen(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.ARMOR_HEAD);
    c("oxygen");
  }
  
  public int a(int ☃)
  {
    return 10 * ☃;
  }
  
  public int b(int ☃)
  {
    return a(☃) + 30;
  }
  
  public int getMaxLevel()
  {
    return 3;
  }
}
