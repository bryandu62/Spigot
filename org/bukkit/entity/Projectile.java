package org.bukkit.entity;

import org.bukkit.projectiles.ProjectileSource;

public abstract interface Projectile
  extends Entity
{
  public abstract ProjectileSource getShooter();
  
  public abstract void setShooter(ProjectileSource paramProjectileSource);
  
  public abstract boolean doesBounce();
  
  public abstract void setBounce(boolean paramBoolean);
}
