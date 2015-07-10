package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.plugin.PluginManager;

public class EntitySnowman
  extends EntityGolem
  implements IRangedEntity
{
  public EntitySnowman(World world)
  {
    super(world);
    setSize(0.7F, 1.9F);
    ((Navigation)getNavigation()).a(true);
    this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.25D, 20, 10.0F));
    this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
    this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
    this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
    this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityInsentient.class, 10, true, false, IMonster.d));
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.maxHealth).setValue(4.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.20000000298023224D);
  }
  
  public void m()
  {
    super.m();
    if (!this.world.isClientSide)
    {
      int i = MathHelper.floor(this.locX);
      int j = MathHelper.floor(this.locY);
      int k = MathHelper.floor(this.locZ);
      if (U()) {
        damageEntity(DamageSource.DROWN, 1.0F);
      }
      if (this.world.getBiome(new BlockPosition(i, 0, k)).a(new BlockPosition(i, j, k)) > 1.0F) {
        damageEntity(CraftEventFactory.MELTING, 1.0F);
      }
      for (int l = 0; l < 4; l++)
      {
        i = MathHelper.floor(this.locX + (l % 2 * 2 - 1) * 0.25F);
        j = MathHelper.floor(this.locY);
        k = MathHelper.floor(this.locZ + (l / 2 % 2 * 2 - 1) * 0.25F);
        BlockPosition blockposition = new BlockPosition(i, j, k);
        if ((this.world.getType(blockposition).getBlock().getMaterial() == Material.AIR) && (this.world.getBiome(new BlockPosition(i, 0, k)).a(blockposition) < 0.8F) && (Blocks.SNOW_LAYER.canPlace(this.world, blockposition)))
        {
          BlockState blockState = this.world.getWorld().getBlockAt(i, j, k).getState();
          blockState.setType(CraftMagicNumbers.getMaterial(Blocks.SNOW_LAYER));
          
          EntityBlockFormEvent event = new EntityBlockFormEvent(getBukkitEntity(), blockState.getBlock(), blockState);
          this.world.getServer().getPluginManager().callEvent(event);
          if (!event.isCancelled()) {
            blockState.update(true);
          }
        }
      }
    }
  }
  
  protected Item getLoot()
  {
    return Items.SNOWBALL;
  }
  
  protected void dropDeathLoot(boolean flag, int i)
  {
    int j = this.random.nextInt(16);
    for (int k = 0; k < j; k++) {
      a(Items.SNOWBALL, 1);
    }
  }
  
  public void a(EntityLiving entityliving, float f)
  {
    EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
    double d0 = entityliving.locY + entityliving.getHeadHeight() - 1.100000023841858D;
    double d1 = entityliving.locX - this.locX;
    double d2 = d0 - entitysnowball.locY;
    double d3 = entityliving.locZ - this.locZ;
    float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
    
    entitysnowball.shoot(d1, d2 + f1, d3, 1.6F, 12.0F);
    makeSound("random.bow", 1.0F, 1.0F / (bc().nextFloat() * 0.4F + 0.8F));
    this.world.addEntity(entitysnowball);
  }
  
  public float getHeadHeight()
  {
    return 1.7F;
  }
}
