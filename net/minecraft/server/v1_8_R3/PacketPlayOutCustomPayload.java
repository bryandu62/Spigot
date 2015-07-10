package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutCustomPayload
  implements Packet<PacketListenerPlayOut>
{
  private String a;
  private PacketDataSerializer b;
  
  public PacketPlayOutCustomPayload() {}
  
  public PacketPlayOutCustomPayload(String ☃, PacketDataSerializer ☃)
  {
    this.a = ☃;
    this.b = ☃;
    if (☃.writerIndex() > 1048576) {
      throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c(20);
    int ☃ = ☃.readableBytes();
    if ((☃ < 0) || (☃ > 1048576)) {
      throw new IOException("Payload may not be larger than 1048576 bytes");
    }
    this.b = new PacketDataSerializer(☃.readBytes(☃));
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.writeBytes(this.b);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
