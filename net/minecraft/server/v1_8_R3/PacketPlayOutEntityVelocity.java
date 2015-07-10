package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutEntityVelocity
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private int b;
  private int c;
  private int d;
  
  public PacketPlayOutEntityVelocity() {}
  
  public PacketPlayOutEntityVelocity(Entity ☃)
  {
    this(☃.getId(), ☃.motX, ☃.motY, ☃.motZ);
  }
  
  public PacketPlayOutEntityVelocity(int ☃, double ☃, double ☃, double ☃)
  {
    this.a = ☃;
    double ☃ = 3.9D;
    if (☃ < -☃) {
      ☃ = -☃;
    }
    if (☃ < -☃) {
      ☃ = -☃;
    }
    if (☃ < -☃) {
      ☃ = -☃;
    }
    if (☃ > ☃) {
      ☃ = ☃;
    }
    if (☃ > ☃) {
      ☃ = ☃;
    }
    if (☃ > ☃) {
      ☃ = ☃;
    }
    this.b = ((int)(☃ * 8000.0D));
    this.c = ((int)(☃ * 8000.0D));
    this.d = ((int)(☃ * 8000.0D));
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.readShort();
    this.c = ☃.readShort();
    this.d = ☃.readShort();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.writeShort(this.b);
    ☃.writeShort(this.c);
    ☃.writeShort(this.d);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
