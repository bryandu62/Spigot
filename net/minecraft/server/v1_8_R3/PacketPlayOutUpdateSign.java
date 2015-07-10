package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutUpdateSign
  implements Packet<PacketListenerPlayOut>
{
  private World a;
  private BlockPosition b;
  private IChatBaseComponent[] c;
  
  public PacketPlayOutUpdateSign() {}
  
  public PacketPlayOutUpdateSign(World ☃, BlockPosition ☃, IChatBaseComponent[] ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = new IChatBaseComponent[] { ☃[0], ☃[1], ☃[2], ☃[3] };
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.b = ☃.c();
    this.c = new IChatBaseComponent[4];
    for (int ☃ = 0; ☃ < 4; ☃++) {
      this.c[☃] = ☃.d();
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.b);
    for (int ☃ = 0; ☃ < 4; ☃++) {
      ☃.a(this.c[☃]);
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
