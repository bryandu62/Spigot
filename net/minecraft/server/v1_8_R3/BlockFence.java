package net.minecraft.server.v1_8_R3;

import java.util.List;

public class BlockFence
  extends Block
{
  public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
  public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
  public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
  public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");
  
  public BlockFence(Material ☃)
  {
    this(☃, ☃.r());
  }
  
  public BlockFence(Material ☃, MaterialMapColor ☃)
  {
    super(☃, ☃);
    j(this.blockStateList.getBlockData().set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)));
    a(CreativeModeTab.c);
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, AxisAlignedBB ☃, List<AxisAlignedBB> ☃, Entity ☃)
  {
    boolean ☃ = e(☃, ☃.north());
    boolean ☃ = e(☃, ☃.south());
    boolean ☃ = e(☃, ☃.west());
    boolean ☃ = e(☃, ☃.east());
    
    float ☃ = 0.375F;
    float ☃ = 0.625F;
    float ☃ = 0.375F;
    float ☃ = 0.625F;
    if (☃) {
      ☃ = 0.0F;
    }
    if (☃) {
      ☃ = 1.0F;
    }
    if ((☃) || (☃))
    {
      a(☃, 0.0F, ☃, ☃, 1.5F, ☃);
      super.a(☃, ☃, ☃, ☃, ☃, ☃);
    }
    ☃ = 0.375F;
    ☃ = 0.625F;
    if (☃) {
      ☃ = 0.0F;
    }
    if (☃) {
      ☃ = 1.0F;
    }
    if ((☃) || (☃) || ((!☃) && (!☃)))
    {
      a(☃, 0.0F, ☃, ☃, 1.5F, ☃);
      super.a(☃, ☃, ☃, ☃, ☃, ☃);
    }
    if (☃) {
      ☃ = 0.0F;
    }
    if (☃) {
      ☃ = 1.0F;
    }
    a(☃, 0.0F, ☃, ☃, 1.0F, ☃);
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    boolean ☃ = e(☃, ☃.north());
    boolean ☃ = e(☃, ☃.south());
    boolean ☃ = e(☃, ☃.west());
    boolean ☃ = e(☃, ☃.east());
    
    float ☃ = 0.375F;
    float ☃ = 0.625F;
    float ☃ = 0.375F;
    float ☃ = 0.625F;
    if (☃) {
      ☃ = 0.0F;
    }
    if (☃) {
      ☃ = 1.0F;
    }
    if (☃) {
      ☃ = 0.0F;
    }
    if (☃) {
      ☃ = 1.0F;
    }
    a(☃, 0.0F, ☃, ☃, 1.0F, ☃);
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
    return false;
  }
  
  public boolean e(IBlockAccess ☃, BlockPosition ☃)
  {
    Block ☃ = ☃.getType(☃).getBlock();
    if (☃ == Blocks.BARRIER) {
      return false;
    }
    if ((((☃ instanceof BlockFence)) && (☃.material == this.material)) || ((☃ instanceof BlockFenceGate))) {
      return true;
    }
    if ((☃.material.k()) && (☃.d())) {
      return ☃.material != Material.PUMPKIN;
    }
    return false;
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.isClientSide) {
      return true;
    }
    return ItemLeash.a(☃, ☃, ☃);
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return 0;
  }
  
  public IBlockData updateState(IBlockData ☃, IBlockAccess ☃, BlockPosition ☃)
  {
    return ☃.set(NORTH, Boolean.valueOf(e(☃, ☃.north()))).set(EAST, Boolean.valueOf(e(☃, ☃.east()))).set(SOUTH, Boolean.valueOf(e(☃, ☃.south()))).set(WEST, Boolean.valueOf(e(☃, ☃.west())));
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { NORTH, EAST, WEST, SOUTH });
  }
}
