package net.minecraft.server.v1_8_R3;

public class BlockWallSign
  extends BlockSign
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  
  public BlockWallSign()
  {
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    EnumDirection ☃ = (EnumDirection)☃.getType(☃).get(FACING);
    
    float ☃ = 0.28125F;
    float ☃ = 0.78125F;
    float ☃ = 0.0F;
    float ☃ = 1.0F;
    
    float ☃ = 0.125F;
    
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    switch (1.a[☃.ordinal()])
    {
    case 1: 
      a(☃, ☃, 1.0F - ☃, ☃, ☃, 1.0F);
      break;
    case 2: 
      a(☃, ☃, 0.0F, ☃, ☃, ☃);
      break;
    case 3: 
      a(1.0F - ☃, ☃, ☃, 1.0F, ☃, ☃);
      break;
    case 4: 
      a(0.0F, ☃, ☃, ☃, ☃, ☃);
    }
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    if (!☃.getType(☃.shift(☃.opposite())).getBlock().getMaterial().isBuildable())
    {
      b(☃, ☃, ☃, 0);
      ☃.setAir(☃);
    }
    super.doPhysics(☃, ☃, ☃, ☃);
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    EnumDirection ☃ = EnumDirection.fromType1(☃);
    if (☃.k() == EnumDirection.EnumAxis.Y) {
      ☃ = EnumDirection.NORTH;
    }
    return getBlockData().set(FACING, ☃);
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumDirection)☃.get(FACING)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING });
  }
}
