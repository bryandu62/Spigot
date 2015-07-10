package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockPoweredRail
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
  
  protected BlockPoweredRail()
  {
    super(true);
    j(this.blockStateList.getBlockData().set(SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH).set(POWERED, Boolean.valueOf(false)));
  }
  
  protected boolean a(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag, int i)
  {
    if (i >= 8) {
      return false;
    }
    int j = blockposition.getX();
    int k = blockposition.getY();
    int l = blockposition.getZ();
    boolean flag1 = true;
    BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition = (BlockMinecartTrackAbstract.EnumTrackPosition)iblockdata.get(SHAPE);
    switch (SyntheticClass_1.a[blockminecarttrackabstract_enumtrackposition.ordinal()])
    {
    case 1: 
      if (flag) {
        l++;
      } else {
        l--;
      }
      break;
    case 2: 
      if (flag) {
        j--;
      } else {
        j++;
      }
      break;
    case 3: 
      if (flag)
      {
        j--;
      }
      else
      {
        j++;
        k++;
        flag1 = false;
      }
      blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST;
      break;
    case 4: 
      if (flag)
      {
        j--;
        k++;
        flag1 = false;
      }
      else
      {
        j++;
      }
      blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST;
      break;
    case 5: 
      if (flag)
      {
        l++;
      }
      else
      {
        l--;
        k++;
        flag1 = false;
      }
      blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
      break;
    case 6: 
      if (flag)
      {
        l++;
        k++;
        flag1 = false;
      }
      else
      {
        l--;
      }
      blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
    }
    return a(world, new BlockPosition(j, k, l), flag, i, blockminecarttrackabstract_enumtrackposition);
  }
  
  protected boolean a(World world, BlockPosition blockposition, boolean flag, int i, BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition)
  {
    IBlockData iblockdata = world.getType(blockposition);
    if (iblockdata.getBlock() != this) {
      return false;
    }
    BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition1 = (BlockMinecartTrackAbstract.EnumTrackPosition)iblockdata.get(SHAPE);
    
    return (blockminecarttrackabstract_enumtrackposition != BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST) || ((blockminecarttrackabstract_enumtrackposition1 != BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH) && (blockminecarttrackabstract_enumtrackposition1 != BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH) && (blockminecarttrackabstract_enumtrackposition1 != BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH));
  }
  
  protected void b(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    boolean flag = ((Boolean)iblockdata.get(POWERED)).booleanValue();
    boolean flag1 = (world.isBlockIndirectlyPowered(blockposition)) || (a(world, blockposition, iblockdata, true, 0)) || (a(world, blockposition, iblockdata, false, 0));
    if (flag1 != flag)
    {
      int power = ((Boolean)iblockdata.get(POWERED)).booleanValue() ? 15 : 0;
      int newPower = CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), power, 15 - power).getNewCurrent();
      if (newPower == power) {
        return;
      }
      world.setTypeAndData(blockposition, iblockdata.set(POWERED, Boolean.valueOf(flag1)), 3);
      world.applyPhysics(blockposition.down(), this);
      if (((BlockMinecartTrackAbstract.EnumTrackPosition)iblockdata.get(SHAPE)).c()) {
        world.applyPhysics(blockposition.up(), this);
      }
    }
  }
  
  public IBlockState<BlockMinecartTrackAbstract.EnumTrackPosition> n()
  {
    return SHAPE;
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
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[BlockMinecartTrackAbstract.EnumTrackPosition.values().length];
    
    static
    {
      try
      {
        a[BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
    }
  }
}
