package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.spigotmc.SpigotWorldConfig;

public class BlockNetherWart
  extends BlockPlant
{
  public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 3);
  
  protected BlockNetherWart()
  {
    super(Material.PLANT, MaterialMapColor.D);
    j(this.blockStateList.getBlockData().set(AGE, Integer.valueOf(0)));
    a(true);
    float f = 0.5F;
    
    a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
    a(null);
  }
  
  protected boolean c(Block block)
  {
    return block == Blocks.SOUL_SAND;
  }
  
  public boolean f(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return c(world.getType(blockposition.down()).getBlock());
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    int i = ((Integer)iblockdata.get(AGE)).intValue();
    if ((i < 3) && (random.nextInt(Math.max(1, (int)world.growthOdds / world.spigotConfig.wartModifier * 10)) == 0))
    {
      iblockdata = iblockdata.set(AGE, Integer.valueOf(i + 1));
      
      CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, toLegacyData(iblockdata));
    }
    super.b(world, blockposition, iblockdata, random);
  }
  
  public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i)
  {
    if (!world.isClientSide)
    {
      int j = 1;
      if (((Integer)iblockdata.get(AGE)).intValue() >= 3)
      {
        j = 2 + world.random.nextInt(3);
        if (i > 0) {
          j += world.random.nextInt(i + 1);
        }
      }
      for (int k = 0; k < j; k++) {
        a(world, blockposition, new ItemStack(Items.NETHER_WART));
      }
    }
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return null;
  }
  
  public int a(Random random)
  {
    return 0;
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
