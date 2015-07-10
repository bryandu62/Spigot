package org.bukkit.projectiles;

import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public abstract interface ProjectileSource
{
  public abstract <T extends Projectile> T launchProjectile(Class<? extends T> paramClass);
  
  public abstract <T extends Projectile> T launchProjectile(Class<? extends T> paramClass, Vector paramVector);
}
