package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutUpdateHealth
  implements Packet<PacketListenerPlayOut>
{
  private float a;
  private int b;
  private float c;
  
  public PacketPlayOutUpdateHealth() {}
  
  public PacketPlayOutUpdateHealth(float ☃, int ☃, float ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readFloat();
    this.b = ☃.e();
    this.c = ☃.readFloat();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeFloat(this.a);
    ☃.b(this.b);
    ☃.writeFloat(this.c);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
