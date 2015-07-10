package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInSetCreativeSlot
  implements Packet<PacketListenerPlayIn>
{
  private int slot;
  private ItemStack b;
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.slot = ☃.readShort();
    this.b = ☃.i();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeShort(this.slot);
    ☃.a(this.b);
  }
  
  public int a()
  {
    return this.slot;
  }
  
  public ItemStack getItemStack()
  {
    return this.b;
  }
}
