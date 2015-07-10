package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutBlockBreakAnimation
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private BlockPosition b;
  private int c;
  
  public PacketPlayOutBlockBreakAnimation() {}
  
  public PacketPlayOutBlockBreakAnimation(int ☃, BlockPosition ☃, int ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.c();
    this.c = ☃.readUnsignedByte();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.a(this.b);
    ☃.writeByte(this.c);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
