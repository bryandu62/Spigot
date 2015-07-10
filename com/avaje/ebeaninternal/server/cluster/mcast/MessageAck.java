package com.avaje.ebeaninternal.server.cluster.mcast;

import com.avaje.ebeaninternal.server.cluster.BinaryMessage;
import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessageAck
  implements Message
{
  private final String toHostPort;
  private final long gotAllPacketId;
  
  public MessageAck(String toHostPort, long gotAllPacketId)
  {
    this.toHostPort = toHostPort;
    this.gotAllPacketId = gotAllPacketId;
  }
  
  public String toString()
  {
    return "Ack " + this.toHostPort + " " + this.gotAllPacketId;
  }
  
  public boolean isControlMessage()
  {
    return false;
  }
  
  public String getToHostPort()
  {
    return this.toHostPort;
  }
  
  public long getGotAllPacketId()
  {
    return this.gotAllPacketId;
  }
  
  public static MessageAck readBinaryMessage(DataInput dataInput)
    throws IOException
  {
    String hostPort = dataInput.readUTF();
    long gotAllPacketId = dataInput.readLong();
    return new MessageAck(hostPort, gotAllPacketId);
  }
  
  public void writeBinaryMessage(BinaryMessageList msgList)
    throws IOException
  {
    BinaryMessage m = new BinaryMessage(this.toHostPort.length() * 2 + 20);
    
    DataOutputStream os = m.getOs();
    os.writeInt(8);
    os.writeUTF(this.toHostPort);
    os.writeLong(this.gotAllPacketId);
    os.flush();
    
    msgList.add(m);
  }
}
