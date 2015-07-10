package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BlockThin
  extends Block
{
  public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
  public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
  public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
  public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");
  private final boolean a;
  
  protected BlockThin(Material ☃, boolean ☃)
  {
    super(☃);
    j(this.blockStateList.getBlockData().set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)));
    this.a = ☃;
    a(CreativeModeTab.c);
  }
  
  public IBlockData updateState(IBlockData ☃, IBlockAccess ☃, BlockPosition ☃)
  {
    return ☃.set(NORTH, Boolean.valueOf(c(☃.getType(☃.north()).getBlock()))).set(SOUTH, Boolean.valueOf(c(☃.getType(☃.south()).getBlock()))).set(WEST, Boolean.valueOf(c(☃.getType(☃.west()).getBlock()))).set(EAST, Boolean.valueOf(c(☃.getType(☃.east()).getBlock())));
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    if (!this.a) {
      return null;
    }
    return super.getDropType(☃, ☃, ☃);
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, AxisAlignedBB ☃, List<AxisAlignedBB> ☃, Entity ☃)
  {
    boolean ☃ = c(☃.getType(☃.north()).getBlock());
    boolean ☃ = c(☃.getType(☃.south()).getBlock());
    boolean ☃ = c(☃.getType(☃.west()).getBlock());
    boolean ☃ = c(☃.getType(☃.east()).getBlock());
    if (((☃) && (☃)) || ((!☃) && (!☃) && (!☃) && (!☃)))
    {
      a(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
      super.a(☃, ☃, ☃, ☃, ☃, ☃);
    }
    else if (☃)
    {
      a(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
      super.a(☃, ☃, ☃, ☃, ☃, ☃);
    }
    else if (☃)
    {
      a(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
      super.a(☃, ☃, ☃, ☃, ☃, ☃);
    }
    if (((☃) && (☃)) || ((!☃) && (!☃) && (!☃) && (!☃)))
    {
      a(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
      super.a(☃, ☃, ☃, ☃, ☃, ☃);
    }
    else if (☃)
    {
      a(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
      super.a(☃, ☃, ☃, ☃, ☃, ☃);
    }
    else if (☃)
    {
      a(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
      super.a(☃, ☃, ☃, ☃, ☃, ☃);
    }
  }
  
  public void j()
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    float ☃ = 0.4375F;
    float ☃ = 0.5625F;
    float ☃ = 0.4375F;
    float ☃ = 0.5625F;
    
    boolean ☃ = c(☃.getType(☃.north()).getBlock());
    boolean ☃ = c(☃.getType(☃.south()).getBlock());
    boolean ☃ = c(☃.getType(☃.west()).getBlock());
    boolean ☃ = c(☃.getType(☃.east()).getBlock());
    if (((☃) && (☃)) || ((!☃) && (!☃) && (!☃) && (!☃)))
    {
      ☃ = 0.0F;
      ☃ = 1.0F;
    }
    else if (☃)
    {
      ☃ = 0.0F;
    }
    else if (☃)
    {
      ☃ = 1.0F;
    }
    if (((☃) && (☃)) || ((!☃) && (!☃) && (!☃) && (!☃)))
    {
      ☃ = 0.0F;
      ☃ = 1.0F;
    }
    else if (☃)
    {
      ☃ = 0.0F;
    }
    else if (☃)
    {
      ☃ = 1.0F;
    }
    a(☃, 0.0F, ☃, ☃, 1.0F, ☃);
  }
  
  public final boolean c(Block ☃)
  {
    return (☃.o()) || (☃ == this) || (☃ == Blocks.GLASS) || (☃ == Blocks.STAINED_GLASS) || (☃ == Blocks.STAINED_GLASS_PANE) || ((☃ instanceof BlockThin));
  }
  
  protected boolean I()
  {
    return true;
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return 0;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { NORTH, EAST, WEST, SOUTH });
  }
}
