package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketLoginOutDisconnect
  implements Packet<PacketLoginOutListener>
{
  private IChatBaseComponent a;
  
  public PacketLoginOutDisconnect() {}
  
  public PacketLoginOutDisconnect(IChatBaseComponent ☃)
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
  
  public void a(PacketLoginOutListener ☃)
  {
    ☃.a(this);
  }
}
