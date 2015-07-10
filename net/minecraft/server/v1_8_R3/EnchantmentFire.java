package net.minecraft.server.v1_8_R3;

public class EnchantmentFire
  extends Enchantment
{
  protected EnchantmentFire(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.WEAPON);
    
    c("fire");
  }
  
  public int a(int ☃)
  {
    return 10 + 20 * (☃ - 1);
  }
  
  public int b(int ☃)
  {
    return super.a(☃) + 50;
  }
  
  public int getMaxLevel()
  {
    return 2;
  }
}
