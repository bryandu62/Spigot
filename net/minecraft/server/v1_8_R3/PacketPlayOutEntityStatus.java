package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutEntityStatus
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private byte b;
  
  public PacketPlayOutEntityStatus() {}
  
  public PacketPlayOutEntityStatus(Entity ☃, byte ☃)
  {
    this.a = ☃.getId();
    this.b = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readInt();
    this.b = ☃.readByte();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeInt(this.a);
    ☃.writeByte(this.b);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
