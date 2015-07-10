package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutOpenSignEditor
  implements Packet<PacketListenerPlayOut>
{
  private BlockPosition a;
  
  public PacketPlayOutOpenSignEditor() {}
  
  public PacketPlayOutOpenSignEditor(BlockPosition ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
  }
}
