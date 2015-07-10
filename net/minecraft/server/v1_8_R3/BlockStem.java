package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.spigotmc.SpigotWorldConfig;

public class BlockStem
  extends BlockPlant
  implements IBlockFragilePlantElement
{
  public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 7);
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", new Predicate()
  {
    public boolean a(EnumDirection enumdirection)
    {
      return enumdirection != EnumDirection.DOWN;
    }
    
    public boolean apply(Object object)
    {
      return a((EnumDirection)object);
    }
  });
  private final Block blockFruit;
  
  protected BlockStem(Block block)
  {
    j(this.blockStateList.getBlockData().set(AGE, Integer.valueOf(0)).set(FACING, EnumDirection.UP));
    this.blockFruit = block;
    a(true);
    float f = 0.125F;
    
    a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
    a(null);
  }
  
  public IBlockData updateState(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    iblockdata = iblockdata.set(FACING, EnumDirection.UP);
    Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
    while (iterator.hasNext())
    {
      EnumDirection enumdirection = (EnumDirection)iterator.next();
      if (iblockaccess.getType(blockposition.shift(enumdirection)).getBlock() == this.blockFruit)
      {
        iblockdata = iblockdata.set(FACING, enumdirection);
        break;
      }
    }
    return iblockdata;
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
      float f = BlockCrops.a(this, world, blockposition);
      if (random.nextInt((int)(world.growthOdds / (this == Blocks.PUMPKIN_STEM ? world.spigotConfig.pumpkinModifier : world.spigotConfig.melonModifier) * (25.0F / f)) + 1) == 0)
      {
        int i = ((Integer)iblockdata.get(AGE)).intValue();
        if (i < 7)
        {
          iblockdata = iblockdata.set(AGE, Integer.valueOf(i + 1));
          
          CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, toLegacyData(iblockdata));
        }
        else
        {
          Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
          while (iterator.hasNext())
          {
            EnumDirection enumdirection = (EnumDirection)iterator.next();
            if (world.getType(blockposition.shift(enumdirection)).getBlock() == this.blockFruit) {
              return;
            }
          }
          blockposition = blockposition.shift(EnumDirection.EnumDirectionLimit.HORIZONTAL.a(random));
          Block block = world.getType(blockposition.down()).getBlock();
          if ((world.getType(blockposition).getBlock().material == Material.AIR) && ((block == Blocks.FARMLAND) || (block == Blocks.DIRT) || (block == Blocks.GRASS))) {
            CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this.blockFruit, 0);
          }
        }
      }
    }
  }
  
  public void g(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    int i = ((Integer)iblockdata.get(AGE)).intValue() + MathHelper.nextInt(world.random, 2, 5);
    
    CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, Math.min(7, i));
  }
  
  public void j()
  {
    float f = 0.125F;
    
    a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    this.maxY = ((((Integer)iblockaccess.getType(blockposition).get(AGE)).intValue() * 2 + 2) / 16.0F);
    float f = 0.125F;
    
    a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, (float)this.maxY, 0.5F + f);
  }
  
  public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i)
  {
    super.dropNaturally(world, blockposition, iblockdata, f, i);
    if (!world.isClientSide)
    {
      Item item = l();
      if (item != null)
      {
        int j = ((Integer)iblockdata.get(AGE)).intValue();
        for (int k = 0; k < 3; k++) {
          if (world.random.nextInt(15) <= j) {
            a(world, blockposition, new ItemStack(item));
          }
        }
      }
    }
  }
  
  protected Item l()
  {
    return this.blockFruit == Blocks.MELON_BLOCK ? Items.MELON_SEEDS : this.blockFruit == Blocks.PUMPKIN ? Items.PUMPKIN_SEEDS : null;
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return null;
  }
  
  public boolean a(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag)
  {
    return ((Integer)iblockdata.get(AGE)).intValue() != 7;
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
    return new BlockStateList(this, new IBlockState[] { AGE, FACING });
  }
}
