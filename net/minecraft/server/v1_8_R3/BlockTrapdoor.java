package net.minecraft.server.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;

public class BlockTrapdoor
  extends Block
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  public static final BlockStateBoolean OPEN = BlockStateBoolean.of("open");
  public static final BlockStateEnum<EnumTrapdoorHalf> HALF = BlockStateEnum.of("half", EnumTrapdoorHalf.class);
  
  protected BlockTrapdoor(Material material)
  {
    super(material);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(OPEN, Boolean.valueOf(false)).set(HALF, EnumTrapdoorHalf.BOTTOM));
    
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    a(CreativeModeTab.d);
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean b(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    return !((Boolean)iblockaccess.getType(blockposition).get(OPEN)).booleanValue();
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    updateShape(world, blockposition);
    return super.a(world, blockposition, iblockdata);
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    d(iblockaccess.getType(blockposition));
  }
  
  public void j()
  {
    a(0.0F, 0.40625F, 0.0F, 1.0F, 0.59375F, 1.0F);
  }
  
  public void d(IBlockData iblockdata)
  {
    if (iblockdata.getBlock() == this)
    {
      boolean flag = iblockdata.get(HALF) == EnumTrapdoorHalf.TOP;
      Boolean obool = (Boolean)iblockdata.get(OPEN);
      EnumDirection enumdirection = (EnumDirection)iblockdata.get(FACING);
      if (flag) {
        a(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
      } else {
        a(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
      }
      if (obool.booleanValue())
      {
        if (enumdirection == EnumDirection.NORTH) {
          a(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
        }
        if (enumdirection == EnumDirection.SOUTH) {
          a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
        }
        if (enumdirection == EnumDirection.WEST) {
          a(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        if (enumdirection == EnumDirection.EAST) {
          a(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
        }
      }
    }
  }
  
  public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2)
  {
    if (this.material == Material.ORE) {
      return true;
    }
    iblockdata = iblockdata.a(OPEN);
    world.setTypeAndData(blockposition, iblockdata, 2);
    world.a(entityhuman, ((Boolean)iblockdata.get(OPEN)).booleanValue() ? 1003 : 1006, blockposition, 0);
    return true;
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if (!world.isClientSide)
    {
      BlockPosition blockposition1 = blockposition.shift(((EnumDirection)iblockdata.get(FACING)).opposite());
      if (!c(world.getType(blockposition1).getBlock()))
      {
        world.setAir(blockposition);
        b(world, blockposition, iblockdata, 0);
      }
      else
      {
        boolean flag = world.isBlockIndirectlyPowered(blockposition);
        if ((flag) || (block.isPowerSource()))
        {
          org.bukkit.World bworld = world.getWorld();
          org.bukkit.block.Block bblock = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
          
          int power = bblock.getBlockPower();
          int oldPower = ((Boolean)iblockdata.get(OPEN)).booleanValue() ? 15 : 0;
          if ((((oldPower == 0 ? 1 : 0) ^ (power == 0 ? 1 : 0)) != 0) || (block.isPowerSource()))
          {
            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bblock, oldPower, power);
            world.getServer().getPluginManager().callEvent(eventRedstone);
            flag = eventRedstone.getNewCurrent() > 0;
          }
          boolean flag1 = ((Boolean)iblockdata.get(OPEN)).booleanValue();
          if (flag1 != flag)
          {
            world.setTypeAndData(blockposition, iblockdata.set(OPEN, Boolean.valueOf(flag)), 2);
            world.a(null, flag ? 1003 : 1006, blockposition, 0);
          }
        }
      }
    }
  }
  
  public MovingObjectPosition a(World world, BlockPosition blockposition, Vec3D vec3d, Vec3D vec3d1)
  {
    updateShape(world, blockposition);
    return super.a(world, blockposition, vec3d, vec3d1);
  }
  
  public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving)
  {
    IBlockData iblockdata = getBlockData();
    if (enumdirection.k().c())
    {
      iblockdata = iblockdata.set(FACING, enumdirection).set(OPEN, Boolean.valueOf(false));
      iblockdata = iblockdata.set(HALF, f1 > 0.5F ? EnumTrapdoorHalf.TOP : EnumTrapdoorHalf.BOTTOM);
    }
    return iblockdata;
  }
  
  public boolean canPlace(World world, BlockPosition blockposition, EnumDirection enumdirection)
  {
    return (!enumdirection.k().b()) && (c(world.getType(blockposition.shift(enumdirection.opposite())).getBlock()));
  }
  
  protected static EnumDirection b(int i)
  {
    switch (i & 0x3)
    {
    case 0: 
      return EnumDirection.NORTH;
    case 1: 
      return EnumDirection.SOUTH;
    case 2: 
      return EnumDirection.WEST;
    }
    return EnumDirection.EAST;
  }
  
  protected static int a(EnumDirection enumdirection)
  {
    switch (SyntheticClass_1.a[enumdirection.ordinal()])
    {
    case 1: 
      return 0;
    case 2: 
      return 1;
    case 3: 
      return 2;
    }
    return 3;
  }
  
  private static boolean c(Block block)
  {
    return ((block.material.k()) && (block.d())) || (block == Blocks.GLOWSTONE) || ((block instanceof BlockStepAbstract)) || ((block instanceof BlockStairs));
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(FACING, b(i)).set(OPEN, Boolean.valueOf((i & 0x4) != 0)).set(HALF, (i & 0x8) == 0 ? EnumTrapdoorHalf.BOTTOM : EnumTrapdoorHalf.TOP);
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    byte b0 = 0;
    int i = b0 | a((EnumDirection)iblockdata.get(FACING));
    if (((Boolean)iblockdata.get(OPEN)).booleanValue()) {
      i |= 0x4;
    }
    if (iblockdata.get(HALF) == EnumTrapdoorHalf.TOP) {
      i |= 0x8;
    }
    return i;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, OPEN, HALF });
  }
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[EnumDirection.values().length];
    
    static
    {
      try
      {
        a[EnumDirection.NORTH.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[EnumDirection.SOUTH.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        a[EnumDirection.WEST.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        a[EnumDirection.EAST.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
    }
  }
  
  public static enum EnumTrapdoorHalf
    implements INamable
  {
    TOP("top"),  BOTTOM("bottom");
    
    private final String c;
    
    private EnumTrapdoorHalf(String s)
    {
      this.c = s;
    }
    
    public String toString()
    {
      return this.c;
    }
    
    public String getName()
    {
      return this.c;
    }
  }
}
