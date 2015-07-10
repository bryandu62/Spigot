package org.spigotmc;

import gnu.trove.set.TByteSet;
import gnu.trove.set.hash.TByteHashSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

public class AntiXray
{
  private static final CustomTimingsHandler update = new CustomTimingsHandler("xray - update");
  private static final CustomTimingsHandler obfuscate = new CustomTimingsHandler("xray - obfuscate");
  private final boolean[] obfuscateBlocks = new boolean['翿'];
  private final byte[] replacementOres;
  
  public AntiXray(SpigotWorldConfig config)
  {
    for (Iterator localIterator1 = (config.engineMode == 1 ? config.hiddenBlocks : config.replaceBlocks).iterator(); localIterator1.hasNext();)
    {
      int id = ((Integer)localIterator1.next()).intValue();
      
      this.obfuscateBlocks[id] = true;
    }
    TByteSet blocks = new TByteHashSet();
    for (Integer i : config.hiddenBlocks)
    {
      Block block = Block.getById(i.intValue());
      if ((block != null) && (!block.isTileEntity())) {
        blocks.add((byte)i.intValue());
      }
    }
    this.replacementOres = blocks.toArray();
  }
  
  public void updateNearbyBlocks(World world, BlockPosition position)
  {
    if (world.spigotConfig.antiXray)
    {
      update.startTiming();
      updateNearbyBlocks(world, position, 2, false);
      update.stopTiming();
    }
  }
  
  public void obfuscateSync(int chunkX, int chunkY, int bitmask, byte[] buffer, World world)
  {
    if (world.spigotConfig.antiXray)
    {
      obfuscate.startTiming();
      obfuscate(chunkX, chunkY, bitmask, buffer, world);
      obfuscate.stopTiming();
    }
  }
  
  public void obfuscate(int chunkX, int chunkY, int bitmask, byte[] buffer, World world)
  {
    if (world.spigotConfig.antiXray)
    {
      int initialRadius = 1;
      
      int index = 0;
      
      int randomOre = 0;
      
      int startX = chunkX << 4;
      int startZ = chunkY << 4;
      byte replaceWithTypeId;
      byte replaceWithTypeId;
      byte replaceWithTypeId;
      switch (world.getWorld().getEnvironment())
      {
      case NORMAL: 
        replaceWithTypeId = (byte)CraftMagicNumbers.getId(Blocks.NETHERRACK);
        break;
      case THE_END: 
        replaceWithTypeId = (byte)CraftMagicNumbers.getId(Blocks.END_STONE);
        break;
      default: 
        replaceWithTypeId = (byte)CraftMagicNumbers.getId(Blocks.STONE);
      }
      for (int i = 0; i < 16; i++) {
        if ((bitmask & 1 << i) != 0) {
          for (int y = 0; y < 16; y++) {
            for (int z = 0; z < 16; z++) {
              for (int x = 0; x < 16; x++) {
                if (index >= buffer.length)
                {
                  index++;
                }
                else
                {
                  int blockId = buffer[(index << 1)] & 0xFF | 
                    (buffer[((index << 1) + 1)] & 0xFF) << 8;
                  blockId >>>= 4;
                  if (this.obfuscateBlocks[blockId] != 0)
                  {
                    if (!isLoaded(world, new BlockPosition(startX + x, (i << 4) + y, startZ + z), initialRadius))
                    {
                      index++;
                      continue;
                    }
                    if (!hasTransparentBlockAdjacent(world, new BlockPosition(startX + x, (i << 4) + y, startZ + z), initialRadius))
                    {
                      int newId = blockId;
                      switch (world.spigotConfig.engineMode)
                      {
                      case 1: 
                        newId = replaceWithTypeId & 0xFF;
                        break;
                      case 2: 
                        if (randomOre >= this.replacementOres.length) {
                          randomOre = 0;
                        }
                        newId = this.replacementOres[(randomOre++)] & 0xFF;
                      }
                      newId = newId << 4;
                      buffer[(index << 1)] = ((byte)(newId & 0xFF));
                      buffer[((index << 1) + 1)] = ((byte)(newId >> 8 & 0xFF));
                    }
                  }
                  index++;
                }
              }
            }
          }
        }
      }
    }
  }
  
  private void updateNearbyBlocks(World world, BlockPosition position, int radius, boolean updateSelf)
  {
    if (world.isLoaded(position))
    {
      Block block = world.getType(position).getBlock();
      if ((updateSelf) && (this.obfuscateBlocks[Block.getId(block)] != 0)) {
        world.notify(position);
      }
      if (radius > 0)
      {
        updateNearbyBlocks(world, position.east(), radius - 1, true);
        updateNearbyBlocks(world, position.west(), radius - 1, true);
        updateNearbyBlocks(world, position.up(), radius - 1, true);
        updateNearbyBlocks(world, position.down(), radius - 1, true);
        updateNearbyBlocks(world, position.south(), radius - 1, true);
        updateNearbyBlocks(world, position.north(), radius - 1, true);
      }
    }
  }
  
  private static boolean isLoaded(World world, BlockPosition position, int radius)
  {
    return (world.isLoaded(position)) && (
      (radius == 0) || (
      (isLoaded(world, position.east(), radius - 1)) && 
      (isLoaded(world, position.west(), radius - 1)) && 
      (isLoaded(world, position.up(), radius - 1)) && 
      (isLoaded(world, position.down(), radius - 1)) && 
      (isLoaded(world, position.south(), radius - 1)) && 
      (isLoaded(world, position.north(), radius - 1))));
  }
  
  private static boolean hasTransparentBlockAdjacent(World world, BlockPosition position, int radius)
  {
    return (!isSolidBlock(world.getType(position, false).getBlock())) || (
      (radius > 0) && (
      (hasTransparentBlockAdjacent(world, position.east(), radius - 1)) || 
      (hasTransparentBlockAdjacent(world, position.west(), radius - 1)) || 
      (hasTransparentBlockAdjacent(world, position.up(), radius - 1)) || 
      (hasTransparentBlockAdjacent(world, position.down(), radius - 1)) || 
      (hasTransparentBlockAdjacent(world, position.south(), radius - 1)) || 
      (hasTransparentBlockAdjacent(world, position.north(), radius - 1))));
  }
  
  private static boolean isSolidBlock(Block block)
  {
    return (block.isOccluding()) && (block != Blocks.MOB_SPAWNER);
  }
}
