package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;

public class BlockRedstoneOre
  extends Block
{
  private final boolean a;
  
  public BlockRedstoneOre(boolean flag)
  {
    super(Material.STONE);
    if (flag) {
      a(true);
    }
    this.a = flag;
  }
  
  public int a(World world)
  {
    return 30;
  }
  
  public void attack(World world, BlockPosition blockposition, EntityHuman entityhuman)
  {
    e(world, blockposition, entityhuman);
    super.attack(world, blockposition, entityhuman);
  }
  
  public void a(World world, BlockPosition blockposition, Entity entity)
  {
    if ((entity instanceof EntityHuman))
    {
      PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent((EntityHuman)entity, Action.PHYSICAL, blockposition, null, null);
      if (!event.isCancelled())
      {
        e(world, blockposition, entity);
        super.a(world, blockposition, entity);
      }
    }
    else
    {
      EntityInteractEvent event = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
      world.getServer().getPluginManager().callEvent(event);
      if (!event.isCancelled())
      {
        e(world, blockposition, entity);
        super.a(world, blockposition, entity);
      }
    }
  }
  
  public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2)
  {
    e(world, blockposition, entityhuman);
    return super.interact(world, blockposition, iblockdata, entityhuman, enumdirection, f, f1, f2);
  }
  
  private void e(World world, BlockPosition blockposition, Entity entity)
  {
    f(world, blockposition);
    if (this == Blocks.REDSTONE_ORE)
    {
      if (CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition.getX(), blockposition.getY(), blockposition.getZ(), Blocks.LIT_REDSTONE_ORE, 0).isCancelled()) {
        return;
      }
      world.setTypeUpdate(blockposition, Blocks.LIT_REDSTONE_ORE.getBlockData());
    }
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if (this == Blocks.LIT_REDSTONE_ORE)
    {
      if (CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), Blocks.REDSTONE_ORE).isCancelled()) {
        return;
      }
      world.setTypeUpdate(blockposition, Blocks.REDSTONE_ORE.getBlockData());
    }
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return Items.REDSTONE;
  }
  
  public int getDropCount(int i, Random random)
  {
    return a(random) + random.nextInt(i + 1);
  }
  
  public int a(Random random)
  {
    return 4 + random.nextInt(2);
  }
  
  public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i)
  {
    super.dropNaturally(world, blockposition, iblockdata, f, i);
  }
  
  public int getExpDrop(World world, IBlockData data, int i)
  {
    if (getDropType(data, world.random, i) != Item.getItemOf(this))
    {
      int j = 1 + world.random.nextInt(5);
      
      return j;
    }
    return 0;
  }
  
  private void f(World world, BlockPosition blockposition)
  {
    Random random = world.random;
    double d0 = 0.0625D;
    for (int i = 0; i < 6; i++)
    {
      double d1 = blockposition.getX() + random.nextFloat();
      double d2 = blockposition.getY() + random.nextFloat();
      double d3 = blockposition.getZ() + random.nextFloat();
      if ((i == 0) && (!world.getType(blockposition.up()).getBlock().c())) {
        d2 = blockposition.getY() + d0 + 1.0D;
      }
      if ((i == 1) && (!world.getType(blockposition.down()).getBlock().c())) {
        d2 = blockposition.getY() - d0;
      }
      if ((i == 2) && (!world.getType(blockposition.south()).getBlock().c())) {
        d3 = blockposition.getZ() + d0 + 1.0D;
      }
      if ((i == 3) && (!world.getType(blockposition.north()).getBlock().c())) {
        d3 = blockposition.getZ() - d0;
      }
      if ((i == 4) && (!world.getType(blockposition.east()).getBlock().c())) {
        d1 = blockposition.getX() + d0 + 1.0D;
      }
      if ((i == 5) && (!world.getType(blockposition.west()).getBlock().c())) {
        d1 = blockposition.getX() - d0;
      }
      if ((d1 < blockposition.getX()) || (d1 > blockposition.getX() + 1) || (d2 < 0.0D) || (d2 > blockposition.getY() + 1) || (d3 < blockposition.getZ()) || (d3 > blockposition.getZ() + 1)) {
        world.addParticle(EnumParticle.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D, new int[0]);
      }
    }
  }
  
  protected ItemStack i(IBlockData iblockdata)
  {
    return new ItemStack(Blocks.REDSTONE_ORE);
  }
}
