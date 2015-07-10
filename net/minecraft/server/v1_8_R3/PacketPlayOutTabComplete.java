package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutTabComplete
  implements Packet<PacketListenerPlayOut>
{
  private String[] a;
  
  public PacketPlayOutTabComplete() {}
  
  public PacketPlayOutTabComplete(String[] ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = new String[☃.e()];
    for (int ☃ = 0; ☃ < this.a.length; ☃++) {
      this.a[☃] = ☃.c(32767);
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a.length);
    for (String ☃ : this.a) {
      ☃.a(☃);
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
