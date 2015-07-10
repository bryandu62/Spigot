package net.minecraft.server.v1_8_R3;

public class EnchantmentLure
  extends Enchantment
{
  protected EnchantmentLure(int ☃, MinecraftKey ☃, int ☃, EnchantmentSlotType ☃)
  {
    super(☃, ☃, ☃, ☃);
    
    c("fishingSpeed");
  }
  
  public int a(int ☃)
  {
    return 15 + (☃ - 1) * 9;
  }
  
  public int b(int ☃)
  {
    return super.a(☃) + 50;
  }
  
  public int getMaxLevel()
  {
    return 3;
  }
}
