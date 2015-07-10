package net.minecraft.server.v1_8_R3;

public class ItemBookAndQuill
  extends Item
{
  public ItemBookAndQuill()
  {
    c(1);
  }
  
  public ItemStack a(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    ☃.openBook(☃);
    ☃.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
    return ☃;
  }
  
  public static boolean b(NBTTagCompound ☃)
  {
    if (☃ == null) {
      return false;
    }
    if (!☃.hasKeyOfType("pages", 9)) {
      return false;
    }
    NBTTagList ☃ = ☃.getList("pages", 8);
    for (int ☃ = 0; ☃ < ☃.size(); ☃++)
    {
      String ☃ = ☃.getString(☃);
      if (☃ == null) {
        return false;
      }
      if (☃.length() > 32767) {
        return false;
      }
    }
    return true;
  }
}
