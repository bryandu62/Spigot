package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInWindowClick
  implements Packet<PacketListenerPlayIn>
{
  private int a;
  private int slot;
  private int button;
  private short d;
  private ItemStack item;
  private int shift;
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readByte();
    this.slot = ☃.readShort();
    this.button = ☃.readByte();
    this.d = ☃.readShort();
    this.shift = ☃.readByte();
    
    this.item = ☃.i();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.a);
    ☃.writeShort(this.slot);
    ☃.writeByte(this.button);
    ☃.writeShort(this.d);
    ☃.writeByte(this.shift);
    
    ☃.a(this.item);
  }
  
  public int a()
  {
    return this.a;
  }
  
  public int b()
  {
    return this.slot;
  }
  
  public int c()
  {
    return this.button;
  }
  
  public short d()
  {
    return this.d;
  }
  
  public ItemStack e()
  {
    return this.item;
  }
  
  public int f()
  {
    return this.shift;
  }
}
