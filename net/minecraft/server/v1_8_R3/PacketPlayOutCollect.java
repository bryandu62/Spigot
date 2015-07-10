package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutCollect
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private int b;
  
  public PacketPlayOutCollect() {}
  
  public PacketPlayOutCollect(int ☃, int ☃)
  {
    this.a = ☃;
    this.b = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.e();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.b(this.b);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
