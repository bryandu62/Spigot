package org.bukkit.entity;

import org.bukkit.inventory.meta.FireworkMeta;

public abstract interface Firework
  extends Entity
{
  public abstract FireworkMeta getFireworkMeta();
  
  public abstract void setFireworkMeta(FireworkMeta paramFireworkMeta);
  
  public abstract void detonate();
}
