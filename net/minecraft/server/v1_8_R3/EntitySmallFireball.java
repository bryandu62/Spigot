package net.minecraft.server.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.plugin.PluginManager;

public class EntitySmallFireball
  extends EntityFireball
{
  public EntitySmallFireball(World world)
  {
    super(world);
    setSize(0.3125F, 0.3125F);
  }
  
  public EntitySmallFireball(World world, EntityLiving entityliving, double d0, double d1, double d2)
  {
    super(world, entityliving, d0, d1, d2);
    setSize(0.3125F, 0.3125F);
  }
  
  public EntitySmallFireball(World world, double d0, double d1, double d2, double d3, double d4, double d5)
  {
    super(world, d0, d1, d2, d3, d4, d5);
    setSize(0.3125F, 0.3125F);
  }
  
  protected void a(MovingObjectPosition movingobjectposition)
  {
    if (!this.world.isClientSide)
    {
      if (movingobjectposition.entity != null)
      {
        boolean flag = movingobjectposition.entity.damageEntity(DamageSource.fireball(this, this.shooter), 5.0F);
        if (flag)
        {
          a(this.shooter, movingobjectposition.entity);
          if (!movingobjectposition.entity.isFireProof())
          {
            EntityCombustByEntityEvent event = new EntityCombustByEntityEvent((Projectile)getBukkitEntity(), movingobjectposition.entity.getBukkitEntity(), 5);
            movingobjectposition.entity.world.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
              movingobjectposition.entity.setOnFire(event.getDuration());
            }
          }
        }
      }
      else
      {
        boolean flag = true;
        if ((this.shooter != null) && ((this.shooter instanceof EntityInsentient))) {
          flag = this.world.getGameRules().getBoolean("mobGriefing");
        }
        if (flag)
        {
          BlockPosition blockposition = movingobjectposition.a().shift(movingobjectposition.direction);
          if (this.world.isEmpty(blockposition)) {
            if ((this.isIncendiary) && (!CraftEventFactory.callBlockIgniteEvent(this.world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled())) {
              this.world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
            }
          }
        }
      }
      die();
    }
  }
  
  public boolean ad()
  {
    return false;
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    return false;
  }
}
