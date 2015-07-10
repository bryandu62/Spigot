package org.bukkit.event.player;

import java.net.InetAddress;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerLoginEvent
  extends PlayerEvent
{
  private static final HandlerList handlers = new HandlerList();
  private final InetAddress address;
  private final String hostname;
  private Result result = Result.ALLOWED;
  private String message = "";
  private final InetAddress realAddress;
  
  @Deprecated
  public PlayerLoginEvent(Player player)
  {
    this(player, "", null);
  }
  
  @Deprecated
  public PlayerLoginEvent(Player player, String hostname)
  {
    this(player, hostname, null);
  }
  
  public PlayerLoginEvent(Player player, String hostname, InetAddress address, InetAddress realAddress)
  {
    super(player);
    this.hostname = hostname;
    this.address = address;
    
    this.realAddress = realAddress;
  }
  
  public PlayerLoginEvent(Player player, String hostname, InetAddress address)
  {
    this(player, hostname, address, address);
  }
  
  @Deprecated
  public PlayerLoginEvent(Player player, Result result, String message)
  {
    this(player, "", null, result, message, null);
  }
  
  public PlayerLoginEvent(Player player, String hostname, InetAddress address, Result result, String message, InetAddress realAddress)
  {
    this(player, hostname, address, realAddress);
  }
  
  public InetAddress getRealAddress()
  {
    return this.realAddress;
  }
  
  public Result getResult()
  {
    return this.result;
  }
  
  public void setResult(Result result)
  {
    this.result = result;
  }
  
  public String getKickMessage()
  {
    return this.message;
  }
  
  public void setKickMessage(String message)
  {
    this.message = message;
  }
  
  public String getHostname()
  {
    return this.hostname;
  }
  
  public void allow()
  {
    this.result = Result.ALLOWED;
    this.message = "";
  }
  
  public void disallow(Result result, String message)
  {
    this.result = result;
    this.message = message;
  }
  
  public InetAddress getAddress()
  {
    return this.address;
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
  
  public static enum Result
  {
    ALLOWED,  KICK_FULL,  KICK_BANNED,  KICK_WHITELIST,  KICK_OTHER;
  }
}
