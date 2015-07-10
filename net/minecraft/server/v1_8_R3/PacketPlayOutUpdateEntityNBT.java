package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutUpdateEntityNBT
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private NBTTagCompound b;
  
  public PacketPlayOutUpdateEntityNBT() {}
  
  public PacketPlayOutUpdateEntityNBT(int ☃, NBTTagCompound ☃)
  {
    this.a = ☃;
    this.b = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.h();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.a(this.b);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
