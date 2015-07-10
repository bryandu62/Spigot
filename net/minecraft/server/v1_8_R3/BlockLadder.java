package net.minecraft.server.v1_8_R3;

public class BlockLadder
  extends Block
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  
  protected BlockLadder()
  {
    super(Material.ORIENTABLE);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
    a(CreativeModeTab.c);
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    updateShape(☃, ☃);
    return super.a(☃, ☃, ☃);
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    if (☃.getBlock() != this) {
      return;
    }
    float ☃ = 0.125F;
    switch (1.a[((EnumDirection)☃.get(FACING)).ordinal()])
    {
    case 1: 
      a(0.0F, 0.0F, 1.0F - ☃, 1.0F, 1.0F, 1.0F);
      break;
    case 2: 
      a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, ☃);
      break;
    case 3: 
      a(1.0F - ☃, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      break;
    case 4: 
    default: 
      a(0.0F, 0.0F, 0.0F, ☃, 1.0F, 1.0F);
    }
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃)
  {
    if (☃.getType(☃.west()).getBlock().isOccluding()) {
      return true;
    }
    if (☃.getType(☃.east()).getBlock().isOccluding()) {
      return true;
    }
    if (☃.getType(☃.north()).getBlock().isOccluding()) {
      return true;
    }
    if (☃.getType(☃.south()).getBlock().isOccluding()) {
      return true;
    }
    return false;
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    if ((☃.k().c()) && (a(☃, ☃, ☃))) {
      return getBlockData().set(FACING, ☃);
    }
    for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
      if (a(☃, ☃, ☃)) {
        return getBlockData().set(FACING, ☃);
      }
    }
    return getBlockData();
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    if (!a(☃, ☃, ☃))
    {
      b(☃, ☃, ☃, 0);
      ☃.setAir(☃);
    }
    super.doPhysics(☃, ☃, ☃, ☃);
  }
  
  protected boolean a(World ☃, BlockPosition ☃, EnumDirection ☃)
  {
    return ☃.getType(☃.shift(☃.opposite())).getBlock().isOccluding();
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
