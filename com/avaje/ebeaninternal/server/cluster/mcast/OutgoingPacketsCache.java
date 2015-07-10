package com.avaje.ebeaninternal.server.cluster.mcast;

import com.avaje.ebeaninternal.server.cluster.Packet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class OutgoingPacketsCache
{
  private final Map<Long, Packet> packetMap = new TreeMap();
  
  public int size()
  {
    return this.packetMap.size();
  }
  
  public Packet getPacket(Long packetId)
  {
    return (Packet)this.packetMap.get(packetId);
  }
  
  public String toString()
  {
    return this.packetMap.keySet().toString();
  }
  
  public void remove(Packet packet)
  {
    this.packetMap.remove(Long.valueOf(packet.getPacketId()));
  }
  
  public void registerPackets(List<Packet> packets)
  {
    for (int i = 0; i < packets.size(); i++)
    {
      Packet p = (Packet)packets.get(i);
      this.packetMap.put(Long.valueOf(p.getPacketId()), p);
    }
  }
  
  public int trimAll()
  {
    int size = this.packetMap.size();
    this.packetMap.clear();
    return size;
  }
  
  public void trimAcknowledgedMessages(long minAcked)
  {
    Iterator<Long> it = this.packetMap.keySet().iterator();
    while (it.hasNext())
    {
      Long pktId = (Long)it.next();
      if (minAcked >= pktId.longValue()) {
        it.remove();
      }
    }
  }
}
