package com.avaje.ebeaninternal.server.cluster.mcast;

import com.avaje.ebeaninternal.server.cluster.BinaryMessage;
import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageResend
  implements Message
{
  private final String toHostPort;
  private final List<Long> resendPacketIds;
  
  public MessageResend(String toHostPort, List<Long> resendPacketIds)
  {
    this.toHostPort = toHostPort;
    this.resendPacketIds = resendPacketIds;
  }
  
  public MessageResend(String toHostPort)
  {
    this(toHostPort, new ArrayList(4));
  }
  
  public String toString()
  {
    return "Resend " + this.toHostPort + " " + this.resendPacketIds;
  }
  
  public boolean isControlMessage()
  {
    return false;
  }
  
  public String getToHostPort()
  {
    return this.toHostPort;
  }
  
  public void add(long packetId)
  {
    this.resendPacketIds.add(Long.valueOf(packetId));
  }
  
  public List<Long> getResendPacketIds()
  {
    return this.resendPacketIds;
  }
  
  public static MessageResend readBinaryMessage(DataInput dataInput)
    throws IOException
  {
    String hostPort = dataInput.readUTF();
    
    MessageResend msg = new MessageResend(hostPort);
    
    int numberOfPacketIds = dataInput.readInt();
    for (int i = 0; i < numberOfPacketIds; i++)
    {
      long packetId = dataInput.readLong();
      msg.add(packetId);
    }
    return msg;
  }
  
  public void writeBinaryMessage(BinaryMessageList msgList)
    throws IOException
  {
    BinaryMessage m = new BinaryMessage(this.toHostPort.length() * 2 + 20);
    
    DataOutputStream os = m.getOs();
    os.writeInt(9);
    os.writeUTF(this.toHostPort);
    os.writeInt(this.resendPacketIds.size());
    for (int i = 0; i < this.resendPacketIds.size(); i++)
    {
      Long packetId = (Long)this.resendPacketIds.get(i);
      os.writeLong(packetId.longValue());
    }
    os.flush();
    msgList.add(m);
  }
}
