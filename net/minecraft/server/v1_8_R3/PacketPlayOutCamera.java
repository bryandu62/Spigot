package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutCamera
  implements Packet<PacketListenerPlayOut>
{
  public int a;
  
  public PacketPlayOutCamera() {}
  
  public PacketPlayOutCamera(Entity ☃)
  {
    this.a = ☃.getId();
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
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
