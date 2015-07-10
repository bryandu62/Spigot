package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutResourcePackSend
  implements Packet<PacketListenerPlayOut>
{
  private String a;
  private String b;
  
  public PacketPlayOutResourcePackSend() {}
  
  public PacketPlayOutResourcePackSend(String ☃, String ☃)
  {
    this.a = ☃;
    this.b = ☃;
    if (☃.length() > 40) {
      throw new IllegalArgumentException("Hash is too long (max 40, was " + ☃.length() + ")");
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c(32767);
    this.b = ☃.c(40);
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
