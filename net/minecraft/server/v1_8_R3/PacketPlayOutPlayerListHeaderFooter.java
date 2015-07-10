package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutPlayerListHeaderFooter
  implements Packet<PacketListenerPlayOut>
{
  private IChatBaseComponent a;
  private IChatBaseComponent b;
  
  public PacketPlayOutPlayerListHeaderFooter() {}
  
  public PacketPlayOutPlayerListHeaderFooter(IChatBaseComponent ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.d();
    this.b = ☃.d();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.a(this.b);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
