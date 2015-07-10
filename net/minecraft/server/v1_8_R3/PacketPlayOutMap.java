package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.Collection;

public class PacketPlayOutMap
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private byte b;
  private MapIcon[] c;
  private int d;
  private int e;
  private int f;
  private int g;
  private byte[] h;
  
  public PacketPlayOutMap() {}
  
  public PacketPlayOutMap(int ☃, byte ☃, Collection<MapIcon> ☃, byte[] ☃, int ☃, int ☃, int ☃, int ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ((MapIcon[])☃.toArray(new MapIcon[☃.size()]));
    this.d = ☃;
    this.e = ☃;
    this.f = ☃;
    this.g = ☃;
    
    this.h = new byte[☃ * ☃];
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++) {
        this.h[(☃ + ☃ * ☃)] = ☃[(☃ + ☃ + (☃ + ☃) * 128)];
      }
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.readByte();
    this.c = new MapIcon[☃.e()];
    for (int ☃ = 0; ☃ < this.c.length; ☃++)
    {
      short ☃ = (short)☃.readByte();
      this.c[☃] = new MapIcon((byte)(☃ >> 4 & 0xF), ☃.readByte(), ☃.readByte(), (byte)(☃ & 0xF));
    }
    this.f = ☃.readUnsignedByte();
    if (this.f > 0)
    {
      this.g = ☃.readUnsignedByte();
      this.d = ☃.readUnsignedByte();
      this.e = ☃.readUnsignedByte();
      this.h = ☃.a();
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.writeByte(this.b);
    ☃.b(this.c.length);
    for (MapIcon ☃ : this.c)
    {
      ☃.writeByte((☃.getType() & 0xF) << 4 | ☃.getRotation() & 0xF);
      ☃.writeByte(☃.getX());
      ☃.writeByte(☃.getY());
    }
    ☃.writeByte(this.f);
    if (this.f > 0)
    {
      ☃.writeByte(this.g);
      ☃.writeByte(this.d);
      ☃.writeByte(this.e);
      ☃.a(this.h);
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
