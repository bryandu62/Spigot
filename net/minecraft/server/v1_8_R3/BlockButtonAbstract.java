package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.plugin.PluginManager;

public abstract class BlockButtonAbstract
  extends Block
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing");
  public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
  private final boolean N;
  
  protected BlockButtonAbstract(boolean flag)
  {
    super(Material.ORIENTABLE);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(POWERED, Boolean.valueOf(false)));
    a(true);
    a(CreativeModeTab.d);
    this.N = flag;
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return null;
  }
  
  public int a(World world)
  {
    return this.N ? 30 : 20;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean canPlace(World world, BlockPosition blockposition, EnumDirection enumdirection)
  {
    return a(world, blockposition, enumdirection.opposite());
  }
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    EnumDirection[] aenumdirection = EnumDirection.values();
    int i = aenumdirection.length;
    for (int j = 0; j < i; j++)
    {
      EnumDirection enumdirection = aenumdirection[j];
      if (a(world, blockposition, enumdirection)) {
        return true;
      }
    }
    return false;
  }
  
  protected static boolean a(World world, BlockPosition blockposition, EnumDirection enumdirection)
  {
    BlockPosition blockposition1 = blockposition.shift(enumdirection);
    
    return enumdirection == EnumDirection.DOWN ? World.a(world, blockposition1) : world.getType(blockposition1).getBlock().isOccluding();
  }
  
  public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving)
  {
    return a(world, blockposition, enumdirection.opposite()) ? getBlockData().set(FACING, enumdirection).set(POWERED, Boolean.valueOf(false)) : getBlockData().set(FACING, EnumDirection.DOWN).set(POWERED, Boolean.valueOf(false));
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if ((e(world, blockposition, iblockdata)) && (!a(world, blockposition, ((EnumDirection)iblockdata.get(FACING)).opposite())))
    {
      b(world, blockposition, iblockdata, 0);
      world.setAir(blockposition);
    }
  }
  
  private boolean e(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (canPlace(world, blockposition)) {
      return true;
    }
    b(world, blockposition, iblockdata, 0);
    world.setAir(blockposition);
    return false;
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    d(iblockaccess.getType(blockposition));
  }
  
  private void d(IBlockData iblockdata)
  {
    EnumDirection enumdirection = (EnumDirection)iblockdata.get(FACING);
    boolean flag = ((Boolean)iblockdata.get(POWERED)).booleanValue();
    
    float f2 = (flag ? 1 : 2) / 16.0F;
    switch (SyntheticClass_1.a[enumdirection.ordinal()])
    {
    case 1: 
      a(0.0F, 0.375F, 0.3125F, f2, 0.625F, 0.6875F);
      break;
    case 2: 
      a(1.0F - f2, 0.375F, 0.3125F, 1.0F, 0.625F, 0.6875F);
      break;
    case 3: 
      a(0.3125F, 0.375F, 0.0F, 0.6875F, 0.625F, f2);
      break;
    case 4: 
      a(0.3125F, 0.375F, 1.0F - f2, 0.6875F, 0.625F, 1.0F);
      break;
    case 5: 
      a(0.3125F, 0.0F, 0.375F, 0.6875F, 0.0F + f2, 0.625F);
      break;
    case 6: 
      a(0.3125F, 1.0F - f2, 0.375F, 0.6875F, 1.0F, 0.625F);
    }
  }
  
  public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2)
  {
    if (((Boolean)iblockdata.get(POWERED)).booleanValue()) {
      return true;
    }
    boolean powered = ((Boolean)iblockdata.get(POWERED)).booleanValue();
    org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
    int old = powered ? 15 : 0;
    int current = !powered ? 15 : 0;
    
    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
    world.getServer().getPluginManager().callEvent(eventRedstone);
    if ((eventRedstone.getNewCurrent() > 0 ? 1 : 0) != (powered ? 0 : 1)) {
      return true;
    }
    world.setTypeAndData(blockposition, iblockdata.set(POWERED, Boolean.valueOf(true)), 3);
    world.b(blockposition, blockposition);
    world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
    c(world, blockposition, (EnumDirection)iblockdata.get(FACING));
    world.a(blockposition, this, a(world));
    return true;
  }
  
  public void remove(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (((Boolean)iblockdata.get(POWERED)).booleanValue()) {
      c(world, blockposition, (EnumDirection)iblockdata.get(FACING));
    }
    super.remove(world, blockposition, iblockdata);
  }
  
  public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return ((Boolean)iblockdata.get(POWERED)).booleanValue() ? 15 : 0;
  }
  
  public int b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return iblockdata.get(FACING) == enumdirection ? 15 : !((Boolean)iblockdata.get(POWERED)).booleanValue() ? 0 : 0;
  }
  
  public boolean isPowerSource()
  {
    return true;
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {}
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if ((!world.isClientSide) && 
      (((Boolean)iblockdata.get(POWERED)).booleanValue())) {
      if (this.N)
      {
        f(world, blockposition, iblockdata);
      }
      else
      {
        org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
        
        BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
        world.getServer().getPluginManager().callEvent(eventRedstone);
        if (eventRedstone.getNewCurrent() > 0) {
          return;
        }
        world.setTypeUpdate(blockposition, iblockdata.set(POWERED, Boolean.valueOf(false)));
        c(world, blockposition, (EnumDirection)iblockdata.get(FACING));
        world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
        world.b(blockposition, blockposition);
      }
    }
  }
  
  public void j()
  {
    float f = 0.1875F;
    float f1 = 0.125F;
    float f2 = 0.125F;
    
    a(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity)
  {
    if ((!world.isClientSide) && 
      (this.N) && 
      (!((Boolean)iblockdata.get(POWERED)).booleanValue())) {
      f(world, blockposition, iblockdata);
    }
  }
  
  private void f(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    d(iblockdata);
    List list = world.a(EntityArrow.class, new AxisAlignedBB(blockposition.getX() + this.minX, blockposition.getY() + this.minY, blockposition.getZ() + this.minZ, blockposition.getX() + this.maxX, blockposition.getY() + this.maxY, blockposition.getZ() + this.maxZ));
    boolean flag = !list.isEmpty();
    boolean flag1 = ((Boolean)iblockdata.get(POWERED)).booleanValue();
    if ((flag1 != flag) && (flag))
    {
      org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
      boolean allowed = false;
      for (Object object : list) {
        if (object != null)
        {
          EntityInteractEvent event = new EntityInteractEvent(((Entity)object).getBukkitEntity(), block);
          world.getServer().getPluginManager().callEvent(event);
          if (!event.isCancelled())
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
    if ((flag) && (!flag1))
    {
      org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
      
      BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 0, 15);
      world.getServer().getPluginManager().callEvent(eventRedstone);
      if (eventRedstone.getNewCurrent() <= 0) {
        return;
      }
      world.setTypeUpdate(blockposition, iblockdata.set(POWERED, Boolean.valueOf(true)));
      c(world, blockposition, (EnumDirection)iblockdata.get(FACING));
      world.b(blockposition, blockposition);
      world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
    }
    if ((!flag) && (flag1))
    {
      org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
      
      BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
      world.getServer().getPluginManager().callEvent(eventRedstone);
      if (eventRedstone.getNewCurrent() > 0) {
        return;
      }
      world.setTypeUpdate(blockposition, iblockdata.set(POWERED, Boolean.valueOf(false)));
      c(world, blockposition, (EnumDirection)iblockdata.get(FACING));
      world.b(blockposition, blockposition);
      world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
    }
    if (flag) {
      world.a(blockposition, this, a(world));
    }
  }
  
  private void c(World world, BlockPosition blockposition, EnumDirection enumdirection)
  {
    world.applyPhysics(blockposition, this);
    world.applyPhysics(blockposition.shift(enumdirection.opposite()), this);
  }
  
  public IBlockData fromLegacyData(int i)
  {
    EnumDirection enumdirection;
    EnumDirection enumdirection;
    EnumDirection enumdirection;
    EnumDirection enumdirection;
    EnumDirection enumdirection;
    EnumDirection enumdirection;
    switch (i & 0x7)
    {
    case 0: 
      enumdirection = EnumDirection.DOWN;
      break;
    case 1: 
      enumdirection = EnumDirection.EAST;
      break;
    case 2: 
      enumdirection = EnumDirection.WEST;
      break;
    case 3: 
      enumdirection = EnumDirection.SOUTH;
      break;
    case 4: 
      enumdirection = EnumDirection.NORTH;
      break;
    case 5: 
    default: 
      enumdirection = EnumDirection.UP;
    }
    return getBlockData().set(FACING, enumdirection).set(POWERED, Boolean.valueOf((i & 0x8) > 0));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    int i;
    int i;
    int i;
    int i;
    int i;
    int i;
    switch (SyntheticClass_1.a[((EnumDirection)iblockdata.get(FACING)).ordinal()])
    {
    case 1: 
      i = 1;
      break;
    case 2: 
      i = 2;
      break;
    case 3: 
      i = 3;
      break;
    case 4: 
      i = 4;
      break;
    case 5: 
    default: 
      i = 5;
      break;
    case 6: 
      i = 0;
    }
    if (((Boolean)iblockdata.get(POWERED)).booleanValue()) {
      i |= 0x8;
    }
    return i;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, POWERED });
  }
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[EnumDirection.values().length];
    
    static
    {
      try
      {
        a[EnumDirection.EAST.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[EnumDirection.WEST.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        a[EnumDirection.SOUTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        a[EnumDirection.NORTH.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        a[EnumDirection.UP.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        a[EnumDirection.DOWN.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
    }
  }
}
