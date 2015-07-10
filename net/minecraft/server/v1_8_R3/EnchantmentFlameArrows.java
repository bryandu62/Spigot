package net.minecraft.server.v1_8_R3;

public class EnchantmentFlameArrows
  extends Enchantment
{
  public EnchantmentFlameArrows(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.BOW);
    c("arrowFire");
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
