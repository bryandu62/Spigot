package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutExperience
  implements Packet<PacketListenerPlayOut>
{
  private float a;
  private int b;
  private int c;
  
  public PacketPlayOutExperience() {}
  
  public PacketPlayOutExperience(float ☃, int ☃, int ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readFloat();
    this.c = ☃.e();
    this.b = ☃.e();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeFloat(this.a);
    ☃.b(this.c);
    ☃.b(this.b);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
