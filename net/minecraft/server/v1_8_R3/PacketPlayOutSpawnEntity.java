package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutSpawnEntity
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private int b;
  private int c;
  private int d;
  private int e;
  private int f;
  private int g;
  private int h;
  private int i;
  private int j;
  private int k;
  
  public PacketPlayOutSpawnEntity() {}
  
  public PacketPlayOutSpawnEntity(Entity ☃, int ☃)
  {
    this(☃, ☃, 0);
  }
  
  public PacketPlayOutSpawnEntity(Entity ☃, int ☃, int ☃)
  {
    this.a = ☃.getId();
    this.b = MathHelper.floor(☃.locX * 32.0D);
    this.c = MathHelper.floor(☃.locY * 32.0D);
    this.d = MathHelper.floor(☃.locZ * 32.0D);
    this.h = MathHelper.d(☃.pitch * 256.0F / 360.0F);
    this.i = MathHelper.d(☃.yaw * 256.0F / 360.0F);
    this.j = ☃;
    this.k = ☃;
    if (☃ > 0)
    {
      double ☃ = ☃.motX;
      double ☃ = ☃.motY;
      double ☃ = ☃.motZ;
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
      this.e = ((int)(☃ * 8000.0D));
      this.f = ((int)(☃ * 8000.0D));
      this.g = ((int)(☃ * 8000.0D));
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.j = ☃.readByte();
    this.b = ☃.readInt();
    this.c = ☃.readInt();
    this.d = ☃.readInt();
    this.h = ☃.readByte();
    this.i = ☃.readByte();
    this.k = ☃.readInt();
    if (this.k > 0)
    {
      this.e = ☃.readShort();
      this.f = ☃.readShort();
      this.g = ☃.readShort();
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.writeByte(this.j);
    ☃.writeInt(this.b);
    ☃.writeInt(this.c);
    ☃.writeInt(this.d);
    ☃.writeByte(this.h);
    ☃.writeByte(this.i);
    ☃.writeInt(this.k);
    if (this.k > 0)
    {
      ☃.writeShort(this.e);
      ☃.writeShort(this.f);
      ☃.writeShort(this.g);
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public void a(int ☃)
  {
    this.b = ☃;
  }
  
  public void b(int ☃)
  {
    this.c = ☃;
  }
  
  public void c(int ☃)
  {
    this.d = ☃;
  }
  
  public void d(int ☃)
  {
    this.e = ☃;
  }
  
  public void e(int ☃)
  {
    this.f = ☃;
  }
  
  public void f(int ☃)
  {
    this.g = ☃;
  }
}
