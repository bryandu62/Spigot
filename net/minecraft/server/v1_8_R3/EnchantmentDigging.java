package net.minecraft.server.v1_8_R3;

public class EnchantmentDigging
  extends Enchantment
{
  protected EnchantmentDigging(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.DIGGER);
    
    c("digging");
  }
  
  public int a(int ☃)
  {
    return 1 + 10 * (☃ - 1);
  }
  
  public int b(int ☃)
  {
    return super.a(☃) + 50;
  }
  
  public int getMaxLevel()
  {
    return 5;
  }
  
  public boolean canEnchant(ItemStack ☃)
  {
    if (☃.getItem() == Items.SHEARS) {
      return true;
    }
    return super.canEnchant(☃);
  }
}
