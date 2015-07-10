package net.minecraft.server.v1_8_R3;

import java.util.List;

public class BlockWaterLily
  extends BlockPlant
{
  protected BlockWaterLily()
  {
    float ☃ = 0.5F;
    float ☃ = 0.015625F;
    a(0.5F - ☃, 0.0F, 0.5F - ☃, 0.5F + ☃, ☃, 0.5F + ☃);
    a(CreativeModeTab.c);
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, AxisAlignedBB ☃, List<AxisAlignedBB> ☃, Entity ☃)
  {
    if ((☃ == null) || (!(☃ instanceof EntityBoat))) {
      super.a(☃, ☃, ☃, ☃, ☃, ☃);
    }
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    return new AxisAlignedBB(☃.getX() + this.minX, ☃.getY() + this.minY, ☃.getZ() + this.minZ, ☃.getX() + this.maxX, ☃.getY() + this.maxY, ☃.getZ() + this.maxZ);
  }
  
  protected boolean c(Block ☃)
  {
    return ☃ == Blocks.WATER;
  }
  
  public boolean f(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if ((☃.getY() < 0) || (☃.getY() >= 256)) {
      return false;
    }
    IBlockData ☃ = ☃.getType(☃.down());
    return (☃.getBlock().getMaterial() == Material.WATER) && (((Integer)☃.get(BlockFluids.LEVEL)).intValue() == 0);
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return 0;
  }
}
