package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;

public class PacketPlayOutExplosion
  implements Packet<PacketListenerPlayOut>
{
  private double a;
  private double b;
  private double c;
  private float d;
  private List<BlockPosition> e;
  private float f;
  private float g;
  private float h;
  
  public PacketPlayOutExplosion() {}
  
  public PacketPlayOutExplosion(double ☃, double ☃, double ☃, float ☃, List<BlockPosition> ☃, Vec3D ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
    this.e = Lists.newArrayList(☃);
    if (☃ != null)
    {
      this.f = ((float)☃.a);
      this.g = ((float)☃.b);
      this.h = ((float)☃.c);
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readFloat();
    this.b = ☃.readFloat();
    this.c = ☃.readFloat();
    this.d = ☃.readFloat();
    int ☃ = ☃.readInt();
    
    this.e = Lists.newArrayListWithCapacity(☃);
    
    int ☃ = (int)this.a;
    int ☃ = (int)this.b;
    int ☃ = (int)this.c;
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      int ☃ = ☃.readByte() + ☃;
      int ☃ = ☃.readByte() + ☃;
      int ☃ = ☃.readByte() + ☃;
      this.e.add(new BlockPosition(☃, ☃, ☃));
    }
    this.f = ☃.readFloat();
    this.g = ☃.readFloat();
    this.h = ☃.readFloat();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeFloat((float)this.a);
    ☃.writeFloat((float)this.b);
    ☃.writeFloat((float)this.c);
    ☃.writeFloat(this.d);
    ☃.writeInt(this.e.size());
    
    int ☃ = (int)this.a;
    int ☃ = (int)this.b;
    int ☃ = (int)this.c;
    for (BlockPosition ☃ : this.e)
    {
      int ☃ = ☃.getX() - ☃;
      int ☃ = ☃.getY() - ☃;
      int ☃ = ☃.getZ() - ☃;
      ☃.writeByte(☃);
      ☃.writeByte(☃);
      ☃.writeByte(☃);
    }
    ☃.writeFloat(this.f);
    ☃.writeFloat(this.g);
    ☃.writeFloat(this.h);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
