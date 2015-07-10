package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutCloseWindow
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  
  public PacketPlayOutCloseWindow() {}
  
  public PacketPlayOutCloseWindow(int ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readUnsignedByte();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.a);
  }
}
