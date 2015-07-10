package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.plugin.PluginManager;

public class BlockTripwire
  extends Block
{
  public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
  public static final BlockStateBoolean SUSPENDED = BlockStateBoolean.of("suspended");
  public static final BlockStateBoolean ATTACHED = BlockStateBoolean.of("attached");
  public static final BlockStateBoolean DISARMED = BlockStateBoolean.of("disarmed");
  public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
  public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
  public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
  public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");
  
  public BlockTripwire()
  {
    super(Material.ORIENTABLE);
    j(this.blockStateList.getBlockData().set(POWERED, Boolean.valueOf(false)).set(SUSPENDED, Boolean.valueOf(false)).set(ATTACHED, Boolean.valueOf(false)).set(DISARMED, Boolean.valueOf(false)).set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)));
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
    a(true);
  }
  
  public IBlockData updateState(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    return iblockdata.set(NORTH, Boolean.valueOf(c(iblockaccess, blockposition, iblockdata, EnumDirection.NORTH))).set(EAST, Boolean.valueOf(c(iblockaccess, blockposition, iblockdata, EnumDirection.EAST))).set(SOUTH, Boolean.valueOf(c(iblockaccess, blockposition, iblockdata, EnumDirection.SOUTH))).set(WEST, Boolean.valueOf(c(iblockaccess, blockposition, iblockdata, EnumDirection.WEST)));
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return null;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return Items.STRING;
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    boolean flag = ((Boolean)iblockdata.get(SUSPENDED)).booleanValue();
    boolean flag1 = !World.a(world, blockposition.down());
    if (flag != flag1)
    {
      b(world, blockposition, iblockdata, 0);
      world.setAir(blockposition);
    }
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    IBlockData iblockdata = iblockaccess.getType(blockposition);
    boolean flag = ((Boolean)iblockdata.get(ATTACHED)).booleanValue();
    boolean flag1 = ((Boolean)iblockdata.get(SUSPENDED)).booleanValue();
    if (!flag1) {
      a(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
    } else if (!flag) {
      a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    } else {
      a(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
    }
  }
  
  public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    iblockdata = iblockdata.set(SUSPENDED, Boolean.valueOf(!World.a(world, blockposition.down())));
    world.setTypeAndData(blockposition, iblockdata, 3);
    e(world, blockposition, iblockdata);
  }
  
  public void remove(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    e(world, blockposition, iblockdata.set(POWERED, Boolean.valueOf(true)));
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman)
  {
    if ((!world.isClientSide) && 
      (entityhuman.bZ() != null) && (entityhuman.bZ().getItem() == Items.SHEARS)) {
      world.setTypeAndData(blockposition, iblockdata.set(DISARMED, Boolean.valueOf(true)), 4);
    }
  }
  
  private void e(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    EnumDirection[] aenumdirection = { EnumDirection.SOUTH, EnumDirection.WEST };
    int i = aenumdirection.length;
    int j = 0;
    while (j < i)
    {
      EnumDirection enumdirection = aenumdirection[j];
      int k = 1;
      while (k < 42)
      {
        BlockPosition blockposition1 = blockposition.shift(enumdirection, k);
        IBlockData iblockdata1 = world.getType(blockposition1);
        if (iblockdata1.getBlock() == Blocks.TRIPWIRE_HOOK)
        {
          if (iblockdata1.get(BlockTripwireHook.FACING) != enumdirection.opposite()) {
            break;
          }
          Blocks.TRIPWIRE_HOOK.a(world, blockposition1, iblockdata1, false, true, k, iblockdata);
          
          break;
        }
        if (iblockdata1.getBlock() != Blocks.TRIPWIRE) {
          break;
        }
        k++;
      }
      j++;
    }
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity)
  {
    if ((!world.isClientSide) && 
      (!((Boolean)iblockdata.get(POWERED)).booleanValue())) {
      e(world, blockposition);
    }
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {}
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if ((!world.isClientSide) && 
      (((Boolean)world.getType(blockposition).get(POWERED)).booleanValue())) {
      e(world, blockposition);
    }
  }
  
  private void e(World world, BlockPosition blockposition)
  {
    IBlockData iblockdata = world.getType(blockposition);
    boolean flag = ((Boolean)iblockdata.get(POWERED)).booleanValue();
    boolean flag1 = false;
    List list = world.getEntities(null, new AxisAlignedBB(blockposition.getX() + this.minX, blockposition.getY() + this.minY, blockposition.getZ() + this.minZ, blockposition.getX() + this.maxX, blockposition.getY() + this.maxY, blockposition.getZ() + this.maxZ));
    if (!list.isEmpty())
    {
      Iterator iterator = list.iterator();
      while (iterator.hasNext())
      {
        Entity entity = (Entity)iterator.next();
        if (!entity.aI())
        {
          flag1 = true;
          break;
        }
      }
    }
    if ((flag != flag1) && (flag1) && (((Boolean)iblockdata.get(ATTACHED)).booleanValue()))
    {
      org.bukkit.World bworld = world.getWorld();
      PluginManager manager = world.getServer().getPluginManager();
      org.bukkit.block.Block block = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
      boolean allowed = false;
      for (Object object : list) {
        if (object != null)
        {
          Cancellable cancellable;
          Cancellable cancellable;
          if ((object instanceof EntityHuman))
          {
            cancellable = CraftEventFactory.callPlayerInteractEvent((EntityHuman)object, Action.PHYSICAL, blockposition, null, null);
          }
          else
          {
            if (!(object instanceof Entity)) {
              continue;
            }
            cancellable = new EntityInteractEvent(((Entity)object).getBukkitEntity(), block);
            manager.callEvent((EntityInteractEvent)cancellable);
          }
          if (!cancellable.isCancelled())
          {
            allowed = true;
            break;
          }
        }
      }
      if (!allowed) {
        return;
      }
    }
    if (flag1 != flag)
    {
      iblockdata = iblockdata.set(POWERED, Boolean.valueOf(flag1));
      world.setTypeAndData(blockposition, iblockdata, 3);
      e(world, blockposition, iblockdata);
    }
    if (flag1) {
      world.a(blockposition, this, a(world));
    }
  }
  
  public static boolean c(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    BlockPosition blockposition1 = blockposition.shift(enumdirection);
    IBlockData iblockdata1 = iblockaccess.getType(blockposition1);
    Block block = iblockdata1.getBlock();
    if (block == Blocks.TRIPWIRE_HOOK)
    {
      EnumDirection enumdirection1 = enumdirection.opposite();
      
      return iblockdata1.get(BlockTripwireHook.FACING) == enumdirection1;
    }
    if (block == Blocks.TRIPWIRE)
    {
      boolean flag = ((Boolean)iblockdata.get(SUSPENDED)).booleanValue();
      boolean flag1 = ((Boolean)iblockdata1.get(SUSPENDED)).booleanValue();
      
      return flag == flag1;
    }
    return false;
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(POWERED, Boolean.valueOf((i & 0x1) > 0)).set(SUSPENDED, Boolean.valueOf((i & 0x2) > 0)).set(ATTACHED, Boolean.valueOf((i & 0x4) > 0)).set(DISARMED, Boolean.valueOf((i & 0x8) > 0));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    int i = 0;
    if (((Boolean)iblockdata.get(POWERED)).booleanValue()) {
      i |= 0x1;
    }
    if (((Boolean)iblockdata.get(SUSPENDED)).booleanValue()) {
      i |= 0x2;
    }
    if (((Boolean)iblockdata.get(ATTACHED)).booleanValue()) {
      i |= 0x4;
    }
    if (((Boolean)iblockdata.get(DISARMED)).booleanValue()) {
      i |= 0x8;
    }
    return i;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { POWERED, SUSPENDED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH });
  }
}
