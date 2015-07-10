package net.minecraft.server.v1_8_R3;

public class ItemGlassBottle
  extends Item
{
  public ItemGlassBottle()
  {
    a(CreativeModeTab.k);
  }
  
  public ItemStack a(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    MovingObjectPosition ☃ = a(☃, ☃, true);
    if (☃ == null) {
      return ☃;
    }
    if (☃.type == MovingObjectPosition.EnumMovingObjectType.BLOCK)
    {
      BlockPosition ☃ = ☃.a();
      if (!☃.a(☃, ☃)) {
        return ☃;
      }
      if (!☃.a(☃.shift(☃.direction), ☃.direction, ☃)) {
        return ☃;
      }
      if (☃.getType(☃).getBlock().getMaterial() == Material.WATER)
      {
        ☃.count -= 1;
        ☃.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
        if (☃.count <= 0) {
          return new ItemStack(Items.POTION);
        }
        if (!☃.inventory.pickup(new ItemStack(Items.POTION))) {
          ☃.drop(new ItemStack(Items.POTION, 1, 0), false);
        }
      }
    }
    return ☃;
  }
}
