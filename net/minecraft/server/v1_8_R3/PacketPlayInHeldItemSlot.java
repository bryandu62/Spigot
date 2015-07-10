package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInHeldItemSlot
  implements Packet<PacketListenerPlayIn>
{
  private int itemInHandIndex;
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.itemInHandIndex = ☃.readShort();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeShort(this.itemInHandIndex);
  }
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public int a()
  {
    return this.itemInHandIndex;
  }
}
