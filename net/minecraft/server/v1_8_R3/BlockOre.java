package net.minecraft.server.v1_8_R3;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Random;

public class BlockOre
  extends Block
{
  public BlockOre()
  {
    this(Material.STONE.r());
  }
  
  public BlockOre(MaterialMapColor materialmapcolor)
  {
    super(Material.STONE, materialmapcolor);
    a(CreativeModeTab.b);
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return this == Blocks.QUARTZ_ORE ? Items.QUARTZ : this == Blocks.EMERALD_ORE ? Items.EMERALD : this == Blocks.LAPIS_ORE ? Items.DYE : this == Blocks.DIAMOND_ORE ? Items.DIAMOND : this == Blocks.COAL_ORE ? Items.COAL : Item.getItemOf(this);
  }
  
  public int a(Random random)
  {
    return this == Blocks.LAPIS_ORE ? 4 + random.nextInt(5) : 1;
  }
  
  public int getDropCount(int i, Random random)
  {
    if ((i > 0) && (Item.getItemOf(this) != getDropType((IBlockData)P().a().iterator().next(), random, i)))
    {
      int j = random.nextInt(i + 2) - 1;
      if (j < 0) {
        j = 0;
      }
      return a(random) * (j + 1);
    }
    return a(random);
  }
  
  public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i)
  {
    super.dropNaturally(world, blockposition, iblockdata, f, i);
  }
  
  public int getExpDrop(World world, IBlockData iblockdata, int i)
  {
    if (getDropType(iblockdata, world.random, i) != Item.getItemOf(this))
    {
      int j = 0;
      if (this == Blocks.COAL_ORE) {
        j = MathHelper.nextInt(world.random, 0, 2);
      } else if (this == Blocks.DIAMOND_ORE) {
        j = MathHelper.nextInt(world.random, 3, 7);
      } else if (this == Blocks.EMERALD_ORE) {
        j = MathHelper.nextInt(world.random, 3, 7);
      } else if (this == Blocks.LAPIS_ORE) {
        j = MathHelper.nextInt(world.random, 2, 5);
      } else if (this == Blocks.QUARTZ_ORE) {
        j = MathHelper.nextInt(world.random, 2, 5);
      }
      return j;
    }
    return 0;
  }
  
  public int getDropData(World world, BlockPosition blockposition)
  {
    return 0;
  }
  
  public int getDropData(IBlockData iblockdata)
  {
    return this == Blocks.LAPIS_ORE ? EnumColor.BLUE.getInvColorIndex() : 0;
  }
}
