package com.avaje.ebeaninternal.server.cluster;

import com.avaje.ebeaninternal.server.cluster.mcast.Message;
import com.avaje.ebeaninternal.server.transaction.RemoteTransactionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketWriter
{
  private final PacketIdGenerator idGenerator;
  private final PacketBuilder messagesPacketBuilder;
  private final PacketBuilder transEventPacketBuilder;
  
  public PacketWriter(int maxPacketSize)
  {
    this.idGenerator = new PacketIdGenerator(null);
    this.messagesPacketBuilder = new PacketBuilder(maxPacketSize, this.idGenerator, new MessagesPacketFactory(null), null);
    this.transEventPacketBuilder = new PacketBuilder(maxPacketSize, this.idGenerator, new TransPacketFactory(null), null);
  }
  
  public long currentPacketId()
  {
    return this.idGenerator.currentPacketId();
  }
  
  public List<Packet> write(boolean requiresAck, List<? extends Message> messages)
    throws IOException
  {
    BinaryMessageList binaryMsgList = new BinaryMessageList();
    for (int i = 0; i < messages.size(); i++)
    {
      Message message = (Message)messages.get(i);
      message.writeBinaryMessage(binaryMsgList);
    }
    return this.messagesPacketBuilder.write(requiresAck, binaryMsgList, "");
  }
  
  public List<Packet> write(RemoteTransactionEvent transEvent)
    throws IOException
  {
    BinaryMessageList messageList = new BinaryMessageList();
    
    transEvent.writeBinaryMessage(messageList);
    
    return this.transEventPacketBuilder.write(true, messageList, transEvent.getServerName());
  }
  
  private static class PacketIdGenerator
  {
    long packetIdCounter;
    
    public long nextPacketId()
    {
      return ++this.packetIdCounter;
    }
    
    public long currentPacketId()
    {
      return this.packetIdCounter;
    }
  }
  
  static abstract interface PacketFactory
  {
    public abstract Packet createPacket(long paramLong1, long paramLong2, String paramString)
      throws IOException;
  }
  
  private static class TransPacketFactory
    implements PacketWriter.PacketFactory
  {
    public Packet createPacket(long packetId, long timestamp, String serverName)
      throws IOException
    {
      return PacketTransactionEvent.forWrite(packetId, timestamp, serverName);
    }
  }
  
  private static class MessagesPacketFactory
    implements PacketWriter.PacketFactory
  {
    public Packet createPacket(long packetId, long timestamp, String serverName)
      throws IOException
    {
      return PacketMessages.forWrite(packetId, timestamp, serverName);
    }
  }
  
  private static class PacketBuilder
  {
    private final PacketWriter.PacketIdGenerator idGenerator;
    private final PacketWriter.PacketFactory packetFactory;
    private final int maxPacketSize;
    
    private PacketBuilder(int maxPacketSize, PacketWriter.PacketIdGenerator idGenerator, PacketWriter.PacketFactory packetFactory)
    {
      this.maxPacketSize = maxPacketSize;
      this.idGenerator = idGenerator;
      this.packetFactory = packetFactory;
    }
    
    private List<Packet> write(boolean requiresAck, BinaryMessageList messageList, String serverName)
      throws IOException
    {
      List<BinaryMessage> list = messageList.getList();
      
      ArrayList<Packet> packets = new ArrayList(1);
      
      long timestamp = System.currentTimeMillis();
      
      long packetId = requiresAck ? this.idGenerator.nextPacketId() : 0L;
      Packet p = this.packetFactory.createPacket(packetId, timestamp, serverName);
      
      packets.add(p);
      for (int i = 0; i < list.size(); i++)
      {
        BinaryMessage binMsg = (BinaryMessage)list.get(i);
        if (!p.writeBinaryMessage(binMsg, this.maxPacketSize))
        {
          packetId = requiresAck ? this.idGenerator.nextPacketId() : 0L;
          p = this.packetFactory.createPacket(packetId, timestamp, serverName);
          packets.add(p);
          p.writeBinaryMessage(binMsg, this.maxPacketSize);
        }
      }
      p.writeEof();
      
      return packets;
    }
  }
}
