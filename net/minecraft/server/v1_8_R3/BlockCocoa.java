package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;

public class BlockCocoa
  extends BlockDirectional
  implements IBlockFragilePlantElement
{
  public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 2);
  
  public BlockCocoa()
  {
    super(Material.PLANT);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(AGE, Integer.valueOf(0)));
    a(true);
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if (!e(world, blockposition, iblockdata))
    {
      f(world, blockposition, iblockdata);
    }
    else if (world.random.nextInt(5) == 0)
    {
      int i = ((Integer)iblockdata.get(AGE)).intValue();
      if (i < 2)
      {
        IBlockData data = iblockdata.set(AGE, Integer.valueOf(i + 1));
        CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, toLegacyData(data));
      }
    }
  }
  
  public boolean e(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    blockposition = blockposition.shift((EnumDirection)iblockdata.get(FACING));
    IBlockData iblockdata1 = world.getType(blockposition);
    
    return (iblockdata1.getBlock() == Blocks.LOG) && (iblockdata1.get(BlockWood.VARIANT) == BlockWood.EnumLogVariant.JUNGLE);
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean c()
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
    IBlockData iblockdata = iblockaccess.getType(blockposition);
    EnumDirection enumdirection = (EnumDirection)iblockdata.get(FACING);
    int i = ((Integer)iblockdata.get(AGE)).intValue();
    int j = 4 + i * 2;
    int k = 5 + i * 2;
    float f = j / 2.0F;
    switch (SyntheticClass_1.a[enumdirection.ordinal()])
    {
    case 1: 
      a((8.0F - f) / 16.0F, (12.0F - k) / 16.0F, (15.0F - j) / 16.0F, (8.0F + f) / 16.0F, 0.75F, 0.9375F);
      break;
    case 2: 
      a((8.0F - f) / 16.0F, (12.0F - k) / 16.0F, 0.0625F, (8.0F + f) / 16.0F, 0.75F, (1.0F + j) / 16.0F);
      break;
    case 3: 
      a(0.0625F, (12.0F - k) / 16.0F, (8.0F - f) / 16.0F, (1.0F + j) / 16.0F, 0.75F, (8.0F + f) / 16.0F);
      break;
    case 4: 
      a((15.0F - j) / 16.0F, (12.0F - k) / 16.0F, (8.0F - f) / 16.0F, 0.9375F, 0.75F, (8.0F + f) / 16.0F);
    }
  }
  
  public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack)
  {
    EnumDirection enumdirection = EnumDirection.fromAngle(entityliving.yaw);
    
    world.setTypeAndData(blockposition, iblockdata.set(FACING, enumdirection), 2);
  }
  
  public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving)
  {
    if (!enumdirection.k().c()) {
      enumdirection = EnumDirection.NORTH;
    }
    return getBlockData().set(FACING, enumdirection.opposite()).set(AGE, Integer.valueOf(0));
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if (!e(world, blockposition, iblockdata)) {
      f(world, blockposition, iblockdata);
    }
  }
  
  private void f(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    world.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
    b(world, blockposition, iblockdata, 0);
  }
  
  public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i)
  {
    int j = ((Integer)iblockdata.get(AGE)).intValue();
    byte b0 = 1;
    if (j >= 2) {
      b0 = 3;
    }
    for (int k = 0; k < b0; k++) {
      a(world, blockposition, new ItemStack(Items.DYE, 1, EnumColor.BROWN.getInvColorIndex()));
    }
  }
  
  public int getDropData(World world, BlockPosition blockposition)
  {
    return EnumColor.BROWN.getInvColorIndex();
  }
  
  public boolean a(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag)
  {
    return ((Integer)iblockdata.get(AGE)).intValue() < 2;
  }
  
  public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata)
  {
    return true;
  }
  
  public void b(World world, Random random, BlockPosition blockposition, IBlockData iblockdata)
  {
    IBlockData data = iblockdata.set(AGE, Integer.valueOf(((Integer)iblockdata.get(AGE)).intValue() + 1));
    CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, toLegacyData(data));
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(FACING, EnumDirection.fromType2(i)).set(AGE, Integer.valueOf((i & 0xF) >> 2));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    byte b0 = 0;
    int i = b0 | ((EnumDirection)iblockdata.get(FACING)).b();
    
    i |= ((Integer)iblockdata.get(AGE)).intValue() << 2;
    return i;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, AGE });
  }
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[EnumDirection.values().length];
    
    static
    {
      try
      {
        a[EnumDirection.SOUTH.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[EnumDirection.NORTH.ordinal()] = 2;
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
}
