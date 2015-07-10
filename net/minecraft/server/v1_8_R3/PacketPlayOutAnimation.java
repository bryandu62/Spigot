package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutAnimation
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private int b;
  
  public PacketPlayOutAnimation() {}
  
  public PacketPlayOutAnimation(Entity ☃, int ☃)
  {
    this.a = ☃.getId();
    this.b = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.readUnsignedByte();
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
