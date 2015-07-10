package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.UUID;

public class PacketPlayInSpectate
  implements Packet<PacketListenerPlayIn>
{
  private UUID a;
  
  public PacketPlayInSpectate() {}
  
  public PacketPlayInSpectate(UUID ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.g();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
  }
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public Entity a(WorldServer ☃)
  {
    return ☃.getEntity(this.a);
  }
}
