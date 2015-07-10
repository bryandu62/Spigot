package net.minecraft.server.v1_8_R3;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import org.bukkit.Server;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.plugin.PluginManager;

public class BlockFlowing
  extends BlockFluids
{
  int a;
  
  protected BlockFlowing(Material material)
  {
    super(material);
  }
  
  private void f(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    world.setTypeAndData(blockposition, b(this.material).getBlockData().set(LEVEL, (Integer)iblockdata.get(LEVEL)), 2);
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    org.bukkit.World bworld = world.getWorld();
    Server server = world.getServer();
    org.bukkit.block.Block source = bworld == null ? null : bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
    
    int i = ((Integer)iblockdata.get(LEVEL)).intValue();
    byte b0 = 1;
    if ((this.material == Material.LAVA) && (!world.worldProvider.n())) {
      b0 = 2;
    }
    int j = a(world);
    if (i > 0)
    {
      int l = -100;
      
      this.a = 0;
      EnumDirection enumdirection;
      for (Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator(); iterator.hasNext(); l = a(world, blockposition.shift(enumdirection), l)) {
        enumdirection = (EnumDirection)iterator.next();
      }
      int i1 = l + b0;
      if ((i1 >= 8) || (l < 0)) {
        i1 = -1;
      }
      if (e(world, blockposition.up()) >= 0)
      {
        int k = e(world, blockposition.up());
        if (k >= 8) {
          i1 = k;
        } else {
          i1 = k + 8;
        }
      }
      if ((this.a >= 2) && (this.material == Material.WATER))
      {
        IBlockData iblockdata1 = world.getType(blockposition.down());
        if (iblockdata1.getBlock().getMaterial().isBuildable()) {
          i1 = 0;
        } else if ((iblockdata1.getBlock().getMaterial() == this.material) && (((Integer)iblockdata1.get(LEVEL)).intValue() == 0)) {
          i1 = 0;
        }
      }
      if ((this.material == Material.LAVA) && (i < 8) && (i1 < 8) && (i1 > i) && (random.nextInt(4) != 0)) {
        j *= 4;
      }
      if (i1 == i)
      {
        f(world, blockposition, iblockdata);
      }
      else
      {
        i = i1;
        if (i1 < 0)
        {
          world.setAir(blockposition);
        }
        else
        {
          iblockdata = iblockdata.set(LEVEL, Integer.valueOf(i1));
          world.setTypeAndData(blockposition, iblockdata, 2);
          world.a(blockposition, this, j);
          world.applyPhysics(blockposition, this);
        }
      }
    }
    else
    {
      f(world, blockposition, iblockdata);
    }
    IBlockData iblockdata2 = world.getType(blockposition.down());
    if (h(world, blockposition.down(), iblockdata2))
    {
      BlockFromToEvent event = new BlockFromToEvent(source, BlockFace.DOWN);
      if (server != null) {
        server.getPluginManager().callEvent(event);
      }
      if (!event.isCancelled())
      {
        if ((this.material == Material.LAVA) && (world.getType(blockposition.down()).getBlock().getMaterial() == Material.WATER))
        {
          world.setTypeUpdate(blockposition.down(), Blocks.STONE.getBlockData());
          fizz(world, blockposition.down());
          return;
        }
        if (i >= 8) {
          flow(world, blockposition.down(), iblockdata2, i);
        } else {
          flow(world, blockposition.down(), iblockdata2, i + 8);
        }
      }
    }
    else if ((i >= 0) && ((i == 0) || (g(world, blockposition.down(), iblockdata2))))
    {
      Set set = f(world, blockposition);
      
      int k = i + b0;
      if (i >= 8) {
        k = 1;
      }
      if (k >= 8) {
        return;
      }
      Iterator iterator1 = set.iterator();
      while (iterator1.hasNext())
      {
        EnumDirection enumdirection1 = (EnumDirection)iterator1.next();
        
        BlockFromToEvent event = new BlockFromToEvent(source, CraftBlock.notchToBlockFace(enumdirection1));
        if (server != null) {
          server.getPluginManager().callEvent(event);
        }
        if (!event.isCancelled()) {
          flow(world, blockposition.shift(enumdirection1), world.getType(blockposition.shift(enumdirection1)), k);
        }
      }
    }
  }
  
  private void flow(World world, BlockPosition blockposition, IBlockData iblockdata, int i)
  {
    if ((world.isLoaded(blockposition)) && (h(world, blockposition, iblockdata)))
    {
      if (iblockdata.getBlock() != Blocks.AIR) {
        if (this.material == Material.LAVA) {
          fizz(world, blockposition);
        } else {
          iblockdata.getBlock().b(world, blockposition, iblockdata, 0);
        }
      }
      world.setTypeAndData(blockposition, getBlockData().set(LEVEL, Integer.valueOf(i)), 3);
    }
  }
  
  private int a(World world, BlockPosition blockposition, int i, EnumDirection enumdirection)
  {
    int j = 1000;
    Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
    while (iterator.hasNext())
    {
      EnumDirection enumdirection1 = (EnumDirection)iterator.next();
      if (enumdirection1 != enumdirection)
      {
        BlockPosition blockposition1 = blockposition.shift(enumdirection1);
        IBlockData iblockdata = world.getType(blockposition1);
        if ((!g(world, blockposition1, iblockdata)) && ((iblockdata.getBlock().getMaterial() != this.material) || (((Integer)iblockdata.get(LEVEL)).intValue() > 0)))
        {
          if (!g(world, blockposition1.down(), iblockdata)) {
            return i;
          }
          if (i < 4)
          {
            int k = a(world, blockposition1, i + 1, enumdirection1.opposite());
            if (k < j) {
              j = k;
            }
          }
        }
      }
    }
    return j;
  }
  
  private Set<EnumDirection> f(World world, BlockPosition blockposition)
  {
    int i = 1000;
    EnumSet enumset = EnumSet.noneOf(EnumDirection.class);
    Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
    while (iterator.hasNext())
    {
      EnumDirection enumdirection = (EnumDirection)iterator.next();
      BlockPosition blockposition1 = blockposition.shift(enumdirection);
      IBlockData iblockdata = world.getType(blockposition1);
      if ((!g(world, blockposition1, iblockdata)) && ((iblockdata.getBlock().getMaterial() != this.material) || (((Integer)iblockdata.get(LEVEL)).intValue() > 0)))
      {
        int j;
        int j;
        if (g(world, blockposition1.down(), world.getType(blockposition1.down()))) {
          j = a(world, blockposition1, 1, enumdirection.opposite());
        } else {
          j = 0;
        }
        if (j < i) {
          enumset.clear();
        }
        if (j <= i)
        {
          enumset.add(enumdirection);
          i = j;
        }
      }
    }
    return enumset;
  }
  
  private boolean g(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    Block block = world.getType(blockposition).getBlock();
    
    return block.material == Material.PORTAL;
  }
  
  protected int a(World world, BlockPosition blockposition, int i)
  {
    int j = e(world, blockposition);
    if (j < 0) {
      return i;
    }
    if (j == 0) {
      this.a += 1;
    }
    if (j >= 8) {
      j = 0;
    }
    return (i >= 0) && (j >= i) ? i : j;
  }
  
  private boolean h(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    Material material = iblockdata.getBlock().getMaterial();
    
    return (material != this.material) && (material != Material.LAVA) && (!g(world, blockposition, iblockdata));
  }
  
  public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (!e(world, blockposition, iblockdata)) {
      world.a(blockposition, this, a(world));
    }
  }
}
