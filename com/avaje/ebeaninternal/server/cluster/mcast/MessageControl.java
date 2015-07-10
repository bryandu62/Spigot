package com.avaje.ebeaninternal.server.cluster.mcast;

import com.avaje.ebeaninternal.server.cluster.BinaryMessage;
import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessageControl
  implements Message
{
  public static final short TYPE_JOIN = 1;
  public static final short TYPE_LEAVE = 2;
  public static final short TYPE_PING = 3;
  public static final short TYPE_JOINRESPONSE = 7;
  public static final short TYPE_PINGRESPONSE = 8;
  private final short controlType;
  private final String fromHostPort;
  
  public static MessageControl readBinaryMessage(DataInput dataInput)
    throws IOException
  {
    short controlType = dataInput.readShort();
    String hostPort = dataInput.readUTF();
    return new MessageControl(controlType, hostPort);
  }
  
  public MessageControl(short controlType, String helloFromHostPort)
  {
    this.controlType = controlType;
    this.fromHostPort = helloFromHostPort;
  }
  
  public String toString()
  {
    switch (this.controlType)
    {
    case 1: 
      return "Join " + this.fromHostPort;
    case 2: 
      return "Leave " + this.fromHostPort;
    case 3: 
      return "Ping " + this.fromHostPort;
    case 8: 
      return "PingResponse " + this.fromHostPort;
    }
    throw new RuntimeException("Invalid controlType " + this.controlType);
  }
  
  public boolean isControlMessage()
  {
    return true;
  }
  
  public short getControlType()
  {
    return this.controlType;
  }
  
  public String getToHostPort()
  {
    return "*";
  }
  
  public String getFromHostPort()
  {
    return this.fromHostPort;
  }
  
  public void writeBinaryMessage(BinaryMessageList msgList)
    throws IOException
  {
    BinaryMessage m = new BinaryMessage(this.fromHostPort.length() * 2 + 10);
    
    DataOutputStream os = m.getOs();
    os.writeInt(0);
    os.writeShort(this.controlType);
    os.writeUTF(this.fromHostPort);
    os.flush();
    
    msgList.add(m);
  }
}
