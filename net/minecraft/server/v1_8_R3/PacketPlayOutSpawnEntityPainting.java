package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutSpawnEntityPainting
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private BlockPosition b;
  private EnumDirection c;
  private String d;
  
  public PacketPlayOutSpawnEntityPainting() {}
  
  public PacketPlayOutSpawnEntityPainting(EntityPainting ☃)
  {
    this.a = ☃.getId();
    this.b = ☃.getBlockPosition();
    this.c = ☃.direction;
    this.d = ☃.art.B;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.d = ☃.c(EntityPainting.EnumArt.A);
    this.b = ☃.c();
    this.c = EnumDirection.fromType2(☃.readUnsignedByte());
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.a(this.d);
    ☃.a(this.b);
    ☃.writeByte(this.c.b());
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
