package net.minecraft.server.v1_8_R3;

public class EnchantmentKnockback
  extends Enchantment
{
  protected EnchantmentKnockback(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.WEAPON);
    
    c("knockback");
  }
  
  public int a(int ☃)
  {
    return 5 + 20 * (☃ - 1);
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
