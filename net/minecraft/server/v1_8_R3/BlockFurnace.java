package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockFurnace
  extends BlockContainer
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  private final boolean b;
  private static boolean N;
  
  protected BlockFurnace(boolean ☃)
  {
    super(Material.STONE);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
    this.b = ☃;
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Item.getItemOf(Blocks.FURNACE);
  }
  
  public void onPlace(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    e(☃, ☃, ☃);
  }
  
  private void e(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (☃.isClientSide) {
      return;
    }
    Block ☃ = ☃.getType(☃.north()).getBlock();
    Block ☃ = ☃.getType(☃.south()).getBlock();
    Block ☃ = ☃.getType(☃.west()).getBlock();
    Block ☃ = ☃.getType(☃.east()).getBlock();
    
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    if ((☃ == EnumDirection.NORTH) && (☃.o()) && (!☃.o())) {
      ☃ = EnumDirection.SOUTH;
    } else if ((☃ == EnumDirection.SOUTH) && (☃.o()) && (!☃.o())) {
      ☃ = EnumDirection.NORTH;
    } else if ((☃ == EnumDirection.WEST) && (☃.o()) && (!☃.o())) {
      ☃ = EnumDirection.EAST;
    } else if ((☃ == EnumDirection.EAST) && (☃.o()) && (!☃.o())) {
      ☃ = EnumDirection.WEST;
    }
    ☃.setTypeAndData(☃, ☃.set(FACING, ☃), 2);
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.isClientSide) {
      return true;
    }
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityFurnace))
    {
      ☃.openContainer((TileEntityFurnace)☃);
      ☃.b(StatisticList.Y);
    }
    return true;
  }
  
  public static void a(boolean ☃, World ☃, BlockPosition ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    TileEntity ☃ = ☃.getTileEntity(☃);
    
    N = true;
    if (☃)
    {
      ☃.setTypeAndData(☃, Blocks.LIT_FURNACE.getBlockData().set(FACING, ☃.get(FACING)), 3);
      ☃.setTypeAndData(☃, Blocks.LIT_FURNACE.getBlockData().set(FACING, ☃.get(FACING)), 3);
    }
    else
    {
      ☃.setTypeAndData(☃, Blocks.FURNACE.getBlockData().set(FACING, ☃.get(FACING)), 3);
      ☃.setTypeAndData(☃, Blocks.FURNACE.getBlockData().set(FACING, ☃.get(FACING)), 3);
    }
    N = false;
    if (☃ != null)
    {
      ☃.D();
      ☃.setTileEntity(☃, ☃);
    }
  }
  
  public TileEntity a(World ☃, int ☃)
  {
    return new TileEntityFurnace();
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    return getBlockData().set(FACING, ☃.getDirection().opposite());
  }
  
  public void postPlace(World ☃, BlockPosition ☃, IBlockData ☃, EntityLiving ☃, ItemStack ☃)
  {
    ☃.setTypeAndData(☃, ☃.set(FACING, ☃.getDirection().opposite()), 2);
    if (☃.hasName())
    {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if ((☃ instanceof TileEntityFurnace)) {
        ((TileEntityFurnace)☃).a(☃.getName());
      }
    }
  }
  
  public void remove(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (!N)
    {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if ((☃ instanceof TileEntityFurnace))
      {
        InventoryUtils.dropInventory(☃, ☃, (TileEntityFurnace)☃);
        
        ☃.updateAdjacentComparators(☃, this);
      }
    }
    super.remove(☃, ☃, ☃);
  }
  
  public boolean isComplexRedstone()
  {
    return true;
  }
  
  public int l(World ☃, BlockPosition ☃)
  {
    return Container.a(☃.getTileEntity(☃));
  }
  
  public int b()
  {
    return 3;
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
