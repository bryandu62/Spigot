package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutSetCompression
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
