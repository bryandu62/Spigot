package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.plugin.PluginManager;

public class BlockSoil
  extends Block
{
  public static final BlockStateInteger MOISTURE = BlockStateInteger.of("moisture", 0, 7);
  
  protected BlockSoil()
  {
    super(Material.EARTH);
    j(this.blockStateList.getBlockData().set(MOISTURE, Integer.valueOf(0)));
    a(true);
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
    e(255);
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return new AxisAlignedBB(blockposition.getX(), blockposition.getY(), blockposition.getZ(), blockposition.getX() + 1, blockposition.getY() + 1, blockposition.getZ() + 1);
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    int i = ((Integer)iblockdata.get(MOISTURE)).intValue();
    if ((!f(world, blockposition)) && (!world.isRainingAt(blockposition.up())))
    {
      if (i > 0)
      {
        world.setTypeAndData(blockposition, iblockdata.set(MOISTURE, Integer.valueOf(i - 1)), 2);
      }
      else if (!e(world, blockposition))
      {
        org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
        if (CraftEventFactory.callBlockFadeEvent(block, Blocks.DIRT).isCancelled()) {
          return;
        }
        world.setTypeUpdate(blockposition, Blocks.DIRT.getBlockData());
      }
    }
    else if (i < 7) {
      world.setTypeAndData(blockposition, iblockdata.set(MOISTURE, Integer.valueOf(7)), 2);
    }
  }
  
  public void a(World world, BlockPosition blockposition, Entity entity, float f)
  {
    super.a(world, blockposition, entity, f);
    if (((entity instanceof EntityLiving)) && 
      (!world.isClientSide) && (world.random.nextFloat() < f - 0.5F))
    {
      if ((!(entity instanceof EntityHuman)) && (!world.getGameRules().getBoolean("mobGriefing"))) {
        return;
      }
      Cancellable cancellable;
      Cancellable cancellable;
      if ((entity instanceof EntityHuman))
      {
        cancellable = CraftEventFactory.callPlayerInteractEvent((EntityHuman)entity, Action.PHYSICAL, blockposition, null, null);
      }
      else
      {
        cancellable = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
        world.getServer().getPluginManager().callEvent((EntityInteractEvent)cancellable);
      }
      if (cancellable.isCancelled()) {
        return;
      }
      if (CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition.getX(), blockposition.getY(), blockposition.getZ(), Blocks.DIRT, 0).isCancelled()) {
        return;
      }
      world.setTypeUpdate(blockposition, Blocks.DIRT.getBlockData());
    }
  }
  
  private boolean e(World world, BlockPosition blockposition)
  {
    Block block = world.getType(blockposition.up()).getBlock();
    
    return ((block instanceof BlockCrops)) || ((block instanceof BlockStem));
  }
  
  private boolean f(World world, BlockPosition blockposition)
  {
    Iterator iterator = BlockPosition.b(blockposition.a(-4, 0, -4), blockposition.a(4, 1, 4)).iterator();
    BlockPosition.MutableBlockPosition blockposition_mutableblockposition;
    do
    {
      if (!iterator.hasNext()) {
        return false;
      }
      blockposition_mutableblockposition = (BlockPosition.MutableBlockPosition)iterator.next();
    } while (world.getType(blockposition_mutableblockposition).getBlock().getMaterial() != Material.WATER);
    return true;
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    super.doPhysics(world, blockposition, iblockdata, block);
    if (world.getType(blockposition.up()).getBlock().getMaterial().isBuildable()) {
      world.setTypeUpdate(blockposition, Blocks.DIRT.getBlockData());
    }
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return Blocks.DIRT.getDropType(Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.DIRT), random, i);
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(MOISTURE, Integer.valueOf(i & 0x7));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    return ((Integer)iblockdata.get(MOISTURE)).intValue();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { MOISTURE });
  }
}