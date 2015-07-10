package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.spigotmc.SpigotWorldConfig;

public class BlockCrops
  extends BlockPlant
  implements IBlockFragilePlantElement
{
  public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 7);
  
  protected BlockCrops()
  {
    j(this.blockStateList.getBlockData().set(AGE, Integer.valueOf(0)));
    a(true);
    float f = 0.5F;
    
    a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
    a(null);
    c(0.0F);
    a(h);
    K();
  }
  
  protected boolean c(Block block)
  {
    return block == Blocks.FARMLAND;
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    super.b(world, blockposition, iblockdata, random);
    if (world.getLightLevel(blockposition.up()) >= 9)
    {
      int i = ((Integer)iblockdata.get(AGE)).intValue();
      if (i < 7)
      {
        float f = a(this, world, blockposition);
        if (random.nextInt((int)(world.growthOdds / world.spigotConfig.wheatModifier * (25.0F / f)) + 1) == 0)
        {
          IBlockData data = iblockdata.set(AGE, Integer.valueOf(i + 1));
          CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, toLegacyData(data));
        }
      }
    }
  }
  
  public void g(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    int i = ((Integer)iblockdata.get(AGE)).intValue() + MathHelper.nextInt(world.random, 2, 5);
    if (i > 7) {
      i = 7;
    }
    IBlockData data = iblockdata.set(AGE, Integer.valueOf(i));
    CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, toLegacyData(data));
  }
  
  protected static float a(Block block, World world, BlockPosition blockposition)
  {
    float f = 1.0F;
    BlockPosition blockposition1 = blockposition.down();
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++)
      {
        float f1 = 0.0F;
        IBlockData iblockdata = world.getType(blockposition1.a(i, 0, j));
        if (iblockdata.getBlock() == Blocks.FARMLAND)
        {
          f1 = 1.0F;
          if (((Integer)iblockdata.get(BlockSoil.MOISTURE)).intValue() > 0) {
            f1 = 3.0F;
          }
        }
        if ((i != 0) || (j != 0)) {
          f1 /= 4.0F;
        }
        f += f1;
      }
    }
    BlockPosition blockposition2 = blockposition.north();
    BlockPosition blockposition3 = blockposition.south();
    BlockPosition blockposition4 = blockposition.west();
    BlockPosition blockposition5 = blockposition.east();
    boolean flag = (block == world.getType(blockposition4).getBlock()) || (block == world.getType(blockposition5).getBlock());
    boolean flag1 = (block == world.getType(blockposition2).getBlock()) || (block == world.getType(blockposition3).getBlock());
    if ((flag) && (flag1))
    {
      f /= 2.0F;
    }
    else
    {
      boolean flag2 = (block == world.getType(blockposition4.north()).getBlock()) || (block == world.getType(blockposition5.north()).getBlock()) || (block == world.getType(blockposition5.south()).getBlock()) || (block == world.getType(blockposition4.south()).getBlock());
      if (flag2) {
        f /= 2.0F;
      }
    }
    return f;
  }
  
  public boolean f(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return ((world.k(blockposition) >= 8) || (world.i(blockposition))) && (c(world.getType(blockposition.down()).getBlock()));
  }
  
  protected Item l()
  {
    return Items.WHEAT_SEEDS;
  }
  
  protected Item n()
  {
    return Items.WHEAT;
  }
  
  public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i)
  {
    super.dropNaturally(world, blockposition, iblockdata, f, 0);
    if (!world.isClientSide)
    {
      int j = ((Integer)iblockdata.get(AGE)).intValue();
      if (j >= 7)
      {
        int k = 3 + i;
        for (int l = 0; l < k; l++) {
          if (world.random.nextInt(15) <= j) {
            a(world, blockposition, new ItemStack(l(), 1, 0));
          }
        }
      }
    }
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return ((Integer)iblockdata.get(AGE)).intValue() == 7 ? n() : l();
  }
  
  public boolean a(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag)
  {
    return ((Integer)iblockdata.get(AGE)).intValue() < 7;
  }
  
  public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata)
  {
    return true;
  }
  
  public void b(World world, Random random, BlockPosition blockposition, IBlockData iblockdata)
  {
    g(world, blockposition, iblockdata);
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(AGE, Integer.valueOf(i));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    return ((Integer)iblockdata.get(AGE)).intValue();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { AGE });
  }
}
