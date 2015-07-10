package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.spigotmc.SpigotWorldConfig;

public class BlockReed
  extends Block
{
  public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 15);
  
  protected BlockReed()
  {
    super(Material.PLANT);
    j(this.blockStateList.getBlockData().set(AGE, Integer.valueOf(0)));
    float f = 0.375F;
    
    a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
    a(true);
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if (((world.getType(blockposition.down()).getBlock() == Blocks.REEDS) || (e(world, blockposition, iblockdata))) && 
      (world.isEmpty(blockposition.up())))
    {
      for (int i = 1; world.getType(blockposition.down(i)).getBlock() == this; i++) {}
      if (i < 3)
      {
        int j = ((Integer)iblockdata.get(AGE)).intValue();
        if (j >= (byte)(int)range(3.0F, world.growthOdds / world.spigotConfig.caneModifier * 15.0F + 0.5F, 15.0F))
        {
          BlockPosition upPos = blockposition.up();
          CraftEventFactory.handleBlockGrowEvent(world, upPos.getX(), upPos.getY(), upPos.getZ(), this, 0);
          world.setTypeAndData(blockposition, iblockdata.set(AGE, Integer.valueOf(0)), 4);
        }
        else
        {
          world.setTypeAndData(blockposition, iblockdata.set(AGE, Integer.valueOf(j + 1)), 4);
        }
      }
    }
  }
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    Block block = world.getType(blockposition.down()).getBlock();
    if (block == this) {
      return true;
    }
    if ((block != Blocks.GRASS) && (block != Blocks.DIRT) && (block != Blocks.SAND)) {
      return false;
    }
    Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
    EnumDirection enumdirection;
    do
    {
      if (!iterator.hasNext()) {
        return false;
      }
      enumdirection = (EnumDirection)iterator.next();
    } while (world.getType(blockposition.shift(enumdirection).down()).getBlock().getMaterial() != Material.WATER);
    return true;
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    e(world, blockposition, iblockdata);
  }
  
  protected final boolean e(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (e(world, blockposition)) {
      return true;
    }
    b(world, blockposition, iblockdata, 0);
    world.setAir(blockposition);
    return false;
  }
  
  public boolean e(World world, BlockPosition blockposition)
  {
    return canPlace(world, blockposition);
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return null;
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return Items.REEDS;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
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
