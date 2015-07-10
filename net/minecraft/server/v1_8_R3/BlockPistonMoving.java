package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockPistonMoving
  extends BlockContainer
{
  public static final BlockStateDirection FACING = BlockPistonExtension.FACING;
  public static final BlockStateEnum<BlockPistonExtension.EnumPistonType> TYPE = BlockPistonExtension.TYPE;
  
  public BlockPistonMoving()
  {
    super(Material.PISTON);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT));
    c(-1.0F);
  }
  
  public TileEntity a(World ☃, int ☃)
  {
    return null;
  }
  
  public static TileEntity a(IBlockData ☃, EnumDirection ☃, boolean ☃, boolean ☃)
  {
    return new TileEntityPiston(☃, ☃, ☃, ☃);
  }
  
  public void remove(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityPiston)) {
      ((TileEntityPiston)☃).h();
    } else {
      super.remove(☃, ☃, ☃);
    }
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃)
  {
    return false;
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃, EnumDirection ☃)
  {
    return false;
  }
  
  public void postBreak(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    BlockPosition ☃ = ☃.shift(((EnumDirection)☃.get(FACING)).opposite());
    IBlockData ☃ = ☃.getType(☃);
    if (((☃.getBlock() instanceof BlockPiston)) && (((Boolean)☃.get(BlockPiston.EXTENDED)).booleanValue())) {
      ☃.setAir(☃);
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
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if ((!☃.isClientSide) && (☃.getTileEntity(☃) == null))
    {
      ☃.setAir(☃);
      return true;
    }
    return false;
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return null;
  }
  
  public void dropNaturally(World ☃, BlockPosition ☃, IBlockData ☃, float ☃, int ☃)
  {
    if (☃.isClientSide) {
      return;
    }
    TileEntityPiston ☃ = e(☃, ☃);
    if (☃ == null) {
      return;
    }
    IBlockData ☃ = ☃.b();
    ☃.getBlock().b(☃, ☃, ☃, 0);
  }
  
  public MovingObjectPosition a(World ☃, BlockPosition ☃, Vec3D ☃, Vec3D ☃)
  {
    return null;
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    if (!☃.isClientSide) {
      ☃.getTileEntity(☃);
    }
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    TileEntityPiston ☃ = e(☃, ☃);
    if (☃ == null) {
      return null;
    }
    float ☃ = ☃.a(0.0F);
    if (☃.d()) {
      ☃ = 1.0F - ☃;
    }
    return a(☃, ☃, ☃.b(), ☃, ☃.e());
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    TileEntityPiston ☃ = e(☃, ☃);
    if (☃ != null)
    {
      IBlockData ☃ = ☃.b();
      Block ☃ = ☃.getBlock();
      if ((☃ == this) || (☃.getMaterial() == Material.AIR)) {
        return;
      }
      float ☃ = ☃.a(0.0F);
      if (☃.d()) {
        ☃ = 1.0F - ☃;
      }
      ☃.updateShape(☃, ☃);
      if ((☃ == Blocks.PISTON) || (☃ == Blocks.STICKY_PISTON)) {
        ☃ = 0.0F;
      }
      EnumDirection ☃ = ☃.e();
      this.minX = (☃.B() - ☃.getAdjacentX() * ☃);
      this.minY = (☃.D() - ☃.getAdjacentY() * ☃);
      this.minZ = (☃.F() - ☃.getAdjacentZ() * ☃);
      this.maxX = (☃.C() - ☃.getAdjacentX() * ☃);
      this.maxY = (☃.E() - ☃.getAdjacentY() * ☃);
      this.maxZ = (☃.G() - ☃.getAdjacentZ() * ☃);
    }
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃, float ☃, EnumDirection ☃)
  {
    if ((☃.getBlock() == this) || (☃.getBlock().getMaterial() == Material.AIR)) {
      return null;
    }
    AxisAlignedBB ☃ = ☃.getBlock().a(☃, ☃, ☃);
    if (☃ == null) {
      return null;
    }
    double ☃ = ☃.a;
    double ☃ = ☃.b;
    double ☃ = ☃.c;
    double ☃ = ☃.d;
    double ☃ = ☃.e;
    double ☃ = ☃.f;
    if (☃.getAdjacentX() < 0) {
      ☃ -= ☃.getAdjacentX() * ☃;
    } else {
      ☃ -= ☃.getAdjacentX() * ☃;
    }
    if (☃.getAdjacentY() < 0) {
      ☃ -= ☃.getAdjacentY() * ☃;
    } else {
      ☃ -= ☃.getAdjacentY() * ☃;
    }
    if (☃.getAdjacentZ() < 0) {
      ☃ -= ☃.getAdjacentZ() * ☃;
    } else {
      ☃ -= ☃.getAdjacentZ() * ☃;
    }
    return new AxisAlignedBB(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  private TileEntityPiston e(IBlockAccess ☃, BlockPosition ☃)
  {
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityPiston)) {
      return (TileEntityPiston)☃;
    }
    return null;
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(FACING, BlockPistonExtension.b(☃)).set(TYPE, (☃ & 0x8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    ☃ |= ((EnumDirection)☃.get(FACING)).a();
    if (☃.get(TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
      ☃ |= 0x8;
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, TYPE });
  }
}
