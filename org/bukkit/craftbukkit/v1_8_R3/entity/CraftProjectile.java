package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityProjectile;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public abstract class CraftProjectile
  extends AbstractProjectile
  implements Projectile
{
  public CraftProjectile(CraftServer server, Entity entity)
  {
    super(server, entity);
  }
  
  public ProjectileSource getShooter()
  {
    return getHandle().projectileSource;
  }
  
  public void setShooter(ProjectileSource shooter)
  {
    if ((shooter instanceof CraftLivingEntity))
    {
      getHandle().shooter = ((EntityLiving)((CraftLivingEntity)shooter).entity);
      if ((shooter instanceof CraftHumanEntity)) {
        getHandle().shooterName = ((CraftHumanEntity)shooter).getName();
      }
    }
    else
    {
      getHandle().shooter = null;
      getHandle().shooterName = null;
    }
    getHandle().projectileSource = shooter;
  }
  
  public EntityProjectile getHandle()
  {
    return (EntityProjectile)this.entity;
  }
  
  public String toString()
  {
    return "CraftProjectile";
  }
}
