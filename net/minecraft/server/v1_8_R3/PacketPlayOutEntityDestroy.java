package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutEntityDestroy
  implements Packet<PacketListenerPlayOut>
{
  private int[] a;
  
  public PacketPlayOutEntityDestroy() {}
  
  public PacketPlayOutEntityDestroy(int... ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = new int[☃.e()];
    for (int ☃ = 0; ☃ < this.a.length; ☃++) {
      this.a[☃] = ☃.e();
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a.length);
    for (int ☃ = 0; ☃ < this.a.length; ☃++) {
      ☃.b(this.a[☃]);
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
