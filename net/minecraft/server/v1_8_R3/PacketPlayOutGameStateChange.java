package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutGameStateChange
  implements Packet<PacketListenerPlayOut>
{
  public static final String[] a = { "tile.bed.notValid" };
  private int b;
  private float c;
  
  public PacketPlayOutGameStateChange() {}
  
  public PacketPlayOutGameStateChange(int ☃, float ☃)
  {
    this.b = ☃;
    this.c = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.b = ☃.readUnsignedByte();
    this.c = ☃.readFloat();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.b);
    ☃.writeFloat(this.c);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
