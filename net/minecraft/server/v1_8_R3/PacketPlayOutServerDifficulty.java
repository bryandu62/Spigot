package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutServerDifficulty
  implements Packet<PacketListenerPlayOut>
{
  private EnumDifficulty a;
  private boolean b;
  
  public PacketPlayOutServerDifficulty() {}
  
  public PacketPlayOutServerDifficulty(EnumDifficulty ☃, boolean ☃)
  {
    this.a = ☃;
    this.b = ☃;
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = EnumDifficulty.getById(☃.readUnsignedByte());
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.a.a());
  }
}
