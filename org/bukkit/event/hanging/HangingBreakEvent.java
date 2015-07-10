package org.bukkit.event.hanging;

import org.bukkit.entity.Hanging;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class HangingBreakEvent
  extends HangingEvent
  implements Cancellable
{
  private static final HandlerList handlers = new HandlerList();
  private boolean cancelled;
  private final RemoveCause cause;
  
  public HangingBreakEvent(Hanging hanging, RemoveCause cause)
  {
    super(hanging);
    this.cause = cause;
  }
  
  public RemoveCause getCause()
  {
    return this.cause;
  }
  
  public boolean isCancelled()
  {
    return this.cancelled;
  }
  
  public void setCancelled(boolean cancel)
  {
    this.cancelled = cancel;
  }
  
  public static enum RemoveCause
  {
    ENTITY,  EXPLOSION,  OBSTRUCTION,  PHYSICS,  DEFAULT;
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
}
