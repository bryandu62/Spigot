package net.minecraft.server.v1_8_R3;

public class EnchantmentDepthStrider
  extends Enchantment
{
  public EnchantmentDepthStrider(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.ARMOR_FEET);
    c("waterWalker");
  }
  
  public int a(int ☃)
  {
    return ☃ * 10;
  }
  
  public int b(int ☃)
  {
    return a(☃) + 15;
  }
  
  public int getMaxLevel()
  {
    return 3;
  }
}
