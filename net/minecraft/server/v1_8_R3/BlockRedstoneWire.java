package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;

public class BlockRedstoneWire
  extends Block
{
  public static final BlockStateEnum<EnumRedstoneWireConnection> NORTH = BlockStateEnum.of("north", EnumRedstoneWireConnection.class);
  public static final BlockStateEnum<EnumRedstoneWireConnection> EAST = BlockStateEnum.of("east", EnumRedstoneWireConnection.class);
  public static final BlockStateEnum<EnumRedstoneWireConnection> SOUTH = BlockStateEnum.of("south", EnumRedstoneWireConnection.class);
  public static final BlockStateEnum<EnumRedstoneWireConnection> WEST = BlockStateEnum.of("west", EnumRedstoneWireConnection.class);
  public static final BlockStateInteger POWER = BlockStateInteger.of("power", 0, 15);
  private boolean Q = true;
  private final Set<BlockPosition> R = Sets.newHashSet();
  
  public BlockRedstoneWire()
  {
    super(Material.ORIENTABLE);
    j(this.blockStateList.getBlockData().set(NORTH, EnumRedstoneWireConnection.NONE).set(EAST, EnumRedstoneWireConnection.NONE).set(SOUTH, EnumRedstoneWireConnection.NONE).set(WEST, EnumRedstoneWireConnection.NONE).set(POWER, Integer.valueOf(0)));
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
  }
  
  public IBlockData updateState(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    iblockdata = iblockdata.set(WEST, c(iblockaccess, blockposition, EnumDirection.WEST));
    iblockdata = iblockdata.set(EAST, c(iblockaccess, blockposition, EnumDirection.EAST));
    iblockdata = iblockdata.set(NORTH, c(iblockaccess, blockposition, EnumDirection.NORTH));
    iblockdata = iblockdata.set(SOUTH, c(iblockaccess, blockposition, EnumDirection.SOUTH));
    return iblockdata;
  }
  
  private EnumRedstoneWireConnection c(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection)
  {
    BlockPosition blockposition1 = blockposition.shift(enumdirection);
    Block block = iblockaccess.getType(blockposition.shift(enumdirection)).getBlock();
    if ((!a(iblockaccess.getType(blockposition1), enumdirection)) && ((block.u()) || (!d(iblockaccess.getType(blockposition1.down())))))
    {
      Block block1 = iblockaccess.getType(blockposition.up()).getBlock();
      
      return (!block1.u()) && (block.u()) && (d(iblockaccess.getType(blockposition1.up()))) ? EnumRedstoneWireConnection.UP : EnumRedstoneWireConnection.NONE;
    }
    return EnumRedstoneWireConnection.SIDE;
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
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    return (World.a(world, blockposition.down())) || (world.getType(blockposition.down()).getBlock() == Blocks.GLOWSTONE);
  }
  
  private IBlockData e(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    iblockdata = a(world, blockposition, blockposition, iblockdata);
    ArrayList arraylist = Lists.newArrayList(this.R);
    
    this.R.clear();
    Iterator iterator = arraylist.iterator();
    while (iterator.hasNext())
    {
      BlockPosition blockposition1 = (BlockPosition)iterator.next();
      
      world.applyPhysics(blockposition1, this);
    }
    return iblockdata;
  }
  
  private IBlockData a(World world, BlockPosition blockposition, BlockPosition blockposition1, IBlockData iblockdata)
  {
    IBlockData iblockdata1 = iblockdata;
    int i = ((Integer)iblockdata.get(POWER)).intValue();
    byte b0 = 0;
    int j = getPower(world, blockposition1, b0);
    
    this.Q = false;
    int k = world.A(blockposition);
    
    this.Q = true;
    if ((k > 0) && (k > j - 1)) {
      j = k;
    }
    int l = 0;
    Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
    while (iterator.hasNext())
    {
      EnumDirection enumdirection = (EnumDirection)iterator.next();
      BlockPosition blockposition2 = blockposition.shift(enumdirection);
      boolean flag = (blockposition2.getX() != blockposition1.getX()) || (blockposition2.getZ() != blockposition1.getZ());
      if (flag) {
        l = getPower(world, blockposition2, l);
      }
      if ((world.getType(blockposition2).getBlock().isOccluding()) && (!world.getType(blockposition.up()).getBlock().isOccluding()))
      {
        if ((flag) && (blockposition.getY() >= blockposition1.getY())) {
          l = getPower(world, blockposition2.up(), l);
        }
      }
      else if ((!world.getType(blockposition2).getBlock().isOccluding()) && (flag) && (blockposition.getY() <= blockposition1.getY())) {
        l = getPower(world, blockposition2.down(), l);
      }
    }
    if (l > j) {
      j = l - 1;
    } else if (j > 0) {
      j--;
    } else {
      j = 0;
    }
    if (k > j - 1) {
      j = k;
    }
    if (i != j)
    {
      BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), i, j);
      world.getServer().getPluginManager().callEvent(event);
      
      j = event.getNewCurrent();
    }
    if (i != j)
    {
      iblockdata = iblockdata.set(POWER, Integer.valueOf(j));
      if (world.getType(blockposition) == iblockdata1) {
        world.setTypeAndData(blockposition, iblockdata, 2);
      }
      this.R.add(blockposition);
      EnumDirection[] aenumdirection = EnumDirection.values();
      int i1 = aenumdirection.length;
      for (int j1 = 0; j1 < i1; j1++)
      {
        EnumDirection enumdirection1 = aenumdirection[j1];
        
        this.R.add(blockposition.shift(enumdirection1));
      }
    }
    return iblockdata;
  }
  
  private void e(World world, BlockPosition blockposition)
  {
    if (world.getType(blockposition).getBlock() == this)
    {
      world.applyPhysics(blockposition, this);
      EnumDirection[] aenumdirection = EnumDirection.values();
      int i = aenumdirection.length;
      for (int j = 0; j < i; j++)
      {
        EnumDirection enumdirection = aenumdirection[j];
        
        world.applyPhysics(blockposition.shift(enumdirection), this);
      }
    }
  }
  
  public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (!world.isClientSide)
    {
      e(world, blockposition, iblockdata);
      Iterator iterator = EnumDirection.EnumDirectionLimit.VERTICAL.iterator();
      while (iterator.hasNext())
      {
        EnumDirection enumdirection = (EnumDirection)iterator.next();
        world.applyPhysics(blockposition.shift(enumdirection), this);
      }
      iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
      while (iterator.hasNext())
      {
        EnumDirection enumdirection = (EnumDirection)iterator.next();
        e(world, blockposition.shift(enumdirection));
      }
      iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
      while (iterator.hasNext())
      {
        EnumDirection enumdirection = (EnumDirection)iterator.next();
        BlockPosition blockposition1 = blockposition.shift(enumdirection);
        if (world.getType(blockposition1).getBlock().isOccluding()) {
          e(world, blockposition1.up());
        } else {
          e(world, blockposition1.down());
        }
      }
    }
  }
  
  public void remove(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    super.remove(world, blockposition, iblockdata);
    if (!world.isClientSide)
    {
      EnumDirection[] aenumdirection = EnumDirection.values();
      int i = aenumdirection.length;
      for (int j = 0; j < i; j++)
      {
        EnumDirection enumdirection = aenumdirection[j];
        
        world.applyPhysics(blockposition.shift(enumdirection), this);
      }
      e(world, blockposition, iblockdata);
      Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
      while (iterator.hasNext())
      {
        EnumDirection enumdirection1 = (EnumDirection)iterator.next();
        e(world, blockposition.shift(enumdirection1));
      }
      iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
      while (iterator.hasNext())
      {
        EnumDirection enumdirection1 = (EnumDirection)iterator.next();
        BlockPosition blockposition1 = blockposition.shift(enumdirection1);
        if (world.getType(blockposition1).getBlock().isOccluding()) {
          e(world, blockposition1.up());
        } else {
          e(world, blockposition1.down());
        }
      }
    }
  }
  
  public int getPower(World world, BlockPosition blockposition, int i)
  {
    if (world.getType(blockposition).getBlock() != this) {
      return i;
    }
    int j = ((Integer)world.getType(blockposition).get(POWER)).intValue();
    
    return j > i ? j : i;
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if (!world.isClientSide) {
      if (canPlace(world, blockposition))
      {
        e(world, blockposition, iblockdata);
      }
      else
      {
        b(world, blockposition, iblockdata, 0);
        world.setAir(blockposition);
      }
    }
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return Items.REDSTONE;
  }
  
  public int b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return !this.Q ? 0 : a(iblockaccess, blockposition, iblockdata, enumdirection);
  }
  
  public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    if (!this.Q) {
      return 0;
    }
    int i = ((Integer)iblockdata.get(POWER)).intValue();
    if (i == 0) {
      return 0;
    }
    if (enumdirection == EnumDirection.UP) {
      return i;
    }
    EnumSet enumset = EnumSet.noneOf(EnumDirection.class);
    Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
    while (iterator.hasNext())
    {
      EnumDirection enumdirection1 = (EnumDirection)iterator.next();
      if (d(iblockaccess, blockposition, enumdirection1)) {
        enumset.add(enumdirection1);
      }
    }
    if ((enumdirection.k().c()) && (enumset.isEmpty())) {
      return i;
    }
    if ((enumset.contains(enumdirection)) && (!enumset.contains(enumdirection.f())) && (!enumset.contains(enumdirection.e()))) {
      return i;
    }
    return 0;
  }
  
  private boolean d(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection)
  {
    BlockPosition blockposition1 = blockposition.shift(enumdirection);
    IBlockData iblockdata = iblockaccess.getType(blockposition1);
    Block block = iblockdata.getBlock();
    boolean flag = block.isOccluding();
    boolean flag1 = iblockaccess.getType(blockposition.up()).getBlock().isOccluding();
    
    return (!flag1) && (flag) && (e(iblockaccess, blockposition1.up()));
  }
  
  protected static boolean e(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    return d(iblockaccess.getType(blockposition));
  }
  
  protected static boolean d(IBlockData iblockdata)
  {
    return a(iblockdata, null);
  }
  
  protected static boolean a(IBlockData iblockdata, EnumDirection enumdirection)
  {
    Block block = iblockdata.getBlock();
    if (block == Blocks.REDSTONE_WIRE) {
      return true;
    }
    if (Blocks.UNPOWERED_REPEATER.e(block))
    {
      EnumDirection enumdirection1 = (EnumDirection)iblockdata.get(BlockRepeater.FACING);
      
      return (enumdirection1 == enumdirection) || (enumdirection1.opposite() == enumdirection);
    }
    return (block.isPowerSource()) && (enumdirection != null);
  }
  
  public boolean isPowerSource()
  {
    return this.Q;
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(POWER, Integer.valueOf(i));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    return ((Integer)iblockdata.get(POWER)).intValue();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { NORTH, EAST, SOUTH, WEST, POWER });
  }
  
  static enum EnumRedstoneWireConnection
    implements INamable
  {
    UP("up"),  SIDE("side"),  NONE("none");
    
    private final String d;
    
    private EnumRedstoneWireConnection(String s)
    {
      this.d = s;
    }
    
    public String toString()
    {
      return getName();
    }
    
    public String getName()
    {
      return this.d;
    }
  }
}
