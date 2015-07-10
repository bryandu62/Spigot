package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutSetSlot
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private int b;
  private ItemStack c;
  
  public PacketPlayOutSetSlot() {}
  
  public PacketPlayOutSetSlot(int ☃, int ☃, ItemStack ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = (☃ == null ? null : ☃.cloneItemStack());
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readByte();
    this.b = ☃.readShort();
    this.c = ☃.i();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.a);
    ☃.writeShort(this.b);
    ☃.a(this.c);
  }
}
