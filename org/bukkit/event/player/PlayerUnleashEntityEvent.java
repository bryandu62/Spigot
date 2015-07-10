package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.EntityUnleashEvent.UnleashReason;

public class PlayerUnleashEntityEvent
  extends EntityUnleashEvent
  implements Cancellable
{
  private final Player player;
  private boolean cancelled = false;
  
  public PlayerUnleashEntityEvent(Entity entity, Player player)
  {
    super(entity, EntityUnleashEvent.UnleashReason.PLAYER_UNLEASH);
    this.player = player;
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
  
  public boolean isCancelled()
  {
    return this.cancelled;
  }
  
  public void setCancelled(boolean cancel)
  {
    this.cancelled = cancel;
  }
}
