package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInResourcePackStatus
  implements Packet<PacketListenerPlayIn>
{
  private String a;
  private EnumResourcePackStatus b;
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c(40);
    this.b = ((EnumResourcePackStatus)☃.a(EnumResourcePackStatus.class));
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.a(this.b);
  }
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public static enum EnumResourcePackStatus
  {
    private EnumResourcePackStatus() {}
  }
}
