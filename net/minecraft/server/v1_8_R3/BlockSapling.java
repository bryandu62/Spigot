package net.minecraft.server.v1_8_R3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.SpigotWorldConfig;

public class BlockSapling
  extends BlockPlant
  implements IBlockFragilePlantElement
{
  public static final BlockStateEnum<BlockWood.EnumLogVariant> TYPE = BlockStateEnum.of("type", BlockWood.EnumLogVariant.class);
  public static final BlockStateInteger STAGE = BlockStateInteger.of("stage", 0, 1);
  public static TreeType treeType;
  
  protected BlockSapling()
  {
    j(this.blockStateList.getBlockData().set(TYPE, BlockWood.EnumLogVariant.OAK).set(STAGE, Integer.valueOf(0)));
    float f = 0.4F;
    
    a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
    a(CreativeModeTab.c);
  }
  
  public String getName()
  {
    return LocaleI18n.get(a() + "." + BlockWood.EnumLogVariant.OAK.d() + ".name");
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if (!world.isClientSide)
    {
      super.b(world, blockposition, iblockdata, random);
      if ((world.getLightLevel(blockposition.up()) >= 9) && (random.nextInt(Math.max(2, (int)(world.growthOdds / world.spigotConfig.saplingModifier * 7.0F + 0.5F))) == 0))
      {
        world.captureTreeGeneration = true;
        
        grow(world, blockposition, iblockdata, random);
        
        world.captureTreeGeneration = false;
        if (world.capturedBlockStates.size() > 0)
        {
          TreeType treeType = treeType;
          treeType = null;
          Location location = new Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ());
          List<BlockState> blocks = (List)world.capturedBlockStates.clone();
          world.capturedBlockStates.clear();
          StructureGrowEvent event = null;
          if (treeType != null)
          {
            event = new StructureGrowEvent(location, treeType, false, null, blocks);
            Bukkit.getPluginManager().callEvent(event);
          }
          if ((event == null) || (!event.isCancelled())) {
            for (BlockState blockstate : blocks) {
              blockstate.update(true);
            }
          }
        }
      }
    }
  }
  
  public void grow(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if (((Integer)iblockdata.get(STAGE)).intValue() == 0) {
      world.setTypeAndData(blockposition, iblockdata.a(STAGE), 4);
    } else {
      e(world, blockposition, iblockdata, random);
    }
  }
  
  public void e(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    Object object;
    Object object;
    if (random.nextInt(10) == 0)
    {
      treeType = TreeType.BIG_TREE;
      object = new WorldGenBigTree(true);
    }
    else
    {
      treeType = TreeType.TREE;
      object = new WorldGenTrees(true);
    }
    int i = 0;
    int j = 0;
    boolean flag = false;
    switch (SyntheticClass_1.a[((BlockWood.EnumLogVariant)iblockdata.get(TYPE)).ordinal()])
    {
    case 1: 
      for (i = 0; i >= -1; i--) {
        for (j = 0; j >= -1; j--) {
          if (a(world, blockposition, i, j, BlockWood.EnumLogVariant.SPRUCE))
          {
            treeType = TreeType.MEGA_REDWOOD;
            object = new WorldGenMegaTree(false, random.nextBoolean());
            flag = true;
            break;
          }
        }
      }
      if (!flag)
      {
        j = 0;
        i = 0;
        treeType = TreeType.REDWOOD;
        object = new WorldGenTaiga2(true);
      }
      break;
    case 2: 
      treeType = TreeType.BIRCH;
      object = new WorldGenForest(true, false);
      break;
    case 3: 
      IBlockData iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
      IBlockData iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
      for (i = 0; i >= -1; i--) {
        for (j = 0; j >= -1; j--) {
          if (a(world, blockposition, i, j, BlockWood.EnumLogVariant.JUNGLE))
          {
            treeType = TreeType.JUNGLE;
            object = new WorldGenJungleTree(true, 10, 20, iblockdata1, iblockdata2);
            flag = true;
            break;
          }
        }
      }
      if (!flag)
      {
        j = 0;
        i = 0;
        treeType = TreeType.SMALL_JUNGLE;
        object = new WorldGenTrees(true, 4 + random.nextInt(7), iblockdata1, iblockdata2, false);
      }
      break;
    case 4: 
      treeType = TreeType.ACACIA;
      object = new WorldGenAcaciaTree(true);
      break;
    case 5: 
      for (i = 0; i >= -1; i--) {
        for (j = 0; j >= -1; j--) {
          if (a(world, blockposition, i, j, BlockWood.EnumLogVariant.DARK_OAK))
          {
            treeType = TreeType.DARK_OAK;
            object = new WorldGenForestTree(true);
            flag = true;
            break;
          }
        }
      }
      if (!flag) {
        return;
      }
      break;
    }
    IBlockData iblockdata1 = Blocks.AIR.getBlockData();
    if (flag)
    {
      world.setTypeAndData(blockposition.a(i, 0, j), iblockdata1, 4);
      world.setTypeAndData(blockposition.a(i + 1, 0, j), iblockdata1, 4);
      world.setTypeAndData(blockposition.a(i, 0, j + 1), iblockdata1, 4);
      world.setTypeAndData(blockposition.a(i + 1, 0, j + 1), iblockdata1, 4);
    }
    else
    {
      world.setTypeAndData(blockposition, iblockdata1, 4);
    }
    if (!((WorldGenerator)object).generate(world, random, blockposition.a(i, 0, j))) {
      if (flag)
      {
        world.setTypeAndData(blockposition.a(i, 0, j), iblockdata, 4);
        world.setTypeAndData(blockposition.a(i + 1, 0, j), iblockdata, 4);
        world.setTypeAndData(blockposition.a(i, 0, j + 1), iblockdata, 4);
        world.setTypeAndData(blockposition.a(i + 1, 0, j + 1), iblockdata, 4);
      }
      else
      {
        world.setTypeAndData(blockposition, iblockdata, 4);
      }
    }
  }
  
  private boolean a(World world, BlockPosition blockposition, int i, int j, BlockWood.EnumLogVariant blockwood_enumlogvariant)
  {
    return (a(world, blockposition.a(i, 0, j), blockwood_enumlogvariant)) && (a(world, blockposition.a(i + 1, 0, j), blockwood_enumlogvariant)) && (a(world, blockposition.a(i, 0, j + 1), blockwood_enumlogvariant)) && (a(world, blockposition.a(i + 1, 0, j + 1), blockwood_enumlogvariant));
  }
  
  public boolean a(World world, BlockPosition blockposition, BlockWood.EnumLogVariant blockwood_enumlogvariant)
  {
    IBlockData iblockdata = world.getType(blockposition);
    
    return (iblockdata.getBlock() == this) && (iblockdata.get(TYPE) == blockwood_enumlogvariant);
  }
  
  public int getDropData(IBlockData iblockdata)
  {
    return ((BlockWood.EnumLogVariant)iblockdata.get(TYPE)).a();
  }
  
  public boolean a(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag)
  {
    return true;
  }
  
  public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata)
  {
    return world.random.nextFloat() < 0.45D;
  }
  
  public void b(World world, Random random, BlockPosition blockposition, IBlockData iblockdata)
  {
    grow(world, blockposition, iblockdata, random);
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(TYPE, BlockWood.EnumLogVariant.a(i & 0x7)).set(STAGE, Integer.valueOf((i & 0x8) >> 3));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    byte b0 = 0;
    int i = b0 | ((BlockWood.EnumLogVariant)iblockdata.get(TYPE)).a();
    
    i |= ((Integer)iblockdata.get(STAGE)).intValue() << 3;
    return i;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { TYPE, STAGE });
  }
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[BlockWood.EnumLogVariant.values().length];
    
    static
    {
      try
      {
        a[BlockWood.EnumLogVariant.SPRUCE.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[BlockWood.EnumLogVariant.BIRCH.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        a[BlockWood.EnumLogVariant.JUNGLE.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        a[BlockWood.EnumLogVariant.ACACIA.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        a[BlockWood.EnumLogVariant.DARK_OAK.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        a[BlockWood.EnumLogVariant.OAK.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
    }
  }
}
