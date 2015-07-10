package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.Random;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.SpigotWorldConfig;

public class BlockMushroom
  extends BlockPlant
  implements IBlockFragilePlantElement
{
  protected BlockMushroom()
  {
    float f = 0.2F;
    
    a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
    a(true);
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    int sourceX = blockposition.getX();int sourceY = blockposition.getY();int sourceZ = blockposition.getZ();
    if (random.nextInt(Math.max(1, (int)world.growthOdds / world.spigotConfig.mushroomModifier * 25)) == 0)
    {
      int i = 5;
      
      Iterator iterator = BlockPosition.b(blockposition.a(-4, -1, -4), blockposition.a(4, 1, 4)).iterator();
      while (iterator.hasNext())
      {
        BlockPosition blockposition1 = (BlockPosition)iterator.next();
        if (world.getType(blockposition1).getBlock() == this)
        {
          i--;
          if (i <= 0) {
            return;
          }
        }
      }
      BlockPosition blockposition2 = blockposition.a(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
      for (int j = 0; j < 4; j++)
      {
        if ((world.isEmpty(blockposition2)) && (f(world, blockposition2, getBlockData()))) {
          blockposition = blockposition2;
        }
        blockposition2 = blockposition.a(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
      }
      if ((world.isEmpty(blockposition2)) && (f(world, blockposition2, getBlockData())))
      {
        org.bukkit.World bworld = world.getWorld();
        BlockState blockState = bworld.getBlockAt(blockposition2.getX(), blockposition2.getY(), blockposition2.getZ()).getState();
        blockState.setType(CraftMagicNumbers.getMaterial(this));
        
        BlockSpreadEvent event = new BlockSpreadEvent(blockState.getBlock(), bworld.getBlockAt(sourceX, sourceY, sourceZ), blockState);
        world.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
          blockState.update(true);
        }
      }
    }
  }
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    return (super.canPlace(world, blockposition)) && (f(world, blockposition, getBlockData()));
  }
  
  protected boolean c(Block block)
  {
    return block.o();
  }
  
  public boolean f(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if ((blockposition.getY() >= 0) && (blockposition.getY() < 256))
    {
      IBlockData iblockdata1 = world.getType(blockposition.down());
      
      return iblockdata1.getBlock() == Blocks.MYCELIUM;
    }
    return false;
  }
  
  public boolean d(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    world.setAir(blockposition);
    WorldGenHugeMushroom worldgenhugemushroom = null;
    if (this == Blocks.BROWN_MUSHROOM)
    {
      BlockSapling.treeType = TreeType.BROWN_MUSHROOM;
      worldgenhugemushroom = new WorldGenHugeMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
    }
    else if (this == Blocks.RED_MUSHROOM)
    {
      BlockSapling.treeType = TreeType.RED_MUSHROOM;
      worldgenhugemushroom = new WorldGenHugeMushroom(Blocks.RED_MUSHROOM_BLOCK);
    }
    if ((worldgenhugemushroom != null) && (worldgenhugemushroom.generate(world, random, blockposition))) {
      return true;
    }
    world.setTypeAndData(blockposition, iblockdata, 3);
    return false;
  }
  
  public boolean a(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag)
  {
    return true;
  }
  
  public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata)
  {
    return random.nextFloat() < 0.4D;
  }
  
  public void b(World world, Random random, BlockPosition blockposition, IBlockData iblockdata)
  {
    d(world, blockposition, iblockdata, random);
  }
}
