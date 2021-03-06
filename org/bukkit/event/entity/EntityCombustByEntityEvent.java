package org.bukkit.event.entity;

import org.bukkit.entity.Entity;

public class EntityCombustByEntityEvent
  extends EntityCombustEvent
{
  private final Entity combuster;
  
  public EntityCombustByEntityEvent(Entity combuster, Entity combustee, int duration)
  {
    super(combustee, duration);
    this.combuster = combuster;
  }
  
  public Entity getCombuster()
  {
    return this.combuster;
  }
}
