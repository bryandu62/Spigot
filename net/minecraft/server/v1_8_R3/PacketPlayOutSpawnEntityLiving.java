package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.List;

public class PacketPlayOutSpawnEntityLiving
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
  private byte i;
  private byte j;
  private byte k;
  private DataWatcher l;
  private List<DataWatcher.WatchableObject> m;
  
  public PacketPlayOutSpawnEntityLiving() {}
  
  public PacketPlayOutSpawnEntityLiving(EntityLiving ☃)
  {
    this.a = ☃.getId();
    
    this.b = ((byte)EntityTypes.a(☃));
    this.c = MathHelper.floor(☃.locX * 32.0D);
    this.d = MathHelper.floor(☃.locY * 32.0D);
    this.e = MathHelper.floor(☃.locZ * 32.0D);
    this.i = ((byte)(int)(☃.yaw * 256.0F / 360.0F));
    this.j = ((byte)(int)(☃.pitch * 256.0F / 360.0F));
    this.k = ((byte)(int)(☃.aK * 256.0F / 360.0F));
    
    double ☃ = 3.9D;
    double ☃ = ☃.motX;
    double ☃ = ☃.motY;
    double ☃ = ☃.motZ;
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
    this.f = ((int)(☃ * 8000.0D));
    this.g = ((int)(☃ * 8000.0D));
    this.h = ((int)(☃ * 8000.0D));
    
    this.l = ☃.getDataWatcher();
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = (☃.readByte() & 0xFF);
    this.c = ☃.readInt();
    this.d = ☃.readInt();
    this.e = ☃.readInt();
    this.i = ☃.readByte();
    this.j = ☃.readByte();
    this.k = ☃.readByte();
    this.f = ☃.readShort();
    this.g = ☃.readShort();
    this.h = ☃.readShort();
    this.m = DataWatcher.b(☃);
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.writeByte(this.b & 0xFF);
    ☃.writeInt(this.c);
    ☃.writeInt(this.d);
    ☃.writeInt(this.e);
    ☃.writeByte(this.i);
    ☃.writeByte(this.j);
    ☃.writeByte(this.k);
    ☃.writeShort(this.f);
    ☃.writeShort(this.g);
    ☃.writeShort(this.h);
    this.l.a(☃);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
