package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.block.BlockRedstoneEvent;

public abstract class BlockDiodeAbstract
  extends BlockDirectional
{
  protected final boolean N;
  
  protected BlockDiodeAbstract(boolean flag)
  {
    super(Material.ORIENTABLE);
    this.N = flag;
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    return World.a(world, blockposition.down()) ? super.canPlace(world, blockposition) : false;
  }
  
  public boolean e(World world, BlockPosition blockposition)
  {
    return World.a(world, blockposition.down());
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {}
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if (!b(world, blockposition, iblockdata))
    {
      boolean flag = e(world, blockposition, iblockdata);
      if ((this.N) && (!flag))
      {
        if (CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 15, 0).getNewCurrent() != 0) {
          return;
        }
        world.setTypeAndData(blockposition, k(iblockdata), 2);
      }
      else if (!this.N)
      {
        if (CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 0, 15).getNewCurrent() != 15) {
          return;
        }
        world.setTypeAndData(blockposition, e(iblockdata), 2);
        if (!flag) {
          world.a(blockposition, e(iblockdata).getBlock(), m(iblockdata), -1);
        }
      }
    }
  }
  
  protected boolean l(IBlockData iblockdata)
  {
    return this.N;
  }
  
  public int b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return a(iblockaccess, blockposition, iblockdata, enumdirection);
  }
  
  public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return iblockdata.get(FACING) == enumdirection ? a(iblockaccess, blockposition, iblockdata) : !l(iblockdata) ? 0 : 0;
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if (e(world, blockposition))
    {
      g(world, blockposition, iblockdata);
    }
    else
    {
      b(world, blockposition, iblockdata, 0);
      world.setAir(blockposition);
      EnumDirection[] aenumdirection = EnumDirection.values();
      int i = aenumdirection.length;
      for (int j = 0; j < i; j++)
      {
        EnumDirection enumdirection = aenumdirection[j];
        
        world.applyPhysics(blockposition.shift(enumdirection), this);
      }
    }
  }
  
  protected void g(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (!b(world, blockposition, iblockdata))
    {
      boolean flag = e(world, blockposition, iblockdata);
      if (((this.N) && (!flag)) || ((!this.N) && (flag) && (!world.a(blockposition, this))))
      {
        byte b0 = -1;
        if (i(world, blockposition, iblockdata)) {
          b0 = -3;
        } else if (this.N) {
          b0 = -2;
        }
        world.a(blockposition, this, d(iblockdata), b0);
      }
    }
  }
  
  public boolean b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata)
  {
    return false;
  }
  
  protected boolean e(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return f(world, blockposition, iblockdata) > 0;
  }
  
  protected int f(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    EnumDirection enumdirection = (EnumDirection)iblockdata.get(FACING);
    BlockPosition blockposition1 = blockposition.shift(enumdirection);
    int i = world.getBlockFacePower(blockposition1, enumdirection);
    if (i >= 15) {
      return i;
    }
    IBlockData iblockdata1 = world.getType(blockposition1);
    
    return Math.max(i, iblockdata1.getBlock() == Blocks.REDSTONE_WIRE ? ((Integer)iblockdata1.get(BlockRedstoneWire.POWER)).intValue() : 0);
  }
  
  protected int c(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata)
  {
    EnumDirection enumdirection = (EnumDirection)iblockdata.get(FACING);
    EnumDirection enumdirection1 = enumdirection.e();
    EnumDirection enumdirection2 = enumdirection.f();
    
    return Math.max(c(iblockaccess, blockposition.shift(enumdirection1), enumdirection1), c(iblockaccess, blockposition.shift(enumdirection2), enumdirection2));
  }
  
  protected int c(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection)
  {
    IBlockData iblockdata = iblockaccess.getType(blockposition);
    Block block = iblockdata.getBlock();
    
    return c(block) ? iblockaccess.getBlockPower(blockposition, enumdirection) : block == Blocks.REDSTONE_WIRE ? ((Integer)iblockdata.get(BlockRedstoneWire.POWER)).intValue() : 0;
  }
  
  public boolean isPowerSource()
  {
    return true;
  }
  
  public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving)
  {
    return getBlockData().set(FACING, entityliving.getDirection().opposite());
  }
  
  public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack)
  {
    if (e(world, blockposition, iblockdata)) {
      world.a(blockposition, this, 1);
    }
  }
  
  public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    h(world, blockposition, iblockdata);
  }
  
  protected void h(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    EnumDirection enumdirection = (EnumDirection)iblockdata.get(FACING);
    BlockPosition blockposition1 = blockposition.shift(enumdirection.opposite());
    
    world.d(blockposition1, this);
    world.a(blockposition1, this, enumdirection);
  }
  
  public void postBreak(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (this.N)
    {
      EnumDirection[] aenumdirection = EnumDirection.values();
      int i = aenumdirection.length;
      for (int j = 0; j < i; j++)
      {
        EnumDirection enumdirection = aenumdirection[j];
        
        world.applyPhysics(blockposition.shift(enumdirection), this);
      }
    }
    super.postBreak(world, blockposition, iblockdata);
  }
  
  public boolean c()
  {
    return false;
  }
  
  protected boolean c(Block block)
  {
    return block.isPowerSource();
  }
  
  protected int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata)
  {
    return 15;
  }
  
  public static boolean d(Block block)
  {
    return (Blocks.UNPOWERED_REPEATER.e(block)) || (Blocks.UNPOWERED_COMPARATOR.e(block));
  }
  
  public boolean e(Block block)
  {
    return (block == e(getBlockData()).getBlock()) || (block == k(getBlockData()).getBlock());
  }
  
  public boolean i(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    EnumDirection enumdirection = ((EnumDirection)iblockdata.get(FACING)).opposite();
    BlockPosition blockposition1 = blockposition.shift(enumdirection);
    
    return world.getType(blockposition1).get(FACING) != enumdirection;
  }
  
  protected int m(IBlockData iblockdata)
  {
    return d(iblockdata);
  }
  
  protected abstract int d(IBlockData paramIBlockData);
  
  protected abstract IBlockData e(IBlockData paramIBlockData);
  
  protected abstract IBlockData k(IBlockData paramIBlockData);
  
  public boolean b(Block block)
  {
    return e(block);
  }
}
