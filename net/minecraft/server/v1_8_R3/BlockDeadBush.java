package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockDeadBush
  extends BlockPlant
{
  protected BlockDeadBush()
  {
    super(Material.REPLACEABLE_PLANT);
    float ☃ = 0.4F;
    a(0.5F - ☃, 0.0F, 0.5F - ☃, 0.5F + ☃, 0.8F, 0.5F + ☃);
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return MaterialMapColor.o;
  }
  
  protected boolean c(Block ☃)
  {
    return (☃ == Blocks.SAND) || (☃ == Blocks.HARDENED_CLAY) || (☃ == Blocks.STAINED_HARDENED_CLAY) || (☃ == Blocks.DIRT);
  }
  
  public boolean a(World ☃, BlockPosition ☃)
  {
    return true;
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return null;
  }
  
  public void a(World ☃, EntityHuman ☃, BlockPosition ☃, IBlockData ☃, TileEntity ☃)
  {
    if ((☃.isClientSide) || (☃.bZ() == null) || (☃.bZ().getItem() != Items.SHEARS))
    {
      super.a(☃, ☃, ☃, ☃, ☃);
    }
    else
    {
      ☃.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
      
      a(☃, ☃, new ItemStack(Blocks.DEADBUSH, 1, 0));
    }
  }
}
