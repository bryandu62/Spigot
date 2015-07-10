package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInCustomPayload
  implements Packet<PacketListenerPlayIn>
{
  private String a;
  private PacketDataSerializer b;
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c(20);
    int ☃ = ☃.readableBytes();
    if ((☃ < 0) || (☃ > 32767)) {
      throw new IOException("Payload may not be larger than 32767 bytes");
    }
    this.b = new PacketDataSerializer(☃.readBytes(☃));
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.writeBytes(this.b);
  }
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public String a()
  {
    return this.a;
  }
  
  public PacketDataSerializer b()
  {
    return this.b;
  }
}
