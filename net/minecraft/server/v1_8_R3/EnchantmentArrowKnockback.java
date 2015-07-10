package net.minecraft.server.v1_8_R3;

public class EnchantmentArrowKnockback
  extends Enchantment
{
  public EnchantmentArrowKnockback(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.BOW);
    c("arrowKnockback");
  }
  
  public int a(int ☃)
  {
    return 12 + (☃ - 1) * 20;
  }
  
  public int b(int ☃)
  {
    return a(☃) + 25;
  }
  
  public int getMaxLevel()
  {
    return 2;
  }
}
