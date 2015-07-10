package net.minecraft.server.v1_8_R3;

public class ItemBed
  extends Item
{
  public ItemBed()
  {
    a(CreativeModeTab.c);
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.isClientSide) {
      return true;
    }
    if (☃ != EnumDirection.UP) {
      return false;
    }
    IBlockData ☃ = ☃.getType(☃);
    Block ☃ = ☃.getBlock();
    boolean ☃ = ☃.a(☃, ☃);
    if (!☃) {
      ☃ = ☃.up();
    }
    int ☃ = MathHelper.floor(☃.yaw * 4.0F / 360.0F + 0.5D) & 0x3;
    EnumDirection ☃ = EnumDirection.fromType2(☃);
    BlockPosition ☃ = ☃.shift(☃);
    if ((!☃.a(☃, ☃, ☃)) || (!☃.a(☃, ☃, ☃))) {
      return false;
    }
    boolean ☃ = ☃.getType(☃).getBlock().a(☃, ☃);
    boolean ☃ = (☃) || (☃.isEmpty(☃));
    boolean ☃ = (☃) || (☃.isEmpty(☃));
    if ((☃) && (☃) && (World.a(☃, ☃.down())) && (World.a(☃, ☃.down())))
    {
      IBlockData ☃ = Blocks.BED.getBlockData().set(BlockBed.OCCUPIED, Boolean.valueOf(false)).set(BlockBed.FACING, ☃).set(BlockBed.PART, BlockBed.EnumBedPart.FOOT);
      if (☃.setTypeAndData(☃, ☃, 3))
      {
        IBlockData ☃ = ☃.set(BlockBed.PART, BlockBed.EnumBedPart.HEAD);
        ☃.setTypeAndData(☃, ☃, 3);
      }
      ☃.count -= 1;
      return true;
    }
    return false;
  }
}
