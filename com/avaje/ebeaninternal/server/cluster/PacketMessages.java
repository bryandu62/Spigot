package com.avaje.ebeaninternal.server.cluster;

import com.avaje.ebeaninternal.server.cluster.mcast.Message;
import com.avaje.ebeaninternal.server.cluster.mcast.MessageAck;
import com.avaje.ebeaninternal.server.cluster.mcast.MessageControl;
import com.avaje.ebeaninternal.server.cluster.mcast.MessageResend;
import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketMessages
  extends Packet
{
  private final ArrayList<Message> messages;
  
  public static PacketMessages forWrite(long packetId, long timestamp, String serverName)
    throws IOException
  {
    return new PacketMessages(true, packetId, timestamp, serverName);
  }
  
  public static PacketMessages forRead(Packet header)
    throws IOException
  {
    return new PacketMessages(header);
  }
  
  private PacketMessages(boolean write, long packetId, long timestamp, String serverName)
    throws IOException
  {
    super(write, (short)1, packetId, timestamp, serverName);
    this.messages = null;
  }
  
  private PacketMessages(Packet header)
    throws IOException
  {
    super(false, (short)1, header.packetId, header.timestamp, header.serverName);
    this.messages = new ArrayList();
  }
  
  public List<Message> getMessages()
  {
    return this.messages;
  }
  
  protected void readMessage(DataInput dataInput, int msgType)
    throws IOException
  {
    switch (msgType)
    {
    case 0: 
      this.messages.add(MessageControl.readBinaryMessage(dataInput));
      break;
    case 8: 
      this.messages.add(MessageAck.readBinaryMessage(dataInput));
      break;
    case 9: 
      this.messages.add(MessageResend.readBinaryMessage(dataInput));
      break;
    default: 
      throw new RuntimeException("Invalid Transaction msgType " + msgType);
    }
  }
}
