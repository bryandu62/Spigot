package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInEnchantItem
  implements Packet<PacketListenerPlayIn>
{
  private int a;
  private int b;
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readByte();
    this.b = ☃.readByte();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.a);
    ☃.writeByte(this.b);
  }
  
  public int a()
  {
    return this.a;
  }
  
  public int b()
  {
    return this.b;
  }
}
