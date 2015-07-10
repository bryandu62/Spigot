package net.minecraft.server.v1_8_R3;

public class ItemSign
  extends Item
{
  public ItemSign()
  {
    this.maxStackSize = 16;
    a(CreativeModeTab.c);
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃ == EnumDirection.DOWN) {
      return false;
    }
    if (!☃.getType(☃).getBlock().getMaterial().isBuildable()) {
      return false;
    }
    ☃ = ☃.shift(☃);
    if (!☃.a(☃, ☃, ☃)) {
      return false;
    }
    if (!Blocks.STANDING_SIGN.canPlace(☃, ☃)) {
      return false;
    }
    if (☃.isClientSide) {
      return true;
    }
    if (☃ == EnumDirection.UP)
    {
      int ☃ = MathHelper.floor((☃.yaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 0xF;
      ☃.setTypeAndData(☃, Blocks.STANDING_SIGN.getBlockData().set(BlockFloorSign.ROTATION, Integer.valueOf(☃)), 3);
    }
    else
    {
      ☃.setTypeAndData(☃, Blocks.WALL_SIGN.getBlockData().set(BlockWallSign.FACING, ☃), 3);
    }
    ☃.count -= 1;
    TileEntity ☃ = ☃.getTileEntity(☃);
    if (((☃ instanceof TileEntitySign)) && 
      (!ItemBlock.a(☃, ☃, ☃, ☃))) {
      ☃.openSign((TileEntitySign)☃);
    }
    return true;
  }
}
