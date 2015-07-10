package com.avaje.ebeaninternal.server.cluster.socket;

import com.avaje.ebeaninternal.server.cluster.DataHolder;
import com.avaje.ebeaninternal.server.cluster.Packet;
import java.io.Serializable;

public class SocketClusterMessage
  implements Serializable
{
  private static final long serialVersionUID = 2993350408394934473L;
  private final String registerHost;
  private final boolean register;
  private final DataHolder dataHolder;
  
  public static SocketClusterMessage register(String registerHost, boolean register)
  {
    return new SocketClusterMessage(registerHost, register);
  }
  
  public static SocketClusterMessage transEvent(DataHolder transEvent)
  {
    return new SocketClusterMessage(transEvent);
  }
  
  public static SocketClusterMessage packet(Packet packet)
  {
    DataHolder d = new DataHolder(packet.getBytes());
    return new SocketClusterMessage(d);
  }
  
  private SocketClusterMessage(String registerHost, boolean register)
  {
    this.registerHost = registerHost;
    this.register = register;
    this.dataHolder = null;
  }
  
  private SocketClusterMessage(DataHolder dataHolder)
  {
    this.dataHolder = dataHolder;
    this.registerHost = null;
    this.register = false;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    if (this.registerHost != null)
    {
      sb.append("register ");
      sb.append(this.register);
      sb.append(" ");
      sb.append(this.registerHost);
    }
    else
    {
      sb.append("transEvent ");
    }
    return sb.toString();
  }
  
  public boolean isRegisterEvent()
  {
    return this.registerHost != null;
  }
  
  public String getRegisterHost()
  {
    return this.registerHost;
  }
  
  public boolean isRegister()
  {
    return this.register;
  }
  
  public DataHolder getDataHolder()
  {
    return this.dataHolder;
  }
}
