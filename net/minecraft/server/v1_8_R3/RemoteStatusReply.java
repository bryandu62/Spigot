package net.minecraft.server.v1_8_R3;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RemoteStatusReply
{
  private ByteArrayOutputStream buffer;
  private DataOutputStream stream;
  
  public RemoteStatusReply(int ☃)
  {
    this.buffer = new ByteArrayOutputStream(☃);
    this.stream = new DataOutputStream(this.buffer);
  }
  
  public void a(byte[] ☃)
    throws IOException
  {
    this.stream.write(☃, 0, ☃.length);
  }
  
  public void a(String ☃)
    throws IOException
  {
    this.stream.writeBytes(☃);
    this.stream.write(0);
  }
  
  public void a(int ☃)
    throws IOException
  {
    this.stream.write(☃);
  }
  
  public void a(short ☃)
    throws IOException
  {
    this.stream.writeShort(Short.reverseBytes(☃));
  }
  
  public byte[] a()
  {
    return this.buffer.toByteArray();
  }
  
  public void b()
  {
    this.buffer.reset();
  }
}
