package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutWorldEvent
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private BlockPosition b;
  private int c;
  private boolean d;
  
  public PacketPlayOutWorldEvent() {}
  
  public PacketPlayOutWorldEvent(int ☃, BlockPosition ☃, int ☃, boolean ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readInt();
    this.b = ☃.c();
    this.c = ☃.readInt();
    this.d = ☃.readBoolean();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeInt(this.a);
    ☃.a(this.b);
    ☃.writeInt(this.c);
    ☃.writeBoolean(this.d);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
