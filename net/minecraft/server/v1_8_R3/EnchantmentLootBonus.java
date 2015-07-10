package net.minecraft.server.v1_8_R3;

public class EnchantmentLootBonus
  extends Enchantment
{
  protected EnchantmentLootBonus(int ☃, MinecraftKey ☃, int ☃, EnchantmentSlotType ☃)
  {
    super(☃, ☃, ☃, ☃);
    if (☃ == EnchantmentSlotType.DIGGER) {
      c("lootBonusDigger");
    } else if (☃ == EnchantmentSlotType.FISHING_ROD) {
      c("lootBonusFishing");
    } else {
      c("lootBonus");
    }
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
  
  public boolean a(Enchantment ☃)
  {
    return (super.a(☃)) && (☃.id != SILK_TOUCH.id);
  }
}
