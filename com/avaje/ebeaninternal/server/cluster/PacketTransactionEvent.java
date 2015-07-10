package com.avaje.ebeaninternal.server.cluster;

import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.TransactionEventTable.TableIUD;
import com.avaje.ebeaninternal.server.transaction.BeanDelta;
import com.avaje.ebeaninternal.server.transaction.BeanPersistIds;
import com.avaje.ebeaninternal.server.transaction.IndexEvent;
import com.avaje.ebeaninternal.server.transaction.RemoteTransactionEvent;
import java.io.DataInput;
import java.io.IOException;

public class PacketTransactionEvent
  extends Packet
{
  private final SpiEbeanServer server;
  private final RemoteTransactionEvent event;
  
  public static PacketTransactionEvent forWrite(long packetId, long timestamp, String serverName)
    throws IOException
  {
    return new PacketTransactionEvent(true, packetId, timestamp, serverName);
  }
  
  private PacketTransactionEvent(boolean write, long packetId, long timestamp, String serverName)
    throws IOException
  {
    super(write, (short)2, packetId, timestamp, serverName);
    this.server = null;
    this.event = null;
  }
  
  private PacketTransactionEvent(Packet header, SpiEbeanServer server)
    throws IOException
  {
    super(false, (short)2, header.packetId, header.timestamp, header.serverName);
    this.server = server;
    this.event = new RemoteTransactionEvent(server);
  }
  
  public static PacketTransactionEvent forRead(Packet header, SpiEbeanServer server)
    throws IOException
  {
    return new PacketTransactionEvent(header, server);
  }
  
  public RemoteTransactionEvent getEvent()
  {
    return this.event;
  }
  
  protected void readMessage(DataInput dataInput, int msgType)
    throws IOException
  {
    switch (msgType)
    {
    case 1: 
      this.event.addBeanPersistIds(BeanPersistIds.readBinaryMessage(this.server, dataInput));
      break;
    case 2: 
      this.event.addTableIUD(TransactionEventTable.TableIUD.readBinaryMessage(dataInput));
      break;
    case 3: 
      this.event.addBeanDelta(BeanDelta.readBinaryMessage(this.server, dataInput));
      break;
    case 7: 
      this.event.addIndexEvent(IndexEvent.readBinaryMessage(dataInput));
      break;
    case 4: 
    case 5: 
    case 6: 
    default: 
      throw new RuntimeException("Invalid Transaction msgType " + msgType);
    }
  }
}
