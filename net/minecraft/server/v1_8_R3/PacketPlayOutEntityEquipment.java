package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutEntityEquipment
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private int b;
  private ItemStack c;
  
  public PacketPlayOutEntityEquipment() {}
  
  public PacketPlayOutEntityEquipment(int ☃, int ☃, ItemStack ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = (☃ == null ? null : ☃.cloneItemStack());
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.readShort();
    this.c = ☃.i();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.writeShort(this.b);
    ☃.a(this.c);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
