package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutBlockAction
  implements Packet<PacketListenerPlayOut>
{
  private BlockPosition a;
  private int b;
  private int c;
  private Block d;
  
  public PacketPlayOutBlockAction() {}
  
  public PacketPlayOutBlockAction(BlockPosition ☃, Block ☃, int ☃, int ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c();
    this.b = ☃.readUnsignedByte();
    this.c = ☃.readUnsignedByte();
    this.d = Block.getById(☃.e() & 0xFFF);
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.writeByte(this.b);
    ☃.writeByte(this.c);
    ☃.b(Block.getId(this.d) & 0xFFF);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
