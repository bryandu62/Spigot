package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutHeldItemSlot
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  
  public PacketPlayOutHeldItemSlot() {}
  
  public PacketPlayOutHeldItemSlot(int ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readByte();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.a);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
