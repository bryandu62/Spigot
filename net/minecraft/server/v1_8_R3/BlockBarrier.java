package net.minecraft.server.v1_8_R3;

public class BlockBarrier
  extends Block
{
  protected BlockBarrier()
  {
    super(Material.BANNER);
    x();
    b(6000001.0F);
    K();
    this.t = true;
  }
  
  public int b()
  {
    return -1;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public void dropNaturally(World ☃, BlockPosition ☃, IBlockData ☃, float ☃, int ☃) {}
}
