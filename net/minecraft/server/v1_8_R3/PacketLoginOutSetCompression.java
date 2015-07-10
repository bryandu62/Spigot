package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketLoginOutSetCompression
  implements Packet<PacketLoginOutListener>
{
  private int a;
  
  public PacketLoginOutSetCompression() {}
  
  public PacketLoginOutSetCompression(int ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
  }
  
  public void a(PacketLoginOutListener ☃)
  {
    ☃.a(this);
  }
}
