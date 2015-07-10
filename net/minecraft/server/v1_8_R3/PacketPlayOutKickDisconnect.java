package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutKickDisconnect
  implements Packet<PacketListenerPlayOut>
{
  private IChatBaseComponent a;
  
  public PacketPlayOutKickDisconnect() {}
  
  public PacketPlayOutKickDisconnect(IChatBaseComponent ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.d();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
