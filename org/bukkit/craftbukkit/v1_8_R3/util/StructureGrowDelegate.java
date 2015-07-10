package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.material.MaterialData;

public class StructureGrowDelegate
  implements BlockChangeDelegate
{
  private final CraftWorld world;
  private final List<BlockState> blocks = new ArrayList();
  
  public StructureGrowDelegate(World world)
  {
    this.world = world.getWorld();
  }
  
  public boolean setRawTypeId(int x, int y, int z, int type)
  {
    return setRawTypeIdAndData(x, y, z, type, 0);
  }
  
  public boolean setRawTypeIdAndData(int x, int y, int z, int type, int data)
  {
    BlockState state = this.world.getBlockAt(x, y, z).getState();
    state.setTypeId(type);
    state.setData(new MaterialData(type, (byte)data));
    this.blocks.add(state);
    return true;
  }
  
  public boolean setTypeId(int x, int y, int z, int typeId)
  {
    return setRawTypeId(x, y, z, typeId);
  }
  
  public boolean setTypeIdAndData(int x, int y, int z, int typeId, int data)
  {
    return setRawTypeIdAndData(x, y, z, typeId, data);
  }
  
  public int getTypeId(int x, int y, int z)
  {
    for (BlockState state : this.blocks) {
      if ((state.getX() == x) && (state.getY() == y) && (state.getZ() == z)) {
        return state.getTypeId();
      }
    }
    return this.world.getBlockTypeIdAt(x, y, z);
  }
  
  public int getHeight()
  {
    return this.world.getMaxHeight();
  }
  
  public List<BlockState> getBlocks()
  {
    return this.blocks;
  }
  
  public boolean isEmpty(int x, int y, int z)
  {
    label79:
    for (Iterator localIterator = this.blocks.iterator(); localIterator.hasNext(); return false)
    {
      BlockState state = (BlockState)localIterator.next();
      if ((state.getX() != x) || (state.getY() != y) || (state.getZ() != z)) {
        break label79;
      }
      if (net.minecraft.server.v1_8_R3.Block.getById(state.getTypeId()) == Blocks.AIR) {
        return true;
      }
    }
    return this.world.getBlockAt(x, y, z).isEmpty();
  }
}
