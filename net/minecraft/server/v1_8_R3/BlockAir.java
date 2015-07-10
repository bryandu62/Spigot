package net.minecraft.server.v1_8_R3;

public class BlockAir
  extends Block
{
  protected BlockAir()
  {
    super(Material.AIR);
  }
  
  public int b()
  {
    return -1;
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    return null;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean a(IBlockData ☃, boolean ☃)
  {
    return false;
  }
  
  public void dropNaturally(World ☃, BlockPosition ☃, IBlockData ☃, float ☃, int ☃) {}
  
  public boolean a(World ☃, BlockPosition ☃)
  {
    return true;
  }
}
