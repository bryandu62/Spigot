package net.minecraft.server.v1_8_R3;

public class SlotFurnaceFuel
  extends Slot
{
  public SlotFurnaceFuel(IInventory ☃, int ☃, int ☃, int ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  public boolean isAllowed(ItemStack ☃)
  {
    return (TileEntityFurnace.isFuel(☃)) || (c_(☃));
  }
  
  public int getMaxStackSize(ItemStack ☃)
  {
    return c_(☃) ? 1 : super.getMaxStackSize(☃);
  }
  
  public static boolean c_(ItemStack ☃)
  {
    return (☃ != null) && (☃.getItem() != null) && (☃.getItem() == Items.BUCKET);
  }
}
