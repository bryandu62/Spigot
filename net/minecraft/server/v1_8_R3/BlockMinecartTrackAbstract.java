package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public abstract class BlockMinecartTrackAbstract
  extends Block
{
  protected final boolean a;
  
  public static boolean e(World world, BlockPosition blockposition)
  {
    return d(world.getType(blockposition));
  }
  
  public static boolean d(IBlockData iblockdata)
  {
    Block block = iblockdata.getBlock();
    
    return (block == Blocks.RAIL) || (block == Blocks.GOLDEN_RAIL) || (block == Blocks.DETECTOR_RAIL) || (block == Blocks.ACTIVATOR_RAIL);
  }
  
  protected BlockMinecartTrackAbstract(boolean flag)
  {
    super(Material.ORIENTABLE);
    this.a = flag;
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    a(CreativeModeTab.e);
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return null;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public MovingObjectPosition a(World world, BlockPosition blockposition, Vec3D vec3d, Vec3D vec3d1)
  {
    updateShape(world, blockposition);
    return super.a(world, blockposition, vec3d, vec3d1);
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    IBlockData iblockdata = iblockaccess.getType(blockposition);
    EnumTrackPosition blockminecarttrackabstract_enumtrackposition = iblockdata.getBlock() == this ? (EnumTrackPosition)iblockdata.get(n()) : null;
    if ((blockminecarttrackabstract_enumtrackposition != null) && (blockminecarttrackabstract_enumtrackposition.c())) {
      a(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
    } else {
      a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    return World.a(world, blockposition.down());
  }
  
  public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (!world.isClientSide)
    {
      iblockdata = a(world, blockposition, iblockdata, true);
      if (this.a) {
        doPhysics(world, blockposition, iblockdata, this);
      }
    }
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if (!world.isClientSide)
    {
      EnumTrackPosition blockminecarttrackabstract_enumtrackposition = (EnumTrackPosition)iblockdata.get(n());
      boolean flag = false;
      if (!World.a(world, blockposition.down())) {
        flag = true;
      }
      if ((blockminecarttrackabstract_enumtrackposition == EnumTrackPosition.ASCENDING_EAST) && (!World.a(world, blockposition.east()))) {
        flag = true;
      } else if ((blockminecarttrackabstract_enumtrackposition == EnumTrackPosition.ASCENDING_WEST) && (!World.a(world, blockposition.west()))) {
        flag = true;
      } else if ((blockminecarttrackabstract_enumtrackposition == EnumTrackPosition.ASCENDING_NORTH) && (!World.a(world, blockposition.north()))) {
        flag = true;
      } else if ((blockminecarttrackabstract_enumtrackposition == EnumTrackPosition.ASCENDING_SOUTH) && (!World.a(world, blockposition.south()))) {
        flag = true;
      }
      if ((flag) && (!world.isEmpty(blockposition)))
      {
        b(world, blockposition, iblockdata, 0);
        world.setAir(blockposition);
      }
      else
      {
        b(world, blockposition, iblockdata, block);
      }
    }
  }
  
  protected void b(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {}
  
  protected IBlockData a(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag)
  {
    return world.isClientSide ? iblockdata : new MinecartTrackLogic(world, blockposition, iblockdata).a(world.isBlockIndirectlyPowered(blockposition), flag).b();
  }
  
  public int k()
  {
    return 0;
  }
  
  public void remove(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    super.remove(world, blockposition, iblockdata);
    if (((EnumTrackPosition)iblockdata.get(n())).c()) {
      world.applyPhysics(blockposition.up(), this);
    }
    if (this.a)
    {
      world.applyPhysics(blockposition, this);
      world.applyPhysics(blockposition.down(), this);
    }
  }
  
  public abstract IBlockState<EnumTrackPosition> n();
  
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
      try
      {
        a[BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST.ordinal()] = 7;
      }
      catch (NoSuchFieldError localNoSuchFieldError7) {}
      try
      {
        a[BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST.ordinal()] = 8;
      }
      catch (NoSuchFieldError localNoSuchFieldError8) {}
      try
      {
        a[BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST.ordinal()] = 9;
      }
      catch (NoSuchFieldError localNoSuchFieldError9) {}
      try
      {
        a[BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST.ordinal()] = 10;
      }
      catch (NoSuchFieldError localNoSuchFieldError10) {}
    }
  }
  
  public static enum EnumTrackPosition
    implements INamable
  {
    NORTH_SOUTH(0, "north_south"),  EAST_WEST(1, "east_west"),  ASCENDING_EAST(2, "ascending_east"),  ASCENDING_WEST(3, "ascending_west"),  ASCENDING_NORTH(4, "ascending_north"),  ASCENDING_SOUTH(5, "ascending_south"),  SOUTH_EAST(6, "south_east"),  SOUTH_WEST(7, "south_west"),  NORTH_WEST(8, "north_west"),  NORTH_EAST(9, "north_east");
    
    private static final EnumTrackPosition[] k;
    private final int l;
    private final String m;
    
    private EnumTrackPosition(int i, String s)
    {
      this.l = i;
      this.m = s;
    }
    
    public int a()
    {
      return this.l;
    }
    
    public String toString()
    {
      return this.m;
    }
    
    public boolean c()
    {
      return (this == ASCENDING_NORTH) || (this == ASCENDING_EAST) || (this == ASCENDING_SOUTH) || (this == ASCENDING_WEST);
    }
    
    public static EnumTrackPosition a(int i)
    {
      if ((i < 0) || (i >= k.length)) {
        i = 0;
      }
      return k[i];
    }
    
    public String getName()
    {
      return this.m;
    }
    
    static
    {
      k = new EnumTrackPosition[values().length];
      
      EnumTrackPosition[] ablockminecarttrackabstract_enumtrackposition = values();
      int i = ablockminecarttrackabstract_enumtrackposition.length;
      for (int j = 0; j < i; j++)
      {
        EnumTrackPosition blockminecarttrackabstract_enumtrackposition = ablockminecarttrackabstract_enumtrackposition[j];
        
        k[blockminecarttrackabstract_enumtrackposition.a()] = blockminecarttrackabstract_enumtrackposition;
      }
    }
  }
  
  public class MinecartTrackLogic
  {
    private final World b;
    private final BlockPosition c;
    private final BlockMinecartTrackAbstract d;
    private IBlockData e;
    private final boolean f;
    private final List<BlockPosition> g = Lists.newArrayList();
    
    public MinecartTrackLogic(World world, BlockPosition blockposition, IBlockData iblockdata)
    {
      this.b = world;
      this.c = blockposition;
      this.e = iblockdata;
      this.d = ((BlockMinecartTrackAbstract)iblockdata.getBlock());
      BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition = (BlockMinecartTrackAbstract.EnumTrackPosition)iblockdata.get(BlockMinecartTrackAbstract.this.n());
      
      this.f = this.d.a;
      a(blockminecarttrackabstract_enumtrackposition);
    }
    
    private void a(BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition)
    {
      this.g.clear();
      switch (BlockMinecartTrackAbstract.SyntheticClass_1.a[blockminecarttrackabstract_enumtrackposition.ordinal()])
      {
      case 1: 
        this.g.add(this.c.north());
        this.g.add(this.c.south());
        break;
      case 2: 
        this.g.add(this.c.west());
        this.g.add(this.c.east());
        break;
      case 3: 
        this.g.add(this.c.west());
        this.g.add(this.c.east().up());
        break;
      case 4: 
        this.g.add(this.c.west().up());
        this.g.add(this.c.east());
        break;
      case 5: 
        this.g.add(this.c.north().up());
        this.g.add(this.c.south());
        break;
      case 6: 
        this.g.add(this.c.north());
        this.g.add(this.c.south().up());
        break;
      case 7: 
        this.g.add(this.c.east());
        this.g.add(this.c.south());
        break;
      case 8: 
        this.g.add(this.c.west());
        this.g.add(this.c.south());
        break;
      case 9: 
        this.g.add(this.c.west());
        this.g.add(this.c.north());
        break;
      case 10: 
        this.g.add(this.c.east());
        this.g.add(this.c.north());
      }
    }
    
    private void c()
    {
      for (int i = 0; i < this.g.size(); i++)
      {
        MinecartTrackLogic blockminecarttrackabstract_minecarttracklogic = b((BlockPosition)this.g.get(i));
        if ((blockminecarttrackabstract_minecarttracklogic != null) && (blockminecarttrackabstract_minecarttracklogic.a(this))) {
          this.g.set(i, blockminecarttrackabstract_minecarttracklogic.c);
        } else {
          this.g.remove(i--);
        }
      }
    }
    
    private boolean a(BlockPosition blockposition)
    {
      return (BlockMinecartTrackAbstract.e(this.b, blockposition)) || (BlockMinecartTrackAbstract.e(this.b, blockposition.up())) || (BlockMinecartTrackAbstract.e(this.b, blockposition.down()));
    }
    
    private MinecartTrackLogic b(BlockPosition blockposition)
    {
      IBlockData iblockdata = this.b.getType(blockposition);
      if (BlockMinecartTrackAbstract.d(iblockdata))
      {
        BlockMinecartTrackAbstract tmp24_21 = BlockMinecartTrackAbstract.this;tmp24_21.getClass();return new MinecartTrackLogic(tmp24_21, this.b, blockposition, iblockdata);
      }
      BlockPosition blockposition1 = blockposition.up();
      
      iblockdata = this.b.getType(blockposition1);
      if (BlockMinecartTrackAbstract.d(iblockdata))
      {
        BlockMinecartTrackAbstract tmp68_65 = BlockMinecartTrackAbstract.this;tmp68_65.getClass();return new MinecartTrackLogic(tmp68_65, this.b, blockposition1, iblockdata);
      }
      blockposition1 = blockposition.down();
      iblockdata = this.b.getType(blockposition1); BlockMinecartTrackAbstract 
        tmp112_109 = BlockMinecartTrackAbstract.this;tmp112_109.getClass();return BlockMinecartTrackAbstract.d(iblockdata) ? new MinecartTrackLogic(tmp112_109, this.b, blockposition1, iblockdata) : null;
    }
    
    private boolean a(MinecartTrackLogic blockminecarttrackabstract_minecarttracklogic)
    {
      return c(blockminecarttrackabstract_minecarttracklogic.c);
    }
    
    private boolean c(BlockPosition blockposition)
    {
      for (int i = 0; i < this.g.size(); i++)
      {
        BlockPosition blockposition1 = (BlockPosition)this.g.get(i);
        if ((blockposition1.getX() == blockposition.getX()) && (blockposition1.getZ() == blockposition.getZ())) {
          return true;
        }
      }
      return false;
    }
    
    protected int a()
    {
      int i = 0;
      Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
      while (iterator.hasNext())
      {
        EnumDirection enumdirection = (EnumDirection)iterator.next();
        if (a(this.c.shift(enumdirection))) {
          i++;
        }
      }
      return i;
    }
    
    private boolean b(MinecartTrackLogic blockminecarttrackabstract_minecarttracklogic)
    {
      return (a(blockminecarttrackabstract_minecarttracklogic)) || (this.g.size() != 2);
    }
    
    private void c(MinecartTrackLogic blockminecarttrackabstract_minecarttracklogic)
    {
      this.g.add(blockminecarttrackabstract_minecarttracklogic.c);
      BlockPosition blockposition = this.c.north();
      BlockPosition blockposition1 = this.c.south();
      BlockPosition blockposition2 = this.c.west();
      BlockPosition blockposition3 = this.c.east();
      boolean flag = c(blockposition);
      boolean flag1 = c(blockposition1);
      boolean flag2 = c(blockposition2);
      boolean flag3 = c(blockposition3);
      BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition = null;
      if ((flag) || (flag1)) {
        blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
      }
      if ((flag2) || (flag3)) {
        blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST;
      }
      if (!this.f)
      {
        if ((flag1) && (flag3) && (!flag) && (!flag2)) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST;
        }
        if ((flag1) && (flag2) && (!flag) && (!flag3)) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST;
        }
        if ((flag) && (flag2) && (!flag1) && (!flag3)) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST;
        }
        if ((flag) && (flag3) && (!flag1) && (!flag2)) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST;
        }
      }
      if (blockminecarttrackabstract_enumtrackposition == BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH)
      {
        if (BlockMinecartTrackAbstract.e(this.b, blockposition.up())) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH;
        }
        if (BlockMinecartTrackAbstract.e(this.b, blockposition1.up())) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH;
        }
      }
      if (blockminecarttrackabstract_enumtrackposition == BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST)
      {
        if (BlockMinecartTrackAbstract.e(this.b, blockposition3.up())) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST;
        }
        if (BlockMinecartTrackAbstract.e(this.b, blockposition2.up())) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST;
        }
      }
      if (blockminecarttrackabstract_enumtrackposition == null) {
        blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
      }
      this.e = this.e.set(this.d.n(), blockminecarttrackabstract_enumtrackposition);
      this.b.setTypeAndData(this.c, this.e, 3);
    }
    
    private boolean d(BlockPosition blockposition)
    {
      MinecartTrackLogic blockminecarttrackabstract_minecarttracklogic = b(blockposition);
      if (blockminecarttrackabstract_minecarttracklogic == null) {
        return false;
      }
      blockminecarttrackabstract_minecarttracklogic.c();
      return blockminecarttrackabstract_minecarttracklogic.b(this);
    }
    
    public MinecartTrackLogic a(boolean flag, boolean flag1)
    {
      BlockPosition blockposition = this.c.north();
      BlockPosition blockposition1 = this.c.south();
      BlockPosition blockposition2 = this.c.west();
      BlockPosition blockposition3 = this.c.east();
      boolean flag2 = d(blockposition);
      boolean flag3 = d(blockposition1);
      boolean flag4 = d(blockposition2);
      boolean flag5 = d(blockposition3);
      BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition = null;
      if (((flag2) || (flag3)) && (!flag4) && (!flag5)) {
        blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
      }
      if (((flag4) || (flag5)) && (!flag2) && (!flag3)) {
        blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST;
      }
      if (!this.f)
      {
        if ((flag3) && (flag5) && (!flag2) && (!flag4)) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST;
        }
        if ((flag3) && (flag4) && (!flag2) && (!flag5)) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST;
        }
        if ((flag2) && (flag4) && (!flag3) && (!flag5)) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST;
        }
        if ((flag2) && (flag5) && (!flag3) && (!flag4)) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST;
        }
      }
      if (blockminecarttrackabstract_enumtrackposition == null)
      {
        if ((flag2) || (flag3)) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
        }
        if ((flag4) || (flag5)) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST;
        }
        if (!this.f) {
          if (flag)
          {
            if ((flag3) && (flag5)) {
              blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST;
            }
            if ((flag4) && (flag3)) {
              blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST;
            }
            if ((flag5) && (flag2)) {
              blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST;
            }
            if ((flag2) && (flag4)) {
              blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST;
            }
          }
          else
          {
            if ((flag2) && (flag4)) {
              blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST;
            }
            if ((flag5) && (flag2)) {
              blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST;
            }
            if ((flag4) && (flag3)) {
              blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST;
            }
            if ((flag3) && (flag5)) {
              blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST;
            }
          }
        }
      }
      if (blockminecarttrackabstract_enumtrackposition == BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH)
      {
        if (BlockMinecartTrackAbstract.e(this.b, blockposition.up())) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH;
        }
        if (BlockMinecartTrackAbstract.e(this.b, blockposition1.up())) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH;
        }
      }
      if (blockminecarttrackabstract_enumtrackposition == BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST)
      {
        if (BlockMinecartTrackAbstract.e(this.b, blockposition3.up())) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST;
        }
        if (BlockMinecartTrackAbstract.e(this.b, blockposition2.up())) {
          blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST;
        }
      }
      if (blockminecarttrackabstract_enumtrackposition == null) {
        blockminecarttrackabstract_enumtrackposition = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
      }
      a(blockminecarttrackabstract_enumtrackposition);
      this.e = this.e.set(this.d.n(), blockminecarttrackabstract_enumtrackposition);
      if ((flag1) || (this.b.getType(this.c) != this.e))
      {
        this.b.setTypeAndData(this.c, this.e, 3);
        for (int i = 0; i < this.g.size(); i++)
        {
          MinecartTrackLogic blockminecarttrackabstract_minecarttracklogic = b((BlockPosition)this.g.get(i));
          if (blockminecarttrackabstract_minecarttracklogic != null)
          {
            blockminecarttrackabstract_minecarttracklogic.c();
            if (blockminecarttrackabstract_minecarttracklogic.b(this)) {
              blockminecarttrackabstract_minecarttracklogic.c(this);
            }
          }
        }
      }
      return this;
    }
    
    public IBlockData b()
    {
      return this.e;
    }
  }
}
