package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;

public class BlockDoor
  extends Block
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  public static final BlockStateBoolean OPEN = BlockStateBoolean.of("open");
  public static final BlockStateEnum<EnumDoorHinge> HINGE = BlockStateEnum.of("hinge", EnumDoorHinge.class);
  public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
  public static final BlockStateEnum<EnumDoorHalf> HALF = BlockStateEnum.of("half", EnumDoorHalf.class);
  
  protected BlockDoor(Material material)
  {
    super(material);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(OPEN, Boolean.valueOf(false)).set(HINGE, EnumDoorHinge.LEFT).set(POWERED, Boolean.valueOf(false)).set(HALF, EnumDoorHalf.LOWER));
  }
  
  public String getName()
  {
    return LocaleI18n.get((a() + ".name").replaceAll("tile", "item"));
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean b(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    return g(e(iblockaccess, blockposition));
  }
  
  public boolean d()
  {
    return false;
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    updateShape(world, blockposition);
    return super.a(world, blockposition, iblockdata);
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    k(e(iblockaccess, blockposition));
  }
  
  private void k(int i)
  {
    float f = 0.1875F;
    
    a(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
    EnumDirection enumdirection = f(i);
    boolean flag = g(i);
    boolean flag1 = j(i);
    if (flag)
    {
      if (enumdirection == EnumDirection.EAST)
      {
        if (!flag1) {
          a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        } else {
          a(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        }
      }
      else if (enumdirection == EnumDirection.SOUTH)
      {
        if (!flag1) {
          a(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        } else {
          a(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        }
      }
      else if (enumdirection == EnumDirection.WEST)
      {
        if (!flag1) {
          a(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        } else {
          a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        }
      }
      else if (enumdirection == EnumDirection.NORTH) {
        if (!flag1) {
          a(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        } else {
          a(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
      }
    }
    else if (enumdirection == EnumDirection.EAST) {
      a(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
    } else if (enumdirection == EnumDirection.SOUTH) {
      a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
    } else if (enumdirection == EnumDirection.WEST) {
      a(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    } else if (enumdirection == EnumDirection.NORTH) {
      a(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
    }
  }
  
  public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2)
  {
    if (this.material == Material.ORE) {
      return true;
    }
    BlockPosition blockposition1 = iblockdata.get(HALF) == EnumDoorHalf.LOWER ? blockposition : blockposition.down();
    IBlockData iblockdata1 = blockposition.equals(blockposition1) ? iblockdata : world.getType(blockposition1);
    if (iblockdata1.getBlock() != this) {
      return false;
    }
    iblockdata = iblockdata1.a(OPEN);
    world.setTypeAndData(blockposition1, iblockdata, 2);
    world.b(blockposition1, blockposition);
    world.a(entityhuman, ((Boolean)iblockdata.get(OPEN)).booleanValue() ? 1003 : 1006, blockposition, 0);
    return true;
  }
  
  public void setDoor(World world, BlockPosition blockposition, boolean flag)
  {
    IBlockData iblockdata = world.getType(blockposition);
    if (iblockdata.getBlock() == this)
    {
      BlockPosition blockposition1 = iblockdata.get(HALF) == EnumDoorHalf.LOWER ? blockposition : blockposition.down();
      IBlockData iblockdata1 = blockposition == blockposition1 ? iblockdata : world.getType(blockposition1);
      if ((iblockdata1.getBlock() == this) && (((Boolean)iblockdata1.get(OPEN)).booleanValue() != flag))
      {
        world.setTypeAndData(blockposition1, iblockdata1.set(OPEN, Boolean.valueOf(flag)), 2);
        world.b(blockposition1, blockposition);
        world.a(null, flag ? 1003 : 1006, blockposition, 0);
      }
    }
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if (iblockdata.get(HALF) == EnumDoorHalf.UPPER)
    {
      BlockPosition blockposition1 = blockposition.down();
      IBlockData iblockdata1 = world.getType(blockposition1);
      if (iblockdata1.getBlock() != this) {
        world.setAir(blockposition);
      } else if (block != this) {
        doPhysics(world, blockposition1, iblockdata1, block);
      }
    }
    else
    {
      boolean flag = false;
      BlockPosition blockposition2 = blockposition.up();
      IBlockData iblockdata2 = world.getType(blockposition2);
      if (iblockdata2.getBlock() != this)
      {
        world.setAir(blockposition);
        flag = true;
      }
      if (!World.a(world, blockposition.down()))
      {
        world.setAir(blockposition);
        flag = true;
        if (iblockdata2.getBlock() == this) {
          world.setAir(blockposition2);
        }
      }
      if (flag)
      {
        if (!world.isClientSide) {
          b(world, blockposition, iblockdata, 0);
        }
      }
      else
      {
        org.bukkit.World bworld = world.getWorld();
        org.bukkit.block.Block bukkitBlock = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
        org.bukkit.block.Block blockTop = bworld.getBlockAt(blockposition2.getX(), blockposition2.getY(), blockposition2.getZ());
        
        int power = bukkitBlock.getBlockPower();
        int powerTop = blockTop.getBlockPower();
        if (powerTop > power) {
          power = powerTop;
        }
        int oldPower = ((Boolean)iblockdata2.get(POWERED)).booleanValue() ? 15 : 0;
        if (((oldPower == 0 ? 1 : 0) ^ (power == 0 ? 1 : 0)) != 0)
        {
          BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bukkitBlock, oldPower, power);
          world.getServer().getPluginManager().callEvent(eventRedstone);
          
          boolean flag1 = eventRedstone.getNewCurrent() > 0;
          world.setTypeAndData(blockposition2, iblockdata2.set(POWERED, Boolean.valueOf(flag1)), 2);
          if (flag1 != ((Boolean)iblockdata.get(OPEN)).booleanValue())
          {
            world.setTypeAndData(blockposition, iblockdata.set(OPEN, Boolean.valueOf(flag1)), 2);
            world.b(blockposition, blockposition);
            world.a(null, flag1 ? 1003 : 1006, blockposition, 0);
          }
        }
      }
    }
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return iblockdata.get(HALF) == EnumDoorHalf.UPPER ? null : l();
  }
  
  public MovingObjectPosition a(World world, BlockPosition blockposition, Vec3D vec3d, Vec3D vec3d1)
  {
    updateShape(world, blockposition);
    return super.a(world, blockposition, vec3d, vec3d1);
  }
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    return blockposition.getY() < 255;
  }
  
  public int k()
  {
    return 1;
  }
  
  public static int e(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    IBlockData iblockdata = iblockaccess.getType(blockposition);
    int i = iblockdata.getBlock().toLegacyData(iblockdata);
    boolean flag = i(i);
    IBlockData iblockdata1 = iblockaccess.getType(blockposition.down());
    int j = iblockdata1.getBlock().toLegacyData(iblockdata1);
    int k = flag ? j : i;
    IBlockData iblockdata2 = iblockaccess.getType(blockposition.up());
    int l = iblockdata2.getBlock().toLegacyData(iblockdata2);
    int i1 = flag ? i : l;
    boolean flag1 = (i1 & 0x1) != 0;
    boolean flag2 = (i1 & 0x2) != 0;
    
    return b(k) | (flag ? 8 : 0) | (flag1 ? 16 : 0) | (flag2 ? 32 : 0);
  }
  
  private Item l()
  {
    return this == Blocks.DARK_OAK_DOOR ? Items.DARK_OAK_DOOR : this == Blocks.ACACIA_DOOR ? Items.ACACIA_DOOR : this == Blocks.JUNGLE_DOOR ? Items.JUNGLE_DOOR : this == Blocks.BIRCH_DOOR ? Items.BIRCH_DOOR : this == Blocks.SPRUCE_DOOR ? Items.SPRUCE_DOOR : this == Blocks.IRON_DOOR ? Items.IRON_DOOR : Items.WOODEN_DOOR;
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman)
  {
    BlockPosition blockposition1 = blockposition.down();
    if ((entityhuman.abilities.canInstantlyBuild) && (iblockdata.get(HALF) == EnumDoorHalf.UPPER) && (world.getType(blockposition1).getBlock() == this)) {
      world.setAir(blockposition1);
    }
  }
  
  public IBlockData updateState(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    if (iblockdata.get(HALF) == EnumDoorHalf.LOWER)
    {
      IBlockData iblockdata1 = iblockaccess.getType(blockposition.up());
      if (iblockdata1.getBlock() == this) {
        iblockdata = iblockdata.set(HINGE, (EnumDoorHinge)iblockdata1.get(HINGE)).set(POWERED, (Boolean)iblockdata1.get(POWERED));
      }
    }
    else
    {
      IBlockData iblockdata1 = iblockaccess.getType(blockposition.down());
      if (iblockdata1.getBlock() == this) {
        iblockdata = iblockdata.set(FACING, (EnumDirection)iblockdata1.get(FACING)).set(OPEN, (Boolean)iblockdata1.get(OPEN));
      }
    }
    return iblockdata;
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return (i & 0x8) > 0 ? getBlockData().set(HALF, EnumDoorHalf.UPPER).set(HINGE, (i & 0x1) > 0 ? EnumDoorHinge.RIGHT : EnumDoorHinge.LEFT).set(POWERED, Boolean.valueOf((i & 0x2) > 0)) : getBlockData().set(HALF, EnumDoorHalf.LOWER).set(FACING, EnumDirection.fromType2(i & 0x3).f()).set(OPEN, Boolean.valueOf((i & 0x4) > 0));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    byte b0 = 0;
    int i;
    if (iblockdata.get(HALF) == EnumDoorHalf.UPPER)
    {
      int i = b0 | 0x8;
      if (iblockdata.get(HINGE) == EnumDoorHinge.RIGHT) {
        i |= 0x1;
      }
      if (((Boolean)iblockdata.get(POWERED)).booleanValue()) {
        i |= 0x2;
      }
    }
    else
    {
      i = b0 | ((EnumDirection)iblockdata.get(FACING)).e().b();
      if (((Boolean)iblockdata.get(OPEN)).booleanValue()) {
        i |= 0x4;
      }
    }
    return i;
  }
  
  protected static int b(int i)
  {
    return i & 0x7;
  }
  
  public static boolean f(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    return g(e(iblockaccess, blockposition));
  }
  
  public static EnumDirection h(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    return f(e(iblockaccess, blockposition));
  }
  
  public static EnumDirection f(int i)
  {
    return EnumDirection.fromType2(i & 0x3).f();
  }
  
  protected static boolean g(int i)
  {
    return (i & 0x4) != 0;
  }
  
  protected static boolean i(int i)
  {
    return (i & 0x8) != 0;
  }
  
  protected static boolean j(int i)
  {
    return (i & 0x10) != 0;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { HALF, FACING, OPEN, HINGE, POWERED });
  }
  
  public static enum EnumDoorHinge
    implements INamable
  {
    LEFT,  RIGHT;
    
    public String toString()
    {
      return getName();
    }
    
    public String getName()
    {
      return this == LEFT ? "left" : "right";
    }
  }
  
  public static enum EnumDoorHalf
    implements INamable
  {
    UPPER,  LOWER;
    
    public String toString()
    {
      return getName();
    }
    
    public String getName()
    {
      return this == UPPER ? "upper" : "lower";
    }
  }
}
