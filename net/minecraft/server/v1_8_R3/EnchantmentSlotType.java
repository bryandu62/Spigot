package net.minecraft.server.v1_8_R3;

public enum EnchantmentSlotType
{
  private EnchantmentSlotType() {}
  
  public boolean canEnchant(Item ☃)
  {
    if (this == ALL) {
      return true;
    }
    if ((this == BREAKABLE) && (☃.usesDurability())) {
      return true;
    }
    if ((☃ instanceof ItemArmor))
    {
      if (this == ARMOR) {
        return true;
      }
      ItemArmor ☃ = (ItemArmor)☃;
      if (☃.b == 0) {
        return this == ARMOR_HEAD;
      }
      if (☃.b == 2) {
        return this == ARMOR_LEGS;
      }
      if (☃.b == 1) {
        return this == ARMOR_TORSO;
      }
      if (☃.b == 3) {
        return this == ARMOR_FEET;
      }
      return false;
    }
    if ((☃ instanceof ItemSword)) {
      return this == WEAPON;
    }
    if ((☃ instanceof ItemTool)) {
      return this == DIGGER;
    }
    if ((☃ instanceof ItemBow)) {
      return this == BOW;
    }
    if ((☃ instanceof ItemFishingRod)) {
      return this == FISHING_ROD;
    }
    return false;
  }
}
