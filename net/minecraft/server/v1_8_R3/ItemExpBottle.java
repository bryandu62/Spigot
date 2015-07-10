package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class ItemExpBottle
  extends Item
{
  public ItemExpBottle()
  {
    a(CreativeModeTab.f);
  }
  
  public ItemStack a(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    if (!☃.abilities.canInstantlyBuild) {
      ☃.count -= 1;
    }
    ☃.makeSound(☃, "random.bow", 0.5F, 0.4F / (g.nextFloat() * 0.4F + 0.8F));
    if (!☃.isClientSide) {
      ☃.addEntity(new EntityThrownExpBottle(☃, ☃));
    }
    ☃.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
    return ☃;
  }
}
