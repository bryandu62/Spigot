package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketStatusInPing
  implements Packet<PacketStatusInListener>
{
  private long a;
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readLong();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeLong(this.a);
  }
  
  public void a(PacketStatusInListener ☃)
  {
    ☃.a(this);
  }
  
  public long a()
  {
    return this.a;
  }
}
