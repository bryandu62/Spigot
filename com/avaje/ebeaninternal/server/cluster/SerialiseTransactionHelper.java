package com.avaje.ebeaninternal.server.cluster;

import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.transaction.RemoteTransactionEvent;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public abstract class SerialiseTransactionHelper
{
  private final PacketWriter packetWriter;
  
  public SerialiseTransactionHelper()
  {
    this.packetWriter = new PacketWriter(Integer.MAX_VALUE);
  }
  
  public abstract SpiEbeanServer getEbeanServer(String paramString);
  
  public DataHolder createDataHolder(RemoteTransactionEvent transEvent)
    throws IOException
  {
    List<Packet> packetList = this.packetWriter.write(transEvent);
    if (packetList.size() != 1) {
      throw new RuntimeException("Always expecting 1 Packet but got " + packetList.size());
    }
    byte[] data = ((Packet)packetList.get(0)).getBytes();
    return new DataHolder(data);
  }
  
  public RemoteTransactionEvent read(DataHolder dataHolder)
    throws IOException
  {
    ByteArrayInputStream bi = new ByteArrayInputStream(dataHolder.getData());
    DataInputStream dataInput = new DataInputStream(bi);
    
    Packet header = Packet.readHeader(dataInput);
    
    SpiEbeanServer server = getEbeanServer(header.getServerName());
    
    PacketTransactionEvent tranEventPacket = PacketTransactionEvent.forRead(header, server);
    tranEventPacket.read(dataInput);
    
    return tranEventPacket.getEvent();
  }
}
