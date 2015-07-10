package org.bukkit.entity;

import org.bukkit.util.Vector;

public abstract interface Vehicle
  extends Entity
{
  public abstract Vector getVelocity();
  
  public abstract void setVelocity(Vector paramVector);
}
