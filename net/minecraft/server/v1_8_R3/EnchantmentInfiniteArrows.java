package net.minecraft.server.v1_8_R3;

public class EnchantmentInfiniteArrows
  extends Enchantment
{
  public EnchantmentInfiniteArrows(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.BOW);
    c("arrowInfinite");
  }
  
  public int a(int ☃)
  {
    return 20;
  }
  
  public int b(int ☃)
  {
    return 50;
  }
  
  public int getMaxLevel()
  {
    return 1;
  }
}
