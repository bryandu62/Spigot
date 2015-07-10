package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class PacketPlayOutPosition
  implements Packet<PacketListenerPlayOut>
{
  private double a;
  private double b;
  private double c;
  private float d;
  private float e;
  private Set<EnumPlayerTeleportFlags> f;
  
  public PacketPlayOutPosition() {}
  
  public PacketPlayOutPosition(double ☃, double ☃, double ☃, float ☃, float ☃, Set<EnumPlayerTeleportFlags> ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
    this.e = ☃;
    this.f = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readDouble();
    this.b = ☃.readDouble();
    this.c = ☃.readDouble();
    this.d = ☃.readFloat();
    this.e = ☃.readFloat();
    this.f = EnumPlayerTeleportFlags.a(☃.readUnsignedByte());
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeDouble(this.a);
    ☃.writeDouble(this.b);
    ☃.writeDouble(this.c);
    ☃.writeFloat(this.d);
    ☃.writeFloat(this.e);
    ☃.writeByte(EnumPlayerTeleportFlags.a(this.f));
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public static enum EnumPlayerTeleportFlags
  {
    private int f;
    
    private EnumPlayerTeleportFlags(int ☃)
    {
      this.f = ☃;
    }
    
    private int a()
    {
      return 1 << this.f;
    }
    
    private boolean b(int ☃)
    {
      return (☃ & a()) == a();
    }
    
    public static Set<EnumPlayerTeleportFlags> a(int ☃)
    {
      Set<EnumPlayerTeleportFlags> ☃ = EnumSet.noneOf(EnumPlayerTeleportFlags.class);
      for (EnumPlayerTeleportFlags ☃ : values()) {
        if (☃.b(☃)) {
          ☃.add(☃);
        }
      }
      return ☃;
    }
    
    public static int a(Set<EnumPlayerTeleportFlags> ☃)
    {
      int ☃ = 0;
      for (EnumPlayerTeleportFlags ☃ : ☃) {
        ☃ |= ☃.a();
      }
      return ☃;
    }
  }
}
