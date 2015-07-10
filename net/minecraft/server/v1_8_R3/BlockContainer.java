package net.minecraft.server.v1_8_R3;

public abstract class BlockContainer
  extends Block
  implements IContainer
{
  protected BlockContainer(Material ☃)
  {
    this(☃, ☃.r());
  }
  
  protected BlockContainer(Material ☃, MaterialMapColor ☃)
  {
    super(☃, ☃);
    this.isTileEntity = true;
  }
  
  protected boolean a(World ☃, BlockPosition ☃, EnumDirection ☃)
  {
    return ☃.getType(☃.shift(☃)).getBlock().getMaterial() == Material.CACTUS;
  }
  
  protected boolean e(World ☃, BlockPosition ☃)
  {
    return (a(☃, ☃, EnumDirection.NORTH)) || (a(☃, ☃, EnumDirection.SOUTH)) || (a(☃, ☃, EnumDirection.WEST)) || (a(☃, ☃, EnumDirection.EAST));
  }
  
  public int b()
  {
    return -1;
  }
  
  public void remove(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    super.remove(☃, ☃, ☃);
    ☃.t(☃);
  }
  
  public boolean a(World ☃, BlockPosition ☃, IBlockData ☃, int ☃, int ☃)
  {
    super.a(☃, ☃, ☃, ☃, ☃);
    
    TileEntity ☃ = ☃.getTileEntity(☃);
    if (☃ == null) {
      return false;
    }
    return ☃.c(☃, ☃);
  }
}
