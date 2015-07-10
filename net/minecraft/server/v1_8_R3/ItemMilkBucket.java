package net.minecraft.server.v1_8_R3;

public class ItemMilkBucket
  extends Item
{
  public ItemMilkBucket()
  {
    c(1);
    a(CreativeModeTab.f);
  }
  
  public ItemStack b(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    if (!☃.abilities.canInstantlyBuild) {
      ☃.count -= 1;
    }
    if (!☃.isClientSide) {
      ☃.removeAllEffects();
    }
    ☃.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
    if (☃.count <= 0) {
      return new ItemStack(Items.BUCKET);
    }
    return ☃;
  }
  
  public int d(ItemStack ☃)
  {
    return 32;
  }
  
  public EnumAnimation e(ItemStack ☃)
  {
    return EnumAnimation.DRINK;
  }
  
  public ItemStack a(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    ☃.a(☃, d(☃));
    return ☃;
  }
}
