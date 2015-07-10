package org.bukkit.entity;

public abstract interface Creature
  extends LivingEntity
{
  public abstract void setTarget(LivingEntity paramLivingEntity);
  
  public abstract LivingEntity getTarget();
}
