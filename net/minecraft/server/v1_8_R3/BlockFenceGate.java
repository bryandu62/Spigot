package net.minecraft.server.v1_8_R3;

public class BlockFenceGate
  extends BlockDirectional
{
  public static final BlockStateBoolean OPEN = BlockStateBoolean.of("open");
  public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
  public static final BlockStateBoolean IN_WALL = BlockStateBoolean.of("in_wall");
  
  public BlockFenceGate(BlockWood.EnumLogVariant ☃)
  {
    super(Material.WOOD, ☃.c());
    
    j(this.blockStateList.getBlockData().set(OPEN, Boolean.valueOf(false)).set(POWERED, Boolean.valueOf(false)).set(IN_WALL, Boolean.valueOf(false)));
    a(CreativeModeTab.d);
  }
  
  public IBlockData updateState(IBlockData ☃, IBlockAccess ☃, BlockPosition ☃)
  {
    EnumDirection.EnumAxis ☃ = ((EnumDirection)☃.get(FACING)).k();
    if (((☃ == EnumDirection.EnumAxis.Z) && ((☃.getType(☃.west()).getBlock() == Blocks.COBBLESTONE_WALL) || (☃.getType(☃.east()).getBlock() == Blocks.COBBLESTONE_WALL))) || ((☃ == EnumDirection.EnumAxis.X) && ((☃.getType(☃.north()).getBlock() == Blocks.COBBLESTONE_WALL) || (☃.getType(☃.south()).getBlock() == Blocks.COBBLESTONE_WALL)))) {
      ☃ = ☃.set(IN_WALL, Boolean.valueOf(true));
    }
    return ☃;
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃)
  {
    if (☃.getType(☃.down()).getBlock().getMaterial().isBuildable()) {
      return super.canPlace(☃, ☃);
    }
    return false;
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (((Boolean)☃.get(OPEN)).booleanValue()) {
      return null;
    }
    EnumDirection.EnumAxis ☃ = ((EnumDirection)☃.get(FACING)).k();
    if (☃ == EnumDirection.EnumAxis.Z) {
      return new AxisAlignedBB(☃.getX(), ☃.getY(), ☃.getZ() + 0.375F, ☃.getX() + 1, ☃.getY() + 1.5F, ☃.getZ() + 0.625F);
    }
    return new AxisAlignedBB(☃.getX() + 0.375F, ☃.getY(), ☃.getZ(), ☃.getX() + 0.625F, ☃.getY() + 1.5F, ☃.getZ() + 1);
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    EnumDirection.EnumAxis ☃ = ((EnumDirection)☃.getType(☃).get(FACING)).k();
    if (☃ == EnumDirection.EnumAxis.Z) {
      a(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
    } else {
      a(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
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
  
  public boolean b(IBlockAccess ☃, BlockPosition ☃)
  {
    return ((Boolean)☃.getType(☃).get(OPEN)).booleanValue();
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    return getBlockData().set(FACING, ☃.getDirection()).set(OPEN, Boolean.valueOf(false)).set(POWERED, Boolean.valueOf(false)).set(IN_WALL, Boolean.valueOf(false));
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (((Boolean)☃.get(OPEN)).booleanValue())
    {
      ☃ = ☃.set(OPEN, Boolean.valueOf(false));
      ☃.setTypeAndData(☃, ☃, 2);
    }
    else
    {
      EnumDirection ☃ = EnumDirection.fromAngle(☃.yaw);
      if (☃.get(FACING) == ☃.opposite()) {
        ☃ = ☃.set(FACING, ☃);
      }
      ☃ = ☃.set(OPEN, Boolean.valueOf(true));
      ☃.setTypeAndData(☃, ☃, 2);
    }
    ☃.a(☃, ((Boolean)☃.get(OPEN)).booleanValue() ? 1003 : 1006, ☃, 0);
    return true;
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    if (☃.isClientSide) {
      return;
    }
    boolean ☃ = ☃.isBlockIndirectlyPowered(☃);
    if ((☃) || (☃.isPowerSource())) {
      if ((☃) && (!((Boolean)☃.get(OPEN)).booleanValue()) && (!((Boolean)☃.get(POWERED)).booleanValue()))
      {
        ☃.setTypeAndData(☃, ☃.set(OPEN, Boolean.valueOf(true)).set(POWERED, Boolean.valueOf(true)), 2);
        ☃.a(null, 1003, ☃, 0);
      }
      else if ((!☃) && (((Boolean)☃.get(OPEN)).booleanValue()) && (((Boolean)☃.get(POWERED)).booleanValue()))
      {
        ☃.setTypeAndData(☃, ☃.set(OPEN, Boolean.valueOf(false)).set(POWERED, Boolean.valueOf(false)), 2);
        ☃.a(null, 1006, ☃, 0);
      }
      else if (☃ != ((Boolean)☃.get(POWERED)).booleanValue())
      {
        ☃.setTypeAndData(☃, ☃.set(POWERED, Boolean.valueOf(☃)), 2);
      }
    }
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(FACING, EnumDirection.fromType2(☃)).set(OPEN, Boolean.valueOf((☃ & 0x4) != 0)).set(POWERED, Boolean.valueOf((☃ & 0x8) != 0));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    ☃ |= ((EnumDirection)☃.get(FACING)).b();
    if (((Boolean)☃.get(POWERED)).booleanValue()) {
      ☃ |= 0x8;
    }
    if (((Boolean)☃.get(OPEN)).booleanValue()) {
      ☃ |= 0x4;
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, OPEN, POWERED, IN_WALL });
  }
}
