package net.minecraft.server.v1_8_R3;

import com.google.common.collect.ImmutableList;
import java.util.AbstractList;
import java.util.List;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.plugin.PluginManager;

public class BlockPiston
  extends Block
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing");
  public static final BlockStateBoolean EXTENDED = BlockStateBoolean.of("extended");
  private final boolean N;
  
  public BlockPiston(boolean flag)
  {
    super(Material.PISTON);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(EXTENDED, Boolean.valueOf(false)));
    this.N = flag;
    a(i);
    c(0.5F);
    a(CreativeModeTab.d);
  }
  
  public boolean c()
  {
    return false;
  }
  
  public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack)
  {
    world.setTypeAndData(blockposition, iblockdata.set(FACING, a(world, blockposition, entityliving)), 2);
    if (!world.isClientSide) {
      e(world, blockposition, iblockdata);
    }
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if (!world.isClientSide) {
      e(world, blockposition, iblockdata);
    }
  }
  
  public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if ((!world.isClientSide) && (world.getTileEntity(blockposition) == null)) {
      e(world, blockposition, iblockdata);
    }
  }
  
  public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving)
  {
    return getBlockData().set(FACING, a(world, blockposition, entityliving)).set(EXTENDED, Boolean.valueOf(false));
  }
  
  private void e(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    EnumDirection enumdirection = (EnumDirection)iblockdata.get(FACING);
    boolean flag = a(world, blockposition, enumdirection);
    if ((flag) && (!((Boolean)iblockdata.get(EXTENDED)).booleanValue()))
    {
      if (new PistonExtendsChecker(world, blockposition, enumdirection, true).a()) {
        world.playBlockAction(blockposition, this, 0, enumdirection.a());
      }
    }
    else if ((!flag) && (((Boolean)iblockdata.get(EXTENDED)).booleanValue()))
    {
      if (!this.N)
      {
        org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
        BlockPistonRetractEvent event = new BlockPistonRetractEvent(block, ImmutableList.of(), CraftBlock.notchToBlockFace(enumdirection));
        world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
          return;
        }
      }
      world.setTypeAndData(blockposition, iblockdata.set(EXTENDED, Boolean.valueOf(false)), 2);
      world.playBlockAction(blockposition, this, 1, enumdirection.a());
    }
  }
  
  private boolean a(World world, BlockPosition blockposition, EnumDirection enumdirection)
  {
    EnumDirection[] aenumdirection = EnumDirection.values();
    int i = aenumdirection.length;
    for (int j = 0; j < i; j++)
    {
      EnumDirection enumdirection1 = aenumdirection[j];
      if ((enumdirection1 != enumdirection) && (world.isBlockFacePowered(blockposition.shift(enumdirection1), enumdirection1))) {
        return true;
      }
    }
    if (world.isBlockFacePowered(blockposition, EnumDirection.DOWN)) {
      return true;
    }
    BlockPosition blockposition1 = blockposition.up();
    EnumDirection[] aenumdirection1 = EnumDirection.values();
    
    j = aenumdirection1.length;
    for (int k = 0; k < j; k++)
    {
      EnumDirection enumdirection2 = aenumdirection1[k];
      if ((enumdirection2 != EnumDirection.DOWN) && (world.isBlockFacePowered(blockposition1.shift(enumdirection2), enumdirection2))) {
        return true;
      }
    }
    return false;
  }
  
  public boolean a(World world, BlockPosition blockposition, IBlockData iblockdata, int i, int j)
  {
    EnumDirection enumdirection = (EnumDirection)iblockdata.get(FACING);
    if (!world.isClientSide)
    {
      boolean flag = a(world, blockposition, enumdirection);
      if ((flag) && (i == 1))
      {
        world.setTypeAndData(blockposition, iblockdata.set(EXTENDED, Boolean.valueOf(true)), 2);
        return false;
      }
      if ((!flag) && (i == 0)) {
        return false;
      }
    }
    if (i == 0)
    {
      if (!a(world, blockposition, enumdirection, true)) {
        return false;
      }
      world.setTypeAndData(blockposition, iblockdata.set(EXTENDED, Boolean.valueOf(true)), 2);
      world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "tile.piston.out", 0.5F, world.random.nextFloat() * 0.25F + 0.6F);
    }
    else if (i == 1)
    {
      TileEntity tileentity = world.getTileEntity(blockposition.shift(enumdirection));
      if ((tileentity instanceof TileEntityPiston)) {
        ((TileEntityPiston)tileentity).h();
      }
      world.setTypeAndData(blockposition, Blocks.PISTON_EXTENSION.getBlockData().set(BlockPistonMoving.FACING, enumdirection).set(BlockPistonMoving.TYPE, this.N ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
      world.setTileEntity(blockposition, BlockPistonMoving.a(fromLegacyData(j), enumdirection, false, true));
      if (this.N)
      {
        BlockPosition blockposition1 = blockposition.a(enumdirection.getAdjacentX() * 2, enumdirection.getAdjacentY() * 2, enumdirection.getAdjacentZ() * 2);
        Block block = world.getType(blockposition1).getBlock();
        boolean flag1 = false;
        if (block == Blocks.PISTON_EXTENSION)
        {
          TileEntity tileentity1 = world.getTileEntity(blockposition1);
          if ((tileentity1 instanceof TileEntityPiston))
          {
            TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity1;
            if ((tileentitypiston.e() == enumdirection) && (tileentitypiston.d()))
            {
              tileentitypiston.h();
              flag1 = true;
            }
          }
        }
        if ((!flag1) && (a(block, world, blockposition1, enumdirection.opposite(), false)) && ((block.k() == 0) || (block == Blocks.PISTON) || (block == Blocks.STICKY_PISTON))) {
          a(world, blockposition, enumdirection, false);
        }
      }
      else
      {
        world.setAir(blockposition.shift(enumdirection));
      }
      world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "tile.piston.in", 0.5F, world.random.nextFloat() * 0.15F + 0.6F);
    }
    return true;
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    IBlockData iblockdata = iblockaccess.getType(blockposition);
    EnumDirection enumdirection;
    if ((iblockdata.getBlock() == this) && (((Boolean)iblockdata.get(EXTENDED)).booleanValue()))
    {
      enumdirection = (EnumDirection)iblockdata.get(FACING);
      if (enumdirection == null) {}
    }
    else
    {
      switch (SyntheticClass_1.a[enumdirection.ordinal()])
      {
      case 1: 
        a(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
        break;
      case 2: 
        a(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        break;
      case 3: 
        a(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
        break;
      case 4: 
        a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
        break;
      case 5: 
        a(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        break;
      case 6: 
        a(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
      default: 
        break;a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }
    }
  }
  
  public void j()
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, Entity entity)
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    super.a(world, blockposition, iblockdata, axisalignedbb, list, entity);
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    updateShape(world, blockposition);
    return super.a(world, blockposition, iblockdata);
  }
  
  public boolean d()
  {
    return false;
  }
  
  public static EnumDirection b(int i)
  {
    int j = i & 0x7;
    
    return j > 5 ? null : EnumDirection.fromType1(j);
  }
  
  public static EnumDirection a(World world, BlockPosition blockposition, EntityLiving entityliving)
  {
    if ((MathHelper.e((float)entityliving.locX - blockposition.getX()) < 2.0F) && (MathHelper.e((float)entityliving.locZ - blockposition.getZ()) < 2.0F))
    {
      double d0 = entityliving.locY + entityliving.getHeadHeight();
      if (d0 - blockposition.getY() > 2.0D) {
        return EnumDirection.UP;
      }
      if (blockposition.getY() - d0 > 0.0D) {
        return EnumDirection.DOWN;
      }
    }
    return entityliving.getDirection().opposite();
  }
  
  public static boolean a(Block block, World world, BlockPosition blockposition, EnumDirection enumdirection, boolean flag)
  {
    if (block == Blocks.OBSIDIAN) {
      return false;
    }
    if (!world.getWorldBorder().a(blockposition)) {
      return false;
    }
    if ((blockposition.getY() >= 0) && ((enumdirection != EnumDirection.DOWN) || (blockposition.getY() != 0)))
    {
      if ((blockposition.getY() <= world.getHeight() - 1) && ((enumdirection != EnumDirection.UP) || (blockposition.getY() != world.getHeight() - 1)))
      {
        if ((block != Blocks.PISTON) && (block != Blocks.STICKY_PISTON))
        {
          if (block.g(world, blockposition) == -1.0F) {
            return false;
          }
          if (block.k() == 2) {
            return false;
          }
          if (block.k() == 1)
          {
            if (!flag) {
              return false;
            }
            return true;
          }
        }
        else if (((Boolean)world.getType(blockposition).get(EXTENDED)).booleanValue())
        {
          return false;
        }
        return !(block instanceof IContainer);
      }
      return false;
    }
    return false;
  }
  
  private boolean a(World world, BlockPosition blockposition, EnumDirection enumdirection, boolean flag)
  {
    if (!flag) {
      world.setAir(blockposition.shift(enumdirection));
    }
    PistonExtendsChecker pistonextendschecker = new PistonExtendsChecker(world, blockposition, enumdirection, flag);
    List list = pistonextendschecker.getMovedBlocks();
    List list1 = pistonextendschecker.getBrokenBlocks();
    if (!pistonextendschecker.a()) {
      return false;
    }
    final org.bukkit.block.Block bblock = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
    
    final List<BlockPosition> moved = pistonextendschecker.getMovedBlocks();
    final List<BlockPosition> broken = pistonextendschecker.getBrokenBlocks();
    
    List<org.bukkit.block.Block> blocks = new AbstractList()
    {
      public int size()
      {
        return moved.size() + broken.size();
      }
      
      public org.bukkit.block.Block get(int index)
      {
        if ((index >= size()) || (index < 0)) {
          throw new ArrayIndexOutOfBoundsException(index);
        }
        BlockPosition pos = index < moved.size() ? (BlockPosition)moved.get(index) : (BlockPosition)broken.get(index - moved.size());
        return bblock.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
      }
    };
    int i = list.size() + list1.size();
    Block[] ablock = new Block[i];
    EnumDirection enumdirection1 = flag ? enumdirection : enumdirection.opposite();
    BlockPistonEvent event;
    BlockPistonEvent event;
    if (flag) {
      event = new BlockPistonExtendEvent(bblock, blocks, CraftBlock.notchToBlockFace(enumdirection1));
    } else {
      event = new BlockPistonRetractEvent(bblock, blocks, CraftBlock.notchToBlockFace(enumdirection1));
    }
    world.getServer().getPluginManager().callEvent(event);
    if (event.isCancelled())
    {
      for (BlockPosition b : broken) {
        world.notify(b);
      }
      for (BlockPosition b : moved)
      {
        world.notify(b);
        world.notify(b.shift(enumdirection1));
      }
      return false;
    }
    for (int j = list1.size() - 1; j >= 0; j--)
    {
      BlockPosition blockposition1 = (BlockPosition)list1.get(j);
      Block block = world.getType(blockposition1).getBlock();
      
      block.b(world, blockposition1, world.getType(blockposition1), 0);
      world.setAir(blockposition1);
      i--;
      ablock[i] = block;
    }
    for (j = list.size() - 1; j >= 0; j--)
    {
      BlockPosition blockposition1 = (BlockPosition)list.get(j);
      IBlockData iblockdata = world.getType(blockposition1);
      Block block1 = iblockdata.getBlock();
      
      block1.toLegacyData(iblockdata);
      world.setAir(blockposition1);
      blockposition1 = blockposition1.shift(enumdirection1);
      world.setTypeAndData(blockposition1, Blocks.PISTON_EXTENSION.getBlockData().set(FACING, enumdirection), 4);
      world.setTileEntity(blockposition1, BlockPistonMoving.a(iblockdata, enumdirection, flag, false));
      i--;
      ablock[i] = block1;
    }
    BlockPosition blockposition2 = blockposition.shift(enumdirection);
    if (flag)
    {
      BlockPistonExtension.EnumPistonType blockpistonextension_enumpistontype = this.N ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
      
      IBlockData iblockdata = Blocks.PISTON_HEAD.getBlockData().set(BlockPistonExtension.FACING, enumdirection).set(BlockPistonExtension.TYPE, blockpistonextension_enumpistontype);
      IBlockData iblockdata1 = Blocks.PISTON_EXTENSION.getBlockData().set(BlockPistonMoving.FACING, enumdirection).set(BlockPistonMoving.TYPE, this.N ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
      
      world.setTypeAndData(blockposition2, iblockdata1, 4);
      world.setTileEntity(blockposition2, BlockPistonMoving.a(iblockdata, enumdirection, true, false));
    }
    for (int k = list1.size() - 1; k >= 0; k--) {
      world.applyPhysics((BlockPosition)list1.get(k), ablock[(i++)]);
    }
    for (k = list.size() - 1; k >= 0; k--) {
      world.applyPhysics((BlockPosition)list.get(k), ablock[(i++)]);
    }
    if (flag)
    {
      world.applyPhysics(blockposition2, Blocks.PISTON_HEAD);
      world.applyPhysics(blockposition, this);
    }
    return true;
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(FACING, b(i)).set(EXTENDED, Boolean.valueOf((i & 0x8) > 0));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    byte b0 = 0;
    int i = b0 | ((EnumDirection)iblockdata.get(FACING)).a();
    if (((Boolean)iblockdata.get(EXTENDED)).booleanValue()) {
      i |= 0x8;
    }
    return i;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, EXTENDED });
  }
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[EnumDirection.values().length];
    
    static
    {
      try
      {
        a[EnumDirection.DOWN.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[EnumDirection.UP.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        a[EnumDirection.NORTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        a[EnumDirection.SOUTH.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        a[EnumDirection.WEST.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        a[EnumDirection.EAST.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
    }
  }
}
