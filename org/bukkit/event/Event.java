package org.bukkit.event;

public abstract class Event
{
  private String name;
  private final boolean async;
  
  public Event()
  {
    this(false);
  }
  
  public Event(boolean isAsync)
  {
    this.async = isAsync;
  }
  
  public String getEventName()
  {
    if (this.name == null) {
      this.name = getClass().getSimpleName();
    }
    return this.name;
  }
  
  public abstract HandlerList getHandlers();
  
  public final boolean isAsynchronous()
  {
    return this.async;
  }
  
  public static enum Result
  {
    DENY,  DEFAULT,  ALLOW;
  }
}
