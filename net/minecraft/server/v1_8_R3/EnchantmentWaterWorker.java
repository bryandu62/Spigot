package net.minecraft.server.v1_8_R3;

public class EnchantmentWaterWorker
  extends Enchantment
{
  public EnchantmentWaterWorker(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.ARMOR_HEAD);
    c("waterWorker");
  }
  
  public int a(int ☃)
  {
    return 1;
  }
  
  public int b(int ☃)
  {
    return a(☃) + 40;
  }
  
  public int getMaxLevel()
  {
    return 1;
  }
}
