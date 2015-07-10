package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInKeepAlive
  implements Packet<PacketListenerPlayIn>
{
  private int a;
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
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
  
  public int a()
  {
    return this.a;
  }
}
