package com.avaje.ebeaninternal.server.cluster;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet
{
  public static final short TYPE_MESSAGES = 1;
  public static final short TYPE_TRANSEVENT = 2;
  protected short packetType;
  protected long packetId;
  protected long timestamp;
  protected String serverName;
  protected ByteArrayOutputStream buffer;
  protected DataOutputStream dataOut;
  protected byte[] bytes;
  private int messageCount;
  private int resendCount;
  
  public static Packet forWrite(short packetType, long packetId, long timestamp, String serverName)
    throws IOException
  {
    return new Packet(true, packetType, packetId, timestamp, serverName);
  }
  
  public static Packet readHeader(DataInput dataInput)
    throws IOException
  {
    short packetType = dataInput.readShort();
    long packetId = dataInput.readLong();
    long timestamp = dataInput.readLong();
    String serverName = dataInput.readUTF();
    
    return new Packet(false, packetType, packetId, timestamp, serverName);
  }
  
  protected Packet(boolean write, short packetType, long packetId, long timestamp, String serverName)
    throws IOException
  {
    this.packetType = packetType;
    this.packetId = packetId;
    this.timestamp = timestamp;
    this.serverName = serverName;
    if (write)
    {
      this.buffer = new ByteArrayOutputStream();
      this.dataOut = new DataOutputStream(this.buffer);
      writeHeader();
    }
    else
    {
      this.buffer = null;
      this.dataOut = null;
    }
  }
  
  private void writeHeader()
    throws IOException
  {
    this.dataOut.writeShort(this.packetType);
    this.dataOut.writeLong(this.packetId);
    this.dataOut.writeLong(this.timestamp);
    this.dataOut.writeUTF(this.serverName);
  }
  
  public int incrementResendCount()
  {
    return this.resendCount++;
  }
  
  public short getPacketType()
  {
    return this.packetType;
  }
  
  public long getPacketId()
  {
    return this.packetId;
  }
  
  public long getTimestamp()
  {
    return this.timestamp;
  }
  
  public String getServerName()
  {
    return this.serverName;
  }
  
  public void writeEof()
    throws IOException
  {
    this.dataOut.writeBoolean(false);
  }
  
  public void read(DataInput dataInput)
    throws IOException
  {
    boolean more = dataInput.readBoolean();
    while (more)
    {
      int msgType = dataInput.readInt();
      readMessage(dataInput, msgType);
      
      more = dataInput.readBoolean();
    }
  }
  
  protected void readMessage(DataInput dataInput, int msgType)
    throws IOException
  {}
  
  public boolean writeBinaryMessage(BinaryMessage msg, int maxPacketSize)
    throws IOException
  {
    byte[] bytes = msg.getByteArray();
    if ((this.messageCount > 0) && (bytes.length + this.buffer.size() > maxPacketSize))
    {
      this.dataOut.writeBoolean(false);
      return false;
    }
    this.messageCount += 1;
    
    this.dataOut.writeBoolean(true);
    this.dataOut.write(bytes);
    return true;
  }
  
  public int getSize()
  {
    return getBytes().length;
  }
  
  public byte[] getBytes()
  {
    if (this.bytes == null)
    {
      this.bytes = this.buffer.toByteArray();
      this.buffer = null;
      this.dataOut = null;
    }
    return this.bytes;
  }
}
