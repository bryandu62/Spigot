package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class EntityUnleashEvent
  extends EntityEvent
{
  private static final HandlerList handlers = new HandlerList();
  private final UnleashReason reason;
  
  public EntityUnleashEvent(Entity entity, UnleashReason reason)
  {
    super(entity);
    this.reason = reason;
  }
  
  public UnleashReason getReason()
  {
    return this.reason;
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
  
  public static enum UnleashReason
  {
    HOLDER_GONE,  PLAYER_UNLEASH,  DISTANCE,  UNKNOWN;
  }
}
