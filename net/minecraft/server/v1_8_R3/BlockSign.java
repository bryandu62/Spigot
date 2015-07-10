package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockSign
  extends BlockContainer
{
  protected BlockSign()
  {
    super(Material.WOOD);
    float ☃ = 0.25F;
    float ☃ = 1.0F;
    a(0.5F - ☃, 0.0F, 0.5F - ☃, 0.5F + ☃, ☃, 0.5F + ☃);
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    return null;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean b(IBlockAccess ☃, BlockPosition ☃)
  {
    return true;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean g()
  {
    return true;
  }
  
  public TileEntity a(World ☃, int ☃)
  {
    return new TileEntitySign();
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.SIGN;
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.isClientSide) {
      return true;
    }
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntitySign)) {
      return ((TileEntitySign)☃).b(☃);
    }
    return false;
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃)
  {
    return (!e(☃, ☃)) && (super.canPlace(☃, ☃));
  }
}
