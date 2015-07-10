package net.minecraft.server.v1_8_R3;

public class EnchantmentArrowDamage
  extends Enchantment
{
  public EnchantmentArrowDamage(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.BOW);
    c("arrowDamage");
  }
  
  public int a(int ☃)
  {
    return 1 + (☃ - 1) * 10;
  }
  
  public int b(int ☃)
  {
    return a(☃) + 15;
  }
  
  public int getMaxLevel()
  {
    return 5;
  }
}
