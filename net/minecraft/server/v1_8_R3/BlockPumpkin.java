package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.util.BlockStateListPopulator;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BlockPumpkin
  extends BlockDirectional
{
  private ShapeDetector snowGolemPart;
  private ShapeDetector snowGolem;
  private ShapeDetector ironGolemPart;
  private ShapeDetector ironGolem;
  private static final Predicate<IBlockData> Q = new Predicate()
  {
    public boolean a(IBlockData iblockdata)
    {
      return (iblockdata != null) && ((iblockdata.getBlock() == Blocks.PUMPKIN) || (iblockdata.getBlock() == Blocks.LIT_PUMPKIN));
    }
    
    public boolean apply(Object object)
    {
      return a((IBlockData)object);
    }
  };
  
  protected BlockPumpkin()
  {
    super(Material.PUMPKIN, MaterialMapColor.q);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
    a(true);
    a(CreativeModeTab.b);
  }
  
  public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    super.onPlace(world, blockposition, iblockdata);
    f(world, blockposition);
  }
  
  public boolean e(World world, BlockPosition blockposition)
  {
    return (getDetectorSnowGolemPart().a(world, blockposition) != null) || (getDetectorIronGolemPart().a(world, blockposition) != null);
  }
  
  private void f(World world, BlockPosition blockposition)
  {
    ShapeDetector.ShapeDetectorCollection shapedetector_shapedetectorcollection;
    if ((shapedetector_shapedetectorcollection = getDetectorSnowGolem().a(world, blockposition)) != null)
    {
      BlockStateListPopulator blockList = new BlockStateListPopulator(world.getWorld());
      for (int i = 0; i < getDetectorSnowGolem().b(); i++)
      {
        ShapeDetectorBlock shapedetectorblock = shapedetector_shapedetectorcollection.a(0, i, 0);
        
        BlockPosition pos = shapedetectorblock.d();
        blockList.setTypeId(pos.getX(), pos.getY(), pos.getZ(), 0);
      }
      EntitySnowman entitysnowman = new EntitySnowman(world);
      BlockPosition blockposition1 = shapedetector_shapedetectorcollection.a(0, 2, 0).d();
      
      entitysnowman.setPositionRotation(blockposition1.getX() + 0.5D, blockposition1.getY() + 0.05D, blockposition1.getZ() + 0.5D, 0.0F, 0.0F);
      if (world.addEntity(entitysnowman, CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN))
      {
        blockList.updateList();
        for (int j = 0; j < 120; j++) {
          world.addParticle(EnumParticle.SNOW_SHOVEL, blockposition1.getX() + world.random.nextDouble(), blockposition1.getY() + world.random.nextDouble() * 2.5D, blockposition1.getZ() + world.random.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
        }
        for (j = 0; j < getDetectorSnowGolem().b(); j++)
        {
          ShapeDetectorBlock shapedetectorblock1 = shapedetector_shapedetectorcollection.a(0, j, 0);
          
          world.update(shapedetectorblock1.d(), Blocks.AIR);
        }
      }
    }
    else if ((shapedetector_shapedetectorcollection = getDetectorIronGolem().a(world, blockposition)) != null)
    {
      BlockStateListPopulator blockList = new BlockStateListPopulator(world.getWorld());
      for (int i = 0; i < getDetectorIronGolem().c(); i++) {
        for (int k = 0; k < getDetectorIronGolem().b(); k++)
        {
          BlockPosition pos = shapedetector_shapedetectorcollection.a(i, k, 0).d();
          blockList.setTypeId(pos.getX(), pos.getY(), pos.getZ(), 0);
        }
      }
      BlockPosition blockposition2 = shapedetector_shapedetectorcollection.a(1, 2, 0).d();
      EntityIronGolem entityirongolem = new EntityIronGolem(world);
      
      entityirongolem.setPlayerCreated(true);
      entityirongolem.setPositionRotation(blockposition2.getX() + 0.5D, blockposition2.getY() + 0.05D, blockposition2.getZ() + 0.5D, 0.0F, 0.0F);
      if (world.addEntity(entityirongolem, CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM))
      {
        blockList.updateList();
        for (int j = 0; j < 120; j++) {
          world.addParticle(EnumParticle.SNOWBALL, blockposition2.getX() + world.random.nextDouble(), blockposition2.getY() + world.random.nextDouble() * 3.9D, blockposition2.getZ() + world.random.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
        }
        for (j = 0; j < getDetectorIronGolem().c(); j++) {
          for (int l = 0; l < getDetectorIronGolem().b(); l++)
          {
            ShapeDetectorBlock shapedetectorblock2 = shapedetector_shapedetectorcollection.a(j, l, 0);
            
            world.update(shapedetectorblock2.d(), Blocks.AIR);
          }
        }
      }
    }
  }
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    return (world.getType(blockposition).getBlock().material.isReplaceable()) && (World.a(world, blockposition.down()));
  }
  
  public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving)
  {
    return getBlockData().set(FACING, entityliving.getDirection().opposite());
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(FACING, EnumDirection.fromType2(i));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    return ((EnumDirection)iblockdata.get(FACING)).b();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING });
  }
  
  protected ShapeDetector getDetectorSnowGolemPart()
  {
    if (this.snowGolemPart == null) {
      this.snowGolemPart = ShapeDetectorBuilder.a().a(new String[] { " ", "#", "#" }).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.SNOW))).b();
    }
    return this.snowGolemPart;
  }
  
  protected ShapeDetector getDetectorSnowGolem()
  {
    if (this.snowGolem == null) {
      this.snowGolem = ShapeDetectorBuilder.a().a(new String[] { "^", "#", "#" }).a('^', ShapeDetectorBlock.a(Q)).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.SNOW))).b();
    }
    return this.snowGolem;
  }
  
  protected ShapeDetector getDetectorIronGolemPart()
  {
    if (this.ironGolemPart == null) {
      this.ironGolemPart = ShapeDetectorBuilder.a().a(new String[] { "~ ~", "###", "~#~" }).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.IRON_BLOCK))).a('~', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.AIR))).b();
    }
    return this.ironGolemPart;
  }
  
  protected ShapeDetector getDetectorIronGolem()
  {
    if (this.ironGolem == null) {
      this.ironGolem = ShapeDetectorBuilder.a().a(new String[] { "~^~", "###", "~#~" }).a('^', ShapeDetectorBlock.a(Q)).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.IRON_BLOCK))).a('~', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.AIR))).b();
    }
    return this.ironGolem;
  }
}
