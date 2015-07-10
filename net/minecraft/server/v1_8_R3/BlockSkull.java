package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.util.BlockStateListPopulator;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BlockSkull
  extends BlockContainer
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing");
  public static final BlockStateBoolean NODROP = BlockStateBoolean.of("nodrop");
  private static final Predicate<ShapeDetectorBlock> N = new Predicate()
  {
    public boolean a(ShapeDetectorBlock shapedetectorblock)
    {
      return (shapedetectorblock.a() != null) && (shapedetectorblock.a().getBlock() == Blocks.SKULL) && ((shapedetectorblock.b() instanceof TileEntitySkull)) && (((TileEntitySkull)shapedetectorblock.b()).getSkullType() == 1);
    }
    
    public boolean apply(Object object)
    {
      return a((ShapeDetectorBlock)object);
    }
  };
  private ShapeDetector O;
  private ShapeDetector P;
  
  protected BlockSkull()
  {
    super(Material.ORIENTABLE);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(NODROP, Boolean.valueOf(false)));
    a(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
  }
  
  public String getName()
  {
    return LocaleI18n.get("tile.skull.skeleton.name");
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    switch (SyntheticClass_1.a[((EnumDirection)iblockaccess.getType(blockposition).get(FACING)).ordinal()])
    {
    case 1: 
    default: 
      a(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
      break;
    case 2: 
      a(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
      break;
    case 3: 
      a(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
      break;
    case 4: 
      a(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
      break;
    case 5: 
      a(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
    }
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    updateShape(world, blockposition);
    return super.a(world, blockposition, iblockdata);
  }
  
  public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving)
  {
    return getBlockData().set(FACING, entityliving.getDirection()).set(NODROP, Boolean.valueOf(false));
  }
  
  public TileEntity a(World world, int i)
  {
    return new TileEntitySkull();
  }
  
  public int getDropData(World world, BlockPosition blockposition)
  {
    TileEntity tileentity = world.getTileEntity(blockposition);
    
    return (tileentity instanceof TileEntitySkull) ? ((TileEntitySkull)tileentity).getSkullType() : super.getDropData(world, blockposition);
  }
  
  public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i)
  {
    if (world.random.nextFloat() < f)
    {
      ItemStack itemstack = new ItemStack(Items.SKULL, 1, getDropData(world, blockposition));
      TileEntitySkull tileentityskull = (TileEntitySkull)world.getTileEntity(blockposition);
      if ((tileentityskull.getSkullType() == 3) && (tileentityskull.getGameProfile() != null))
      {
        itemstack.setTag(new NBTTagCompound());
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        
        GameProfileSerializer.serialize(nbttagcompound, tileentityskull.getGameProfile());
        itemstack.getTag().set("SkullOwner", nbttagcompound);
      }
      a(world, blockposition, itemstack);
    }
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman)
  {
    if (entityhuman.abilities.canInstantlyBuild)
    {
      iblockdata = iblockdata.set(NODROP, Boolean.valueOf(true));
      world.setTypeAndData(blockposition, iblockdata, 4);
    }
    super.a(world, blockposition, iblockdata, entityhuman);
  }
  
  public void remove(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (!world.isClientSide) {
      super.remove(world, blockposition, iblockdata);
    }
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return Items.SKULL;
  }
  
  public boolean b(World world, BlockPosition blockposition, ItemStack itemstack)
  {
    return l().a(world, blockposition) != null;
  }
  
  public void a(World world, BlockPosition blockposition, TileEntitySkull tileentityskull)
  {
    if (world.captureBlockStates) {
      return;
    }
    if ((tileentityskull.getSkullType() == 1) && (blockposition.getY() >= 2) && (world.getDifficulty() != EnumDifficulty.PEACEFUL) && (!world.isClientSide))
    {
      ShapeDetector shapedetector = n();
      ShapeDetector.ShapeDetectorCollection shapedetector_shapedetectorcollection = shapedetector.a(world, blockposition);
      if (shapedetector_shapedetectorcollection != null)
      {
        BlockStateListPopulator blockList = new BlockStateListPopulator(world.getWorld());
        for (int i = 0; i < 3; i++)
        {
          ShapeDetectorBlock shapedetectorblock = shapedetector_shapedetectorcollection.a(i, 0, 0);
          
          BlockPosition pos = shapedetectorblock.d();
          IBlockData data = shapedetectorblock.a().set(NODROP, Boolean.valueOf(true));
          blockList.setTypeAndData(pos.getX(), pos.getY(), pos.getZ(), data.getBlock(), data.getBlock().toLegacyData(data), 2);
        }
        for (i = 0; i < shapedetector.c(); i++) {
          for (int j = 0; j < shapedetector.b(); j++)
          {
            ShapeDetectorBlock shapedetectorblock1 = shapedetector_shapedetectorcollection.a(i, j, 0);
            
            BlockPosition pos = shapedetectorblock1.d();
            blockList.setTypeAndData(pos.getX(), pos.getY(), pos.getZ(), Blocks.AIR, 0, 2);
          }
        }
        BlockPosition blockposition1 = shapedetector_shapedetectorcollection.a(1, 0, 0).d();
        EntityWither entitywither = new EntityWither(world);
        BlockPosition blockposition2 = shapedetector_shapedetectorcollection.a(1, 2, 0).d();
        
        entitywither.setPositionRotation(blockposition2.getX() + 0.5D, blockposition2.getY() + 0.55D, blockposition2.getZ() + 0.5D, shapedetector_shapedetectorcollection.b().k() == EnumDirection.EnumAxis.X ? 0.0F : 90.0F, 0.0F);
        entitywither.aI = (shapedetector_shapedetectorcollection.b().k() == EnumDirection.EnumAxis.X ? 0.0F : 90.0F);
        entitywither.n();
        Iterator iterator = world.a(EntityHuman.class, entitywither.getBoundingBox().grow(50.0D, 50.0D, 50.0D)).iterator();
        if (world.addEntity(entitywither, CreatureSpawnEvent.SpawnReason.BUILD_WITHER))
        {
          blockList.updateList();
          while (iterator.hasNext())
          {
            EntityHuman entityhuman = (EntityHuman)iterator.next();
            
            entityhuman.b(AchievementList.I);
          }
          for (int k = 0; k < 120; k++) {
            world.addParticle(EnumParticle.SNOWBALL, blockposition1.getX() + world.random.nextDouble(), blockposition1.getY() - 2 + world.random.nextDouble() * 3.9D, blockposition1.getZ() + world.random.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
          }
          for (k = 0; k < shapedetector.c(); k++) {
            for (int l = 0; l < shapedetector.b(); l++)
            {
              ShapeDetectorBlock shapedetectorblock2 = shapedetector_shapedetectorcollection.a(k, l, 0);
              
              world.update(shapedetectorblock2.d(), Blocks.AIR);
            }
          }
        }
      }
    }
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(FACING, EnumDirection.fromType1(i & 0x7)).set(NODROP, Boolean.valueOf((i & 0x8) > 0));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    byte b0 = 0;
    int i = b0 | ((EnumDirection)iblockdata.get(FACING)).a();
    if (((Boolean)iblockdata.get(NODROP)).booleanValue()) {
      i |= 0x8;
    }
    return i;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, NODROP });
  }
  
  protected ShapeDetector l()
  {
    if (this.O == null) {
      this.O = ShapeDetectorBuilder.a().a(new String[] { "   ", "###", "~#~" }).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.SOUL_SAND))).a('~', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.AIR))).b();
    }
    return this.O;
  }
  
  protected ShapeDetector n()
  {
    if (this.P == null) {
      this.P = ShapeDetectorBuilder.a().a(new String[] { "^^^", "###", "~#~" }).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.SOUL_SAND))).a('^', N).a('~', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.AIR))).b();
    }
    return this.P;
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
        a[EnumDirection.WEST.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        a[EnumDirection.EAST.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
    }
  }
}
