package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.plugin.PluginManager;

public class BlockDragonEgg
  extends Block
{
  public BlockDragonEgg()
  {
    super(Material.DRAGON_EGG, MaterialMapColor.E);
    a(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
  }
  
  public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    world.a(blockposition, this, a(world));
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    world.a(blockposition, this, a(world));
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    e(world, blockposition);
  }
  
  private void e(World world, BlockPosition blockposition)
  {
    if ((BlockFalling.canFall(world, blockposition.down())) && (blockposition.getY() >= 0))
    {
      byte b0 = 32;
      if ((!BlockFalling.instaFall) && (world.areChunksLoadedBetween(blockposition.a(-b0, -b0, -b0), blockposition.a(b0, b0, b0))))
      {
        world.addEntity(new EntityFallingBlock(world, blockposition.getX() + 0.5F, blockposition.getY(), blockposition.getZ() + 0.5F, getBlockData()));
      }
      else
      {
        world.setAir(blockposition);
        for (BlockPosition blockposition1 = blockposition; (BlockFalling.canFall(world, blockposition1)) && (blockposition1.getY() > 0); blockposition1 = blockposition1.down()) {}
        if (blockposition1.getY() > 0) {
          world.setTypeAndData(blockposition1, getBlockData(), 2);
        }
      }
    }
  }
  
  public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2)
  {
    f(world, blockposition);
    return true;
  }
  
  public void attack(World world, BlockPosition blockposition, EntityHuman entityhuman)
  {
    f(world, blockposition);
  }
  
  private void f(World world, BlockPosition blockposition)
  {
    IBlockData iblockdata = world.getType(blockposition);
    if (iblockdata.getBlock() == this) {
      for (int i = 0; i < 1000; i++)
      {
        BlockPosition blockposition1 = blockposition.a(world.random.nextInt(16) - world.random.nextInt(16), world.random.nextInt(8) - world.random.nextInt(8), world.random.nextInt(16) - world.random.nextInt(16));
        if (world.getType(blockposition1).getBlock().material == Material.AIR)
        {
          org.bukkit.block.Block from = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
          org.bukkit.block.Block to = world.getWorld().getBlockAt(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
          BlockFromToEvent event = new BlockFromToEvent(from, to);
          Bukkit.getPluginManager().callEvent(event);
          if (event.isCancelled()) {
            return;
          }
          blockposition1 = new BlockPosition(event.getToBlock().getX(), event.getToBlock().getY(), event.getToBlock().getZ());
          if (world.isClientSide)
          {
            for (int j = 0; j < 128; j++)
            {
              double d0 = world.random.nextDouble();
              float f = (world.random.nextFloat() - 0.5F) * 0.2F;
              float f1 = (world.random.nextFloat() - 0.5F) * 0.2F;
              float f2 = (world.random.nextFloat() - 0.5F) * 0.2F;
              double d1 = blockposition1.getX() + (blockposition.getX() - blockposition1.getX()) * d0 + (world.random.nextDouble() - 0.5D) * 1.0D + 0.5D;
              double d2 = blockposition1.getY() + (blockposition.getY() - blockposition1.getY()) * d0 + world.random.nextDouble() * 1.0D - 0.5D;
              double d3 = blockposition1.getZ() + (blockposition.getZ() - blockposition1.getZ()) * d0 + (world.random.nextDouble() - 0.5D) * 1.0D + 0.5D;
              
              world.addParticle(EnumParticle.PORTAL, d1, d2, d3, f, f1, f2, new int[0]);
            }
          }
          else
          {
            world.setTypeAndData(blockposition1, iblockdata, 2);
            world.setAir(blockposition);
          }
          return;
        }
      }
    }
  }
  
  public int a(World world)
  {
    return 5;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
}
