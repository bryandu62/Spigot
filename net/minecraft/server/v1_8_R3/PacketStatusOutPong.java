package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketStatusOutPong
  implements Packet<PacketStatusOutListener>
{
  private long a;
  
  public PacketStatusOutPong() {}
  
  public PacketStatusOutPong(long ☃)
  {
    this.a = ☃;
  }
  
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
  
  public void a(PacketStatusOutListener ☃)
  {
    ☃.a(this);
  }
}
