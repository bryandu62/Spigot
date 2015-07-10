package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutBed
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private BlockPosition b;
  
  public PacketPlayOutBed() {}
  
  public PacketPlayOutBed(EntityHuman ☃, BlockPosition ☃)
  {
    this.a = ☃.getId();
    this.b = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.c();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.a(this.b);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
