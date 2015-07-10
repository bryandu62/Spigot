package net.minecraft.server.v1_8_R3;

public class ItemCarrotStick
  extends Item
{
  public ItemCarrotStick()
  {
    a(CreativeModeTab.e);
    c(1);
    setMaxDurability(25);
  }
  
  public ItemStack a(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    if ((☃.au()) && ((☃.vehicle instanceof EntityPig)))
    {
      EntityPig ☃ = (EntityPig)☃.vehicle;
      if ((☃.cm().h()) && (☃.j() - ☃.getData() >= 7))
      {
        ☃.cm().g();
        ☃.damage(7, ☃);
        if (☃.count == 0)
        {
          ItemStack ☃ = new ItemStack(Items.FISHING_ROD);
          ☃.setTag(☃.getTag());
          return ☃;
        }
      }
    }
    ☃.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
    
    return ☃;
  }
}
