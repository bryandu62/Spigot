package org.bukkit.event.server;

import java.net.InetAddress;
import java.util.Iterator;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.CachedServerIcon;

public class ServerListPingEvent
  extends ServerEvent
  implements Iterable<Player>
{
  private static final int MAGIC_PLAYER_COUNT = Integer.MIN_VALUE;
  private static final HandlerList handlers = new HandlerList();
  private final InetAddress address;
  private String motd;
  private final int numPlayers;
  private int maxPlayers;
  
  public ServerListPingEvent(InetAddress address, String motd, int numPlayers, int maxPlayers)
  {
    Validate.isTrue(numPlayers >= 0, "Cannot have negative number of players online", numPlayers);
    this.address = address;
    this.motd = motd;
    this.numPlayers = numPlayers;
    this.maxPlayers = maxPlayers;
  }
  
  protected ServerListPingEvent(InetAddress address, String motd, int maxPlayers)
  {
    this.numPlayers = Integer.MIN_VALUE;
    this.address = address;
    this.motd = motd;
    this.maxPlayers = maxPlayers;
  }
  
  public InetAddress getAddress()
  {
    return this.address;
  }
  
  public String getMotd()
  {
    return this.motd;
  }
  
  public void setMotd(String motd)
  {
    this.motd = motd;
  }
  
  public int getNumPlayers()
  {
    int numPlayers = this.numPlayers;
    if (numPlayers == Integer.MIN_VALUE)
    {
      numPlayers = 0;
      for (Iterator localIterator = iterator(); localIterator.hasNext();)
      {
        ((Player)localIterator.next());
        numPlayers++;
      }
    }
    return numPlayers;
  }
  
  public int getMaxPlayers()
  {
    return this.maxPlayers;
  }
  
  public void setMaxPlayers(int maxPlayers)
  {
    this.maxPlayers = maxPlayers;
  }
  
  public void setServerIcon(CachedServerIcon icon)
    throws IllegalArgumentException, UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
  
  public Iterator<Player> iterator()
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }
}
