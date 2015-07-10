package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;

public class BlockVine
  extends Block
{
  public static final BlockStateBoolean UP = BlockStateBoolean.of("up");
  public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
  public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
  public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
  public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");
  public static final BlockStateBoolean[] Q = { UP, NORTH, SOUTH, WEST, EAST };
  
  public BlockVine()
  {
    super(Material.REPLACEABLE_PLANT);
    j(this.blockStateList.getBlockData().set(UP, Boolean.valueOf(false)).set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)));
    a(true);
    a(CreativeModeTab.c);
  }
  
  public IBlockData updateState(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    return iblockdata.set(UP, Boolean.valueOf(iblockaccess.getType(blockposition.up()).getBlock().u()));
  }
  
  public void j()
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean a(World world, BlockPosition blockposition)
  {
    return true;
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    float f1 = 1.0F;
    float f2 = 1.0F;
    float f3 = 1.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    boolean flag = false;
    if (((Boolean)iblockaccess.getType(blockposition).get(WEST)).booleanValue())
    {
      f4 = Math.max(f4, 0.0625F);
      f1 = 0.0F;
      f2 = 0.0F;
      f5 = 1.0F;
      f3 = 0.0F;
      f6 = 1.0F;
      flag = true;
    }
    if (((Boolean)iblockaccess.getType(blockposition).get(EAST)).booleanValue())
    {
      f1 = Math.min(f1, 0.9375F);
      f4 = 1.0F;
      f2 = 0.0F;
      f5 = 1.0F;
      f3 = 0.0F;
      f6 = 1.0F;
      flag = true;
    }
    if (((Boolean)iblockaccess.getType(blockposition).get(NORTH)).booleanValue())
    {
      f6 = Math.max(f6, 0.0625F);
      f3 = 0.0F;
      f1 = 0.0F;
      f4 = 1.0F;
      f2 = 0.0F;
      f5 = 1.0F;
      flag = true;
    }
    if (((Boolean)iblockaccess.getType(blockposition).get(SOUTH)).booleanValue())
    {
      f3 = Math.min(f3, 0.9375F);
      f6 = 1.0F;
      f1 = 0.0F;
      f4 = 1.0F;
      f2 = 0.0F;
      f5 = 1.0F;
      flag = true;
    }
    if ((!flag) && (c(iblockaccess.getType(blockposition.up()).getBlock())))
    {
      f2 = Math.min(f2, 0.9375F);
      f5 = 1.0F;
      f1 = 0.0F;
      f4 = 1.0F;
      f3 = 0.0F;
      f6 = 1.0F;
    }
    a(f1, f2, f3, f4, f5, f6);
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return null;
  }
  
  public boolean canPlace(World world, BlockPosition blockposition, EnumDirection enumdirection)
  {
    switch (SyntheticClass_1.a[enumdirection.ordinal()])
    {
    case 1: 
      return c(world.getType(blockposition.up()).getBlock());
    case 2: 
    case 3: 
    case 4: 
    case 5: 
      return c(world.getType(blockposition.shift(enumdirection.opposite())).getBlock());
    }
    return false;
  }
  
  private boolean c(Block block)
  {
    return (block.d()) && (block.material.isSolid());
  }
  
  private boolean e(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    IBlockData iblockdata1 = iblockdata;
    Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
    while (iterator.hasNext())
    {
      EnumDirection enumdirection = (EnumDirection)iterator.next();
      BlockStateBoolean blockstateboolean = a(enumdirection);
      if ((((Boolean)iblockdata.get(blockstateboolean)).booleanValue()) && (!c(world.getType(blockposition.shift(enumdirection)).getBlock())))
      {
        IBlockData iblockdata2 = world.getType(blockposition.up());
        if ((iblockdata2.getBlock() != this) || (!((Boolean)iblockdata2.get(blockstateboolean)).booleanValue())) {
          iblockdata = iblockdata.set(blockstateboolean, Boolean.valueOf(false));
        }
      }
    }
    if (d(iblockdata) == 0) {
      return false;
    }
    if (iblockdata1 != iblockdata) {
      world.setTypeAndData(blockposition, iblockdata, 2);
    }
    return true;
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if ((!world.isClientSide) && (!e(world, blockposition, iblockdata)))
    {
      b(world, blockposition, iblockdata, 0);
      world.setAir(blockposition);
    }
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if ((!world.isClientSide) && 
      (world.random.nextInt(4) == 0))
    {
      byte b0 = 4;
      int i = 5;
      boolean flag = false;
      for (int j = -b0; j <= b0; j++) {
        for (int k = -b0; k <= b0; k++) {
          for (int l = -1; l <= 1; l++) {
            if (world.getType(blockposition.a(j, l, k)).getBlock() == this)
            {
              i--;
              if (i <= 0)
              {
                flag = true;
                break;
              }
            }
          }
        }
      }
      EnumDirection enumdirection = EnumDirection.a(random);
      BlockPosition blockposition1 = blockposition.up();
      if ((enumdirection == EnumDirection.UP) && (blockposition.getY() < 255) && (world.isEmpty(blockposition1)))
      {
        if (!flag)
        {
          IBlockData iblockdata1 = iblockdata;
          Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
          while (iterator.hasNext())
          {
            EnumDirection enumdirection1 = (EnumDirection)iterator.next();
            if ((random.nextBoolean()) || (!c(world.getType(blockposition1.shift(enumdirection1)).getBlock()))) {
              iblockdata1 = iblockdata1.set(a(enumdirection1), Boolean.valueOf(false));
            }
          }
          if ((((Boolean)iblockdata1.get(NORTH)).booleanValue()) || (((Boolean)iblockdata1.get(EAST)).booleanValue()) || (((Boolean)iblockdata1.get(SOUTH)).booleanValue()) || (((Boolean)iblockdata1.get(WEST)).booleanValue()))
          {
            BlockPosition target = blockposition1;
            org.bukkit.block.Block source = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            org.bukkit.block.Block block = world.getWorld().getBlockAt(target.getX(), target.getY(), target.getZ());
            CraftEventFactory.handleBlockSpreadEvent(block, source, this, toLegacyData(iblockdata1));
          }
        }
      }
      else if ((enumdirection.k().c()) && (!((Boolean)iblockdata.get(a(enumdirection))).booleanValue()))
      {
        if (!flag)
        {
          BlockPosition blockposition2 = blockposition.shift(enumdirection);
          Block block = world.getType(blockposition2).getBlock();
          if (block.material == Material.AIR)
          {
            EnumDirection enumdirection1 = enumdirection.e();
            EnumDirection enumdirection2 = enumdirection.f();
            boolean flag1 = ((Boolean)iblockdata.get(a(enumdirection1))).booleanValue();
            boolean flag2 = ((Boolean)iblockdata.get(a(enumdirection2))).booleanValue();
            BlockPosition blockposition3 = blockposition2.shift(enumdirection1);
            BlockPosition blockposition4 = blockposition2.shift(enumdirection2);
            
            org.bukkit.block.Block source = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(blockposition2.getX(), blockposition2.getY(), blockposition2.getZ());
            if ((flag1) && (c(world.getType(blockposition3).getBlock())))
            {
              CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, toLegacyData(getBlockData().set(a(enumdirection1), Boolean.valueOf(true))));
            }
            else if ((flag2) && (c(world.getType(blockposition4).getBlock())))
            {
              CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, toLegacyData(getBlockData().set(a(enumdirection2), Boolean.valueOf(true))));
            }
            else if ((flag1) && (world.isEmpty(blockposition3)) && (c(world.getType(blockposition.shift(enumdirection1)).getBlock())))
            {
              bukkitBlock = world.getWorld().getBlockAt(blockposition3.getX(), blockposition3.getY(), blockposition3.getZ());
              CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, toLegacyData(getBlockData().set(a(enumdirection.opposite()), Boolean.valueOf(true))));
            }
            else if ((flag2) && (world.isEmpty(blockposition4)) && (c(world.getType(blockposition.shift(enumdirection2)).getBlock())))
            {
              bukkitBlock = world.getWorld().getBlockAt(blockposition4.getX(), blockposition4.getY(), blockposition4.getZ());
              CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, toLegacyData(getBlockData().set(a(enumdirection.opposite()), Boolean.valueOf(true))));
            }
            else if (c(world.getType(blockposition2.up()).getBlock()))
            {
              CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, toLegacyData(getBlockData()));
            }
          }
          else if ((block.material.k()) && (block.d()))
          {
            world.setTypeAndData(blockposition, iblockdata.set(a(enumdirection), Boolean.valueOf(true)), 2);
          }
        }
      }
      else if (blockposition.getY() > 1)
      {
        BlockPosition blockposition2 = blockposition.down();
        IBlockData iblockdata2 = world.getType(blockposition2);
        Block block1 = iblockdata2.getBlock();
        if (block1.material == Material.AIR)
        {
          IBlockData iblockdata3 = iblockdata;
          Iterator iterator1 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
          while (iterator1.hasNext())
          {
            EnumDirection enumdirection3 = (EnumDirection)iterator1.next();
            if (random.nextBoolean()) {
              iblockdata3 = iblockdata3.set(a(enumdirection3), Boolean.valueOf(false));
            }
          }
          if ((((Boolean)iblockdata3.get(NORTH)).booleanValue()) || (((Boolean)iblockdata3.get(EAST)).booleanValue()) || (((Boolean)iblockdata3.get(SOUTH)).booleanValue()) || (((Boolean)iblockdata3.get(WEST)).booleanValue()))
          {
            org.bukkit.block.Block source = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(blockposition2.getX(), blockposition2.getY(), blockposition2.getZ());
            CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, toLegacyData(iblockdata3));
          }
        }
        else if (block1 == this)
        {
          IBlockData iblockdata3 = iblockdata2;
          Iterator iterator1 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
          while (iterator1.hasNext())
          {
            EnumDirection enumdirection3 = (EnumDirection)iterator1.next();
            BlockStateBoolean blockstateboolean = a(enumdirection3);
            if ((random.nextBoolean()) && (((Boolean)iblockdata.get(blockstateboolean)).booleanValue())) {
              iblockdata3 = iblockdata3.set(blockstateboolean, Boolean.valueOf(true));
            }
          }
          if ((((Boolean)iblockdata3.get(NORTH)).booleanValue()) || (((Boolean)iblockdata3.get(EAST)).booleanValue()) || (((Boolean)iblockdata3.get(SOUTH)).booleanValue()) || (((Boolean)iblockdata3.get(WEST)).booleanValue())) {
            world.setTypeAndData(blockposition2, iblockdata3, 2);
          }
        }
      }
    }
  }
  
  public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving)
  {
    IBlockData iblockdata = getBlockData().set(UP, Boolean.valueOf(false)).set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false));
    
    return enumdirection.k().c() ? iblockdata.set(a(enumdirection.opposite()), Boolean.valueOf(true)) : iblockdata;
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return null;
  }
  
  public int a(Random random)
  {
    return 0;
  }
  
  public void a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, TileEntity tileentity)
  {
    if ((!world.isClientSide) && (entityhuman.bZ() != null) && (entityhuman.bZ().getItem() == Items.SHEARS))
    {
      entityhuman.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
      a(world, blockposition, new ItemStack(Blocks.VINE, 1, 0));
    }
    else
    {
      super.a(world, entityhuman, blockposition, iblockdata, tileentity);
    }
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(SOUTH, Boolean.valueOf((i & 0x1) > 0)).set(WEST, Boolean.valueOf((i & 0x2) > 0)).set(NORTH, Boolean.valueOf((i & 0x4) > 0)).set(EAST, Boolean.valueOf((i & 0x8) > 0));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    int i = 0;
    if (((Boolean)iblockdata.get(SOUTH)).booleanValue()) {
      i |= 0x1;
    }
    if (((Boolean)iblockdata.get(WEST)).booleanValue()) {
      i |= 0x2;
    }
    if (((Boolean)iblockdata.get(NORTH)).booleanValue()) {
      i |= 0x4;
    }
    if (((Boolean)iblockdata.get(EAST)).booleanValue()) {
      i |= 0x8;
    }
    return i;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { UP, NORTH, EAST, SOUTH, WEST });
  }
  
  public static BlockStateBoolean a(EnumDirection enumdirection)
  {
    switch (SyntheticClass_1.a[enumdirection.ordinal()])
    {
    case 1: 
      return UP;
    case 2: 
      return NORTH;
    case 3: 
      return SOUTH;
    case 4: 
      return EAST;
    case 5: 
      return WEST;
    }
    throw new IllegalArgumentException(enumdirection + " is an invalid choice");
  }
  
  public static int d(IBlockData iblockdata)
  {
    int i = 0;
    BlockStateBoolean[] ablockstateboolean = Q;
    int j = ablockstateboolean.length;
    for (int k = 0; k < j; k++)
    {
      BlockStateBoolean blockstateboolean = ablockstateboolean[k];
      if (((Boolean)iblockdata.get(blockstateboolean)).booleanValue()) {
        i++;
      }
    }
    return i;
  }
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[EnumDirection.values().length];
    
    static
    {
      try
      {
        a[EnumDirection.UP.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[EnumDirection.NORTH.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        a[EnumDirection.SOUTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        a[EnumDirection.EAST.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        a[EnumDirection.WEST.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
    }
  }
}
