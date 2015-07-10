package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.List;

public class PacketPlayOutWindowItems
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private ItemStack[] b;
  
  public PacketPlayOutWindowItems() {}
  
  public PacketPlayOutWindowItems(int ☃, List<ItemStack> ☃)
  {
    this.a = ☃;
    this.b = new ItemStack[☃.size()];
    for (int ☃ = 0; ☃ < this.b.length; ☃++)
    {
      ItemStack ☃ = (ItemStack)☃.get(☃);
      this.b[☃] = (☃ == null ? null : ☃.cloneItemStack());
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readUnsignedByte();
    int ☃ = ☃.readShort();
    this.b = new ItemStack[☃];
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      this.b[☃] = ☃.i();
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.a);
    ☃.writeShort(this.b.length);
    for (ItemStack ☃ : this.b) {
      ☃.a(☃);
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
