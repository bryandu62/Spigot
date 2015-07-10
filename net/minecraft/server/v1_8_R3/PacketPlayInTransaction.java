package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInTransaction
  implements Packet<PacketListenerPlayIn>
{
  private int a;
  private short b;
  private boolean c;
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readByte();
    this.b = ☃.readShort();
    this.c = (☃.readByte() != 0);
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.a);
    ☃.writeShort(this.b);
    ☃.writeByte(this.c ? 1 : 0);
  }
  
  public int a()
  {
    return this.a;
  }
  
  public short b()
  {
    return this.b;
  }
}
