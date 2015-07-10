package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockDaylightDetector
  extends BlockContainer
{
  public static final BlockStateInteger POWER = BlockStateInteger.of("power", 0, 15);
  private final boolean b;
  
  public BlockDaylightDetector(boolean flag)
  {
    super(Material.WOOD);
    this.b = flag;
    j(this.blockStateList.getBlockData().set(POWER, Integer.valueOf(0)));
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
    a(CreativeModeTab.d);
    c(0.2F);
    a(f);
    c("daylightDetector");
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
  }
  
  public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return ((Integer)iblockdata.get(POWER)).intValue();
  }
  
  public void f(World world, BlockPosition blockposition)
  {
    if (!world.worldProvider.o())
    {
      IBlockData iblockdata = world.getType(blockposition);
      int i = world.b(EnumSkyBlock.SKY, blockposition) - world.ab();
      float f = world.d(1.0F);
      float f1 = f < 3.1415927F ? 0.0F : 6.2831855F;
      
      f += (f1 - f) * 0.2F;
      i = Math.round(i * MathHelper.cos(f));
      i = MathHelper.clamp(i, 0, 15);
      if (this.b) {
        i = 15 - i;
      }
      if (((Integer)iblockdata.get(POWER)).intValue() != i)
      {
        i = CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), ((Integer)iblockdata.get(POWER)).intValue(), i).getNewCurrent();
        world.setTypeAndData(blockposition, iblockdata.set(POWER, Integer.valueOf(i)), 3);
      }
    }
  }
  
  public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2)
  {
    if (entityhuman.cn())
    {
      if (world.isClientSide) {
        return true;
      }
      if (this.b)
      {
        world.setTypeAndData(blockposition, Blocks.DAYLIGHT_DETECTOR.getBlockData().set(POWER, (Integer)iblockdata.get(POWER)), 4);
        Blocks.DAYLIGHT_DETECTOR.f(world, blockposition);
      }
      else
      {
        world.setTypeAndData(blockposition, Blocks.DAYLIGHT_DETECTOR_INVERTED.getBlockData().set(POWER, (Integer)iblockdata.get(POWER)), 4);
        Blocks.DAYLIGHT_DETECTOR_INVERTED.f(world, blockposition);
      }
      return true;
    }
    return super.interact(world, blockposition, iblockdata, entityhuman, enumdirection, f, f1, f2);
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return Item.getItemOf(Blocks.DAYLIGHT_DETECTOR);
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public int b()
  {
    return 3;
  }
  
  public boolean isPowerSource()
  {
    return true;
  }
  
  public TileEntity a(World world, int i)
  {
    return new TileEntityLightDetector();
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
    return new BlockStateList(this, new IBlockState[] { POWER });
  }
}
