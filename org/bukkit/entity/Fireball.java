package org.bukkit.entity;

import org.bukkit.util.Vector;

public abstract interface Fireball
  extends Projectile, Explosive
{
  public abstract void setDirection(Vector paramVector);
  
  public abstract Vector getDirection();
}
