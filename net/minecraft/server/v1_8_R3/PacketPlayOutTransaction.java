package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutTransaction
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private short b;
  private boolean c;
  
  public PacketPlayOutTransaction() {}
  
  public PacketPlayOutTransaction(int ☃, short ☃, boolean ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readUnsignedByte();
    this.b = ☃.readShort();
    this.c = ☃.readBoolean();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.a);
    ☃.writeShort(this.b);
    ☃.writeBoolean(this.c);
  }
}
