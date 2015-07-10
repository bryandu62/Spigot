package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;

public class BlockTorch
  extends Block
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", new Predicate()
  {
    public boolean a(EnumDirection ☃)
    {
      return ☃ != EnumDirection.DOWN;
    }
  });
  
  protected BlockTorch()
  {
    super(Material.ORIENTABLE);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.UP));
    a(true);
    a(CreativeModeTab.c);
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    return null;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  private boolean e(World ☃, BlockPosition ☃)
  {
    if (World.a(☃, ☃)) {
      return true;
    }
    Block ☃ = ☃.getType(☃).getBlock();
    return ((☃ instanceof BlockFence)) || (☃ == Blocks.GLASS) || (☃ == Blocks.COBBLESTONE_WALL) || (☃ == Blocks.STAINED_GLASS);
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃)
  {
    for (EnumDirection ☃ : FACING.c()) {
      if (a(☃, ☃, ☃)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean a(World ☃, BlockPosition ☃, EnumDirection ☃)
  {
    BlockPosition ☃ = ☃.shift(☃.opposite());
    
    boolean ☃ = ☃.k().c();
    return ((☃) && (☃.d(☃, true))) || ((☃.equals(EnumDirection.UP)) && (e(☃, ☃)));
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    if (a(☃, ☃, ☃)) {
      return getBlockData().set(FACING, ☃);
    }
    for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
      if (☃.d(☃.shift(☃.opposite()), true)) {
        return getBlockData().set(FACING, ☃);
      }
    }
    return getBlockData();
  }
  
  public void onPlace(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    f(☃, ☃, ☃);
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    e(☃, ☃, ☃);
  }
  
  protected boolean e(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (!f(☃, ☃, ☃)) {
      return true;
    }
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    EnumDirection.EnumAxis ☃ = ☃.k();
    EnumDirection ☃ = ☃.opposite();
    
    boolean ☃ = false;
    if ((☃.c()) && (!☃.d(☃.shift(☃), true))) {
      ☃ = true;
    } else if ((☃.b()) && (!e(☃, ☃.shift(☃)))) {
      ☃ = true;
    }
    if (☃)
    {
      b(☃, ☃, ☃, 0);
      ☃.setAir(☃);
      return true;
    }
    return false;
  }
  
  protected boolean f(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if ((☃.getBlock() == this) && 
      (a(☃, ☃, (EnumDirection)☃.get(FACING)))) {
      return true;
    }
    if (☃.getType(☃).getBlock() == this)
    {
      b(☃, ☃, ☃, 0);
      ☃.setAir(☃);
    }
    return false;
  }
  
  public MovingObjectPosition a(World ☃, BlockPosition ☃, Vec3D ☃, Vec3D ☃)
  {
    EnumDirection ☃ = (EnumDirection)☃.getType(☃).get(FACING);
    
    float ☃ = 0.15F;
    if (☃ == EnumDirection.EAST)
    {
      a(0.0F, 0.2F, 0.5F - ☃, ☃ * 2.0F, 0.8F, 0.5F + ☃);
    }
    else if (☃ == EnumDirection.WEST)
    {
      a(1.0F - ☃ * 2.0F, 0.2F, 0.5F - ☃, 1.0F, 0.8F, 0.5F + ☃);
    }
    else if (☃ == EnumDirection.SOUTH)
    {
      a(0.5F - ☃, 0.2F, 0.0F, 0.5F + ☃, 0.8F, ☃ * 2.0F);
    }
    else if (☃ == EnumDirection.NORTH)
    {
      a(0.5F - ☃, 0.2F, 1.0F - ☃ * 2.0F, 0.5F + ☃, 0.8F, 1.0F);
    }
    else
    {
      ☃ = 0.1F;
      a(0.5F - ☃, 0.0F, 0.5F - ☃, 0.5F + ☃, 0.6F, 0.5F + ☃);
    }
    return super.a(☃, ☃, ☃, ☃);
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    IBlockData ☃ = getBlockData();
    switch (☃)
    {
    case 1: 
      ☃ = ☃.set(FACING, EnumDirection.EAST);
      break;
    case 2: 
      ☃ = ☃.set(FACING, EnumDirection.WEST);
      break;
    case 3: 
      ☃ = ☃.set(FACING, EnumDirection.SOUTH);
      break;
    case 4: 
      ☃ = ☃.set(FACING, EnumDirection.NORTH);
      break;
    case 5: 
    default: 
      ☃ = ☃.set(FACING, EnumDirection.UP);
    }
    return ☃;
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    switch (2.a[((EnumDirection)☃.get(FACING)).ordinal()])
    {
    case 1: 
      ☃ |= 0x1;
      break;
    case 2: 
      ☃ |= 0x2;
      break;
    case 3: 
      ☃ |= 0x3;
      break;
    case 4: 
      ☃ |= 0x4;
      break;
    case 5: 
    case 6: 
    default: 
      ☃ |= 0x5;
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING });
  }
}
