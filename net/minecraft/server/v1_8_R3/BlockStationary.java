package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.block.BlockIgniteEvent;

public class BlockStationary
  extends BlockFluids
{
  protected BlockStationary(Material material)
  {
    super(material);
    a(false);
    if (material == Material.LAVA) {
      a(true);
    }
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if (!e(world, blockposition, iblockdata)) {
      f(world, blockposition, iblockdata);
    }
  }
  
  private void f(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    BlockFlowing blockflowing = a(this.material);
    
    world.setTypeAndData(blockposition, blockflowing.getBlockData().set(LEVEL, (Integer)iblockdata.get(LEVEL)), 2);
    world.a(blockposition, blockflowing, a(world));
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if ((this.material == Material.LAVA) && 
      (world.getGameRules().getBoolean("doFireTick")))
    {
      int i = random.nextInt(3);
      if (i > 0)
      {
        BlockPosition blockposition1 = blockposition;
        for (int j = 0; j < i; j++)
        {
          blockposition1 = blockposition1.a(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
          Block block = world.getType(blockposition1).getBlock();
          if (block.material == Material.AIR)
          {
            if (f(world, blockposition1)) {
              if ((world.getType(blockposition1) == Blocks.FIRE) || 
                (!CraftEventFactory.callBlockIgniteEvent(world, blockposition1.getX(), blockposition1.getY(), blockposition1.getZ(), blockposition.getX(), blockposition.getY(), blockposition.getZ()).isCancelled())) {
                world.setTypeUpdate(blockposition1, Blocks.FIRE.getBlockData());
              }
            }
          }
          else if (block.material.isSolid()) {
            return;
          }
        }
      }
      else
      {
        for (int k = 0; k < 3; k++)
        {
          BlockPosition blockposition2 = blockposition.a(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
          if ((world.isEmpty(blockposition2.up())) && (m(world, blockposition2)))
          {
            BlockPosition up = blockposition2.up();
            if ((world.getType(up) == Blocks.FIRE) || 
              (!CraftEventFactory.callBlockIgniteEvent(world, up.getX(), up.getY(), up.getZ(), blockposition.getX(), blockposition.getY(), blockposition.getZ()).isCancelled())) {
              world.setTypeUpdate(blockposition2.up(), Blocks.FIRE.getBlockData());
            }
          }
        }
      }
    }
  }
  
  protected boolean f(World world, BlockPosition blockposition)
  {
    EnumDirection[] aenumdirection = EnumDirection.values();
    int i = aenumdirection.length;
    for (int j = 0; j < i; j++)
    {
      EnumDirection enumdirection = aenumdirection[j];
      if (m(world, blockposition.shift(enumdirection))) {
        return true;
      }
    }
    return false;
  }
  
  private boolean m(World world, BlockPosition blockposition)
  {
    return world.getType(blockposition).getBlock().getMaterial().isBurnable();
  }
}
