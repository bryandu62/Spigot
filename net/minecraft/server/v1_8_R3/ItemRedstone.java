package net.minecraft.server.v1_8_R3;

public class ItemRedstone
  extends Item
{
  public ItemRedstone()
  {
    a(CreativeModeTab.d);
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    boolean ☃ = ☃.getType(☃).getBlock().a(☃, ☃);
    BlockPosition ☃ = ☃ ? ☃ : ☃.shift(☃);
    if (!☃.a(☃, ☃, ☃)) {
      return false;
    }
    Block ☃ = ☃.getType(☃).getBlock();
    if (!☃.a(☃, ☃, false, ☃, null, ☃)) {
      return false;
    }
    if (Blocks.REDSTONE_WIRE.canPlace(☃, ☃))
    {
      ☃.count -= 1;
      ☃.setTypeUpdate(☃, Blocks.REDSTONE_WIRE.getBlockData());
      return true;
    }
    return false;
  }
}
