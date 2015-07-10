package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutWindowData
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private int b;
  private int c;
  
  public PacketPlayOutWindowData() {}
  
  public PacketPlayOutWindowData(int ☃, int ☃, int ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readUnsignedByte();
    this.b = ☃.readShort();
    this.c = ☃.readShort();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.a);
    ☃.writeShort(this.b);
    ☃.writeShort(this.c);
  }
}
