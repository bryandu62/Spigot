package net.minecraft.server.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.PluginManager;

public class EntityLargeFireball
  extends EntityFireball
{
  public int yield = 1;
  
  public EntityLargeFireball(World world)
  {
    super(world);
  }
  
  public EntityLargeFireball(World world, EntityLiving entityliving, double d0, double d1, double d2)
  {
    super(world, entityliving, d0, d1, d2);
  }
  
  protected void a(MovingObjectPosition movingobjectposition)
  {
    if (!this.world.isClientSide)
    {
      if (movingobjectposition.entity != null)
      {
        movingobjectposition.entity.damageEntity(DamageSource.fireball(this, this.shooter), 6.0F);
        a(this.shooter, movingobjectposition.entity);
      }
      boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
      
      ExplosionPrimeEvent event = new ExplosionPrimeEvent((Explosive)CraftEntity.getEntity(this.world.getServer(), this));
      this.world.getServer().getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
        this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire(), flag);
      }
      die();
    }
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setInt("ExplosionPower", this.yield);
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    if (nbttagcompound.hasKeyOfType("ExplosionPower", 99)) {
      this.bukkitYield = (this.yield = nbttagcompound.getInt("ExplosionPower"));
    }
  }
}
