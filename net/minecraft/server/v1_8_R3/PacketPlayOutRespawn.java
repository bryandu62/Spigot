package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutRespawn
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private EnumDifficulty b;
  private WorldSettings.EnumGamemode c;
  private WorldType d;
  
  public PacketPlayOutRespawn() {}
  
  public PacketPlayOutRespawn(int ☃, EnumDifficulty ☃, WorldType ☃, WorldSettings.EnumGamemode ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readInt();
    this.b = EnumDifficulty.getById(☃.readUnsignedByte());
    this.c = WorldSettings.EnumGamemode.getById(☃.readUnsignedByte());
    this.d = WorldType.getType(☃.c(16));
    if (this.d == null) {
      this.d = WorldType.NORMAL;
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeInt(this.a);
    ☃.writeByte(this.b.a());
    ☃.writeByte(this.c.getId());
    ☃.a(this.d.name());
  }
}
