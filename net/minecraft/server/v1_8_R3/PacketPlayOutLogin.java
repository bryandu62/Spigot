package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutLogin
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private boolean b;
  private WorldSettings.EnumGamemode c;
  private int d;
  private EnumDifficulty e;
  private int f;
  private WorldType g;
  private boolean h;
  
  public PacketPlayOutLogin() {}
  
  public PacketPlayOutLogin(int ☃, WorldSettings.EnumGamemode ☃, boolean ☃, int ☃, EnumDifficulty ☃, int ☃, WorldType ☃, boolean ☃)
  {
    this.a = ☃;
    this.d = ☃;
    this.e = ☃;
    this.c = ☃;
    this.f = ☃;
    this.b = ☃;
    this.g = ☃;
    this.h = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readInt();
    
    int ☃ = ☃.readUnsignedByte();
    this.b = ((☃ & 0x8) == 8);
    ☃ &= 0xFFFFFFF7;
    this.c = WorldSettings.EnumGamemode.getById(☃);
    
    this.d = ☃.readByte();
    this.e = EnumDifficulty.getById(☃.readUnsignedByte());
    this.f = ☃.readUnsignedByte();
    this.g = WorldType.getType(☃.c(16));
    if (this.g == null) {
      this.g = WorldType.NORMAL;
    }
    this.h = ☃.readBoolean();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeInt(this.a);
    int ☃ = this.c.getId();
    if (this.b) {
      ☃ |= 0x8;
    }
    ☃.writeByte(☃);
    ☃.writeByte(this.d);
    ☃.writeByte(this.e.a());
    ☃.writeByte(this.f);
    ☃.a(this.g.name());
    ☃.writeBoolean(this.h);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
