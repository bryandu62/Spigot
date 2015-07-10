package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutEntityHeadRotation
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private byte b;
  
  public PacketPlayOutEntityHeadRotation() {}
  
  public PacketPlayOutEntityHeadRotation(Entity ☃, byte ☃)
  {
    this.a = ☃.getId();
    this.b = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.readByte();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.writeByte(this.b);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
