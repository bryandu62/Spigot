package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutAttachEntity
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private int b;
  private int c;
  
  public PacketPlayOutAttachEntity() {}
  
  public PacketPlayOutAttachEntity(int ☃, Entity ☃, Entity ☃)
  {
    this.a = ☃;
    this.b = ☃.getId();
    this.c = (☃ != null ? ☃.getId() : -1);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.b = ☃.readInt();
    this.c = ☃.readInt();
    this.a = ☃.readUnsignedByte();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeInt(this.b);
    ☃.writeInt(this.c);
    ☃.writeByte(this.a);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
