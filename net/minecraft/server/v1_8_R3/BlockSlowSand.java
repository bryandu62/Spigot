package net.minecraft.server.v1_8_R3;

public class BlockSlowSand
  extends Block
{
  public BlockSlowSand()
  {
    super(Material.SAND, MaterialMapColor.B);
    a(CreativeModeTab.b);
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    float ☃ = 0.125F;
    return new AxisAlignedBB(☃.getX(), ☃.getY(), ☃.getZ(), ☃.getX() + 1, ☃.getY() + 1 - ☃, ☃.getZ() + 1);
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, Entity ☃)
  {
    ☃.motX *= 0.4D;
    ☃.motZ *= 0.4D;
  }
}
