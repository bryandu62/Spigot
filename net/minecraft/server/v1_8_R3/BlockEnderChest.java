package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockEnderChest
  extends BlockContainer
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  
  protected BlockEnderChest()
  {
    super(Material.STONE);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
    a(CreativeModeTab.c);
    a(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public int b()
  {
    return 2;
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Item.getItemOf(Blocks.OBSIDIAN);
  }
  
  public int a(Random ☃)
  {
    return 8;
  }
  
  protected boolean I()
  {
    return true;
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    return getBlockData().set(FACING, ☃.getDirection().opposite());
  }
  
  public void postPlace(World ☃, BlockPosition ☃, IBlockData ☃, EntityLiving ☃, ItemStack ☃)
  {
    ☃.setTypeAndData(☃, ☃.set(FACING, ☃.getDirection().opposite()), 2);
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    InventoryEnderChest ☃ = ☃.getEnderChest();
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ == null) || (!(☃ instanceof TileEntityEnderChest))) {
      return true;
    }
    if (☃.getType(☃.up()).getBlock().isOccluding()) {
      return true;
    }
    if (☃.isClientSide) {
      return true;
    }
    ☃.a((TileEntityEnderChest)☃);
    ☃.openContainer(☃);
    ☃.b(StatisticList.V);
    
    return true;
  }
  
  public TileEntity a(World ☃, int ☃)
  {
    return new TileEntityEnderChest();
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
