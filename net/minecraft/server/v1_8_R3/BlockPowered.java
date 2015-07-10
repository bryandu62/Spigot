package net.minecraft.server.v1_8_R3;

public class BlockPowered
  extends Block
{
  public BlockPowered(Material ☃, MaterialMapColor ☃)
  {
    super(☃, ☃);
  }
  
  public boolean isPowerSource()
  {
    return true;
  }
  
  public int a(IBlockAccess ☃, BlockPosition ☃, IBlockData ☃, EnumDirection ☃)
  {
    return 15;
  }
}
