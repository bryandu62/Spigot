package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutSpawnPosition
  implements Packet<PacketListenerPlayOut>
{
  public BlockPosition position;
  
  public PacketPlayOutSpawnPosition() {}
  
  public PacketPlayOutSpawnPosition(BlockPosition ☃)
  {
    this.position = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.position = ☃.c();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.position);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
