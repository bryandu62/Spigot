package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;

public class BlockMinecartDetector
  extends BlockMinecartTrackAbstract
{
  public static final BlockStateEnum<BlockMinecartTrackAbstract.EnumTrackPosition> SHAPE = BlockStateEnum.a("shape", BlockMinecartTrackAbstract.EnumTrackPosition.class, new Predicate()
  {
    public boolean a(BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition)
    {
      return (blockminecarttrackabstract_enumtrackposition != BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST) && (blockminecarttrackabstract_enumtrackposition != BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST) && (blockminecarttrackabstract_enumtrackposition != BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST) && (blockminecarttrackabstract_enumtrackposition != BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST);
    }
    
    public boolean apply(Object object)
    {
      return a((BlockMinecartTrackAbstract.EnumTrackPosition)object);
    }
  });
  public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
  
  public BlockMinecartDetector()
  {
    super(true);
    j(this.blockStateList.getBlockData().set(POWERED, Boolean.valueOf(false)).set(SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH));
    a(true);
  }
  
  public int a(World world)
  {
    return 20;
  }
  
  public boolean isPowerSource()
  {
    return true;
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity)
  {
    if ((!world.isClientSide) && 
      (!((Boolean)iblockdata.get(POWERED)).booleanValue())) {
      e(world, blockposition, iblockdata);
    }
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {}
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if ((!world.isClientSide) && (((Boolean)iblockdata.get(POWERED)).booleanValue())) {
      e(world, blockposition, iblockdata);
    }
  }
  
  public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return ((Boolean)iblockdata.get(POWERED)).booleanValue() ? 15 : 0;
  }
  
  public int b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return enumdirection == EnumDirection.UP ? 15 : !((Boolean)iblockdata.get(POWERED)).booleanValue() ? 0 : 0;
  }
  
  private void e(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    boolean flag = ((Boolean)iblockdata.get(POWERED)).booleanValue();
    boolean flag1 = false;
    List list = a(world, blockposition, EntityMinecartAbstract.class, new Predicate[0]);
    if (!list.isEmpty()) {
      flag1 = true;
    }
    if (flag != flag1)
    {
      Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
      
      BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, flag ? 15 : 0, flag1 ? 15 : 0);
      world.getServer().getPluginManager().callEvent(eventRedstone);
      
      flag1 = eventRedstone.getNewCurrent() > 0;
    }
    if ((flag1) && (!flag))
    {
      world.setTypeAndData(blockposition, iblockdata.set(POWERED, Boolean.valueOf(true)), 3);
      world.applyPhysics(blockposition, this);
      world.applyPhysics(blockposition.down(), this);
      world.b(blockposition, blockposition);
    }
    if ((!flag1) && (flag))
    {
      world.setTypeAndData(blockposition, iblockdata.set(POWERED, Boolean.valueOf(false)), 3);
      world.applyPhysics(blockposition, this);
      world.applyPhysics(blockposition.down(), this);
      world.b(blockposition, blockposition);
    }
    if (flag1) {
      world.a(blockposition, this, a(world));
    }
    world.updateAdjacentComparators(blockposition, this);
  }
  
  public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    super.onPlace(world, blockposition, iblockdata);
    e(world, blockposition, iblockdata);
  }
  
  public IBlockState<BlockMinecartTrackAbstract.EnumTrackPosition> n()
  {
    return SHAPE;
  }
  
  public boolean isComplexRedstone()
  {
    return true;
  }
  
  public int l(World world, BlockPosition blockposition)
  {
    if (((Boolean)world.getType(blockposition).get(POWERED)).booleanValue())
    {
      List list = a(world, blockposition, EntityMinecartCommandBlock.class, new Predicate[0]);
      if (!list.isEmpty()) {
        return ((EntityMinecartCommandBlock)list.get(0)).getCommandBlock().j();
      }
      List list1 = a(world, blockposition, EntityMinecartAbstract.class, new Predicate[] { IEntitySelector.c });
      if (!list1.isEmpty()) {
        return Container.b((IInventory)list1.get(0));
      }
    }
    return 0;
  }
  
  protected <T extends EntityMinecartAbstract> List<T> a(World world, BlockPosition blockposition, Class<T> oclass, Predicate<Entity>... apredicate)
  {
    AxisAlignedBB axisalignedbb = a(blockposition);
    
    return apredicate.length != 1 ? world.a(oclass, axisalignedbb) : world.a(oclass, axisalignedbb, apredicate[0]);
  }
  
  private AxisAlignedBB a(BlockPosition blockposition)
  {
    return new AxisAlignedBB(blockposition.getX() + 0.2F, blockposition.getY(), blockposition.getZ() + 0.2F, blockposition.getX() + 1 - 0.2F, blockposition.getY() + 1 - 0.2F, blockposition.getZ() + 1 - 0.2F);
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.a(i & 0x7)).set(POWERED, Boolean.valueOf((i & 0x8) > 0));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    byte b0 = 0;
    int i = b0 | ((BlockMinecartTrackAbstract.EnumTrackPosition)iblockdata.get(SHAPE)).a();
    if (((Boolean)iblockdata.get(POWERED)).booleanValue()) {
      i |= 0x8;
    }
    return i;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { SHAPE, POWERED });
  }
}
