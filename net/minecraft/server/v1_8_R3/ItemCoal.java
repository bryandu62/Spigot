package net.minecraft.server.v1_8_R3;

public class ItemCoal
  extends Item
{
  public ItemCoal()
  {
    a(true);
    setMaxDurability(0);
    a(CreativeModeTab.l);
  }
  
  public String e_(ItemStack ☃)
  {
    if (☃.getData() == 1) {
      return "item.charcoal";
    }
    return "item.coal";
  }
}
