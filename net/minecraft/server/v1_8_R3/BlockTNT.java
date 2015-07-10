package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BlockTNT
  extends Block
{
  public static final BlockStateBoolean EXPLODE = BlockStateBoolean.of("explode");
  
  public BlockTNT()
  {
    super(Material.TNT);
    j(this.blockStateList.getBlockData().set(EXPLODE, Boolean.valueOf(false)));
    a(CreativeModeTab.d);
  }
  
  public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    super.onPlace(world, blockposition, iblockdata);
    if (world.isBlockIndirectlyPowered(blockposition))
    {
      postBreak(world, blockposition, iblockdata.set(EXPLODE, Boolean.valueOf(true)));
      world.setAir(blockposition);
    }
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if (world.isBlockIndirectlyPowered(blockposition))
    {
      postBreak(world, blockposition, iblockdata.set(EXPLODE, Boolean.valueOf(true)));
      world.setAir(blockposition);
    }
  }
  
  public void wasExploded(World world, BlockPosition blockposition, Explosion explosion)
  {
    if (!world.isClientSide)
    {
      EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, blockposition.getX() + 0.5F, blockposition.getY(), blockposition.getZ() + 0.5F, explosion.c());
      
      entitytntprimed.fuseTicks = (world.random.nextInt(entitytntprimed.fuseTicks / 4) + entitytntprimed.fuseTicks / 8);
      world.addEntity(entitytntprimed);
    }
  }
  
  public void postBreak(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    a(world, blockposition, iblockdata, null);
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving)
  {
    if ((!world.isClientSide) && 
      (((Boolean)iblockdata.get(EXPLODE)).booleanValue()))
    {
      EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, blockposition.getX() + 0.5F, blockposition.getY(), blockposition.getZ() + 0.5F, entityliving);
      
      world.addEntity(entitytntprimed);
      world.makeSound(entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);
    }
  }
  
  public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2)
  {
    if (entityhuman.bZ() != null)
    {
      Item item = entityhuman.bZ().getItem();
      if ((item == Items.FLINT_AND_STEEL) || (item == Items.FIRE_CHARGE))
      {
        a(world, blockposition, iblockdata.set(EXPLODE, Boolean.valueOf(true)), entityhuman);
        world.setAir(blockposition);
        if (item == Items.FLINT_AND_STEEL) {
          entityhuman.bZ().damage(1, entityhuman);
        } else if (!entityhuman.abilities.canInstantlyBuild) {
          entityhuman.bZ().count -= 1;
        }
        return true;
      }
    }
    return super.interact(world, blockposition, iblockdata, entityhuman, enumdirection, f, f1, f2);
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity)
  {
    if ((!world.isClientSide) && ((entity instanceof EntityArrow)))
    {
      EntityArrow entityarrow = (EntityArrow)entity;
      if (entityarrow.isBurning())
      {
        if (CraftEventFactory.callEntityChangeBlockEvent(entityarrow, blockposition.getX(), blockposition.getY(), blockposition.getZ(), Blocks.AIR, 0).isCancelled()) {
          return;
        }
        a(world, blockposition, world.getType(blockposition).set(EXPLODE, Boolean.valueOf(true)), (entityarrow.shooter instanceof EntityLiving) ? (EntityLiving)entityarrow.shooter : null);
        world.setAir(blockposition);
      }
    }
  }
  
  public boolean a(Explosion explosion)
  {
    return false;
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(EXPLODE, Boolean.valueOf((i & 0x1) > 0));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    return ((Boolean)iblockdata.get(EXPLODE)).booleanValue() ? 1 : 0;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { EXPLODE });
  }
}
