package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutUpdateTime
  implements Packet<PacketListenerPlayOut>
{
  private long a;
  private long b;
  
  public PacketPlayOutUpdateTime() {}
  
  public PacketPlayOutUpdateTime(long ☃, long ☃, boolean ☃)
  {
    this.a = ☃;
    this.b = ☃;
    if (!☃)
    {
      this.b = (-this.b);
      if (this.b == 0L) {
        this.b = -1L;
      }
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readLong();
    this.b = ☃.readLong();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeLong(this.a);
    ☃.writeLong(this.b);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
