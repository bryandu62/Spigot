package net.minecraft.server.v1_8_R3;

public class BlockFloorSign
  extends BlockSign
{
  public static final BlockStateInteger ROTATION = BlockStateInteger.of("rotation", 0, 15);
  
  public BlockFloorSign()
  {
    j(this.blockStateList.getBlockData().set(ROTATION, Integer.valueOf(0)));
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    if (!☃.getType(☃.down()).getBlock().getMaterial().isBuildable())
    {
      b(☃, ☃, ☃, 0);
      ☃.setAir(☃);
    }
    super.doPhysics(☃, ☃, ☃, ☃);
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(ROTATION, Integer.valueOf(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((Integer)☃.get(ROTATION)).intValue();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { ROTATION });
  }
}
