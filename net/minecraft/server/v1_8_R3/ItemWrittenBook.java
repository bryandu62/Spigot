package net.minecraft.server.v1_8_R3;

public class ItemWrittenBook
  extends Item
{
  public ItemWrittenBook()
  {
    c(1);
  }
  
  public static boolean b(NBTTagCompound ☃)
  {
    if (!ItemBookAndQuill.b(☃)) {
      return false;
    }
    if (!☃.hasKeyOfType("title", 8)) {
      return false;
    }
    String ☃ = ☃.getString("title");
    if ((☃ == null) || (☃.length() > 32)) {
      return false;
    }
    if (!☃.hasKeyOfType("author", 8)) {
      return false;
    }
    return true;
  }
  
  public static int h(ItemStack ☃)
  {
    return ☃.getTag().getInt("generation");
  }
  
  public String a(ItemStack ☃)
  {
    if (☃.hasTag())
    {
      NBTTagCompound ☃ = ☃.getTag();
      
      String ☃ = ☃.getString("title");
      if (!UtilColor.b(☃)) {
        return ☃;
      }
    }
    return super.a(☃);
  }
  
  public ItemStack a(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    if (!☃.isClientSide) {
      a(☃, ☃);
    }
    ☃.openBook(☃);
    ☃.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
    return ☃;
  }
  
  private void a(ItemStack ☃, EntityHuman ☃)
  {
    if ((☃ == null) || (☃.getTag() == null)) {
      return;
    }
    NBTTagCompound ☃ = ☃.getTag();
    if (☃.getBoolean("resolved")) {
      return;
    }
    ☃.setBoolean("resolved", true);
    if (!b(☃)) {
      return;
    }
    NBTTagList ☃ = ☃.getList("pages", 8);
    for (int ☃ = 0; ☃ < ☃.size(); ☃++)
    {
      String ☃ = ☃.getString(☃);
      IChatBaseComponent ☃;
      try
      {
        ☃ = IChatBaseComponent.ChatSerializer.a(☃);
        ☃ = ChatComponentUtils.filterForDisplay(☃, ☃, ☃);
      }
      catch (Exception ☃)
      {
        ☃ = new ChatComponentText(☃);
      }
      ☃.a(☃, new NBTTagString(IChatBaseComponent.ChatSerializer.a(☃)));
    }
    ☃.set("pages", ☃);
    if (((☃ instanceof EntityPlayer)) && (☃.bZ() == ☃))
    {
      Slot ☃ = ☃.activeContainer.getSlot(☃.inventory, ☃.inventory.itemInHandIndex);
      ((EntityPlayer)☃).playerConnection.sendPacket(new PacketPlayOutSetSlot(0, ☃.rawSlotIndex, ☃));
    }
  }
}
