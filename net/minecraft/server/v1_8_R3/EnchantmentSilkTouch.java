package net.minecraft.server.v1_8_R3;

public class EnchantmentSilkTouch
  extends Enchantment
{
  protected EnchantmentSilkTouch(int ☃, MinecraftKey ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.DIGGER);
    
    c("untouching");
  }
  
  public int a(int ☃)
  {
    return 15;
  }
  
  public int b(int ☃)
  {
    return super.a(☃) + 50;
  }
  
  public int getMaxLevel()
  {
    return 1;
  }
  
  public boolean a(Enchantment ☃)
  {
    return (super.a(☃)) && (☃.id != LOOT_BONUS_BLOCKS.id);
  }
  
  public boolean canEnchant(ItemStack ☃)
  {
    if (☃.getItem() == Items.SHEARS) {
      return true;
    }
    return super.canEnchant(☃);
  }
}
