package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutKeepAlive
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  
  public PacketPlayOutKeepAlive() {}
  
  public PacketPlayOutKeepAlive(int ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketListenerPlayOut ☃)
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
}
