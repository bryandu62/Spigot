package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutEntityEffect
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private byte b;
  private byte c;
  private int d;
  private byte e;
  
  public PacketPlayOutEntityEffect() {}
  
  public PacketPlayOutEntityEffect(int ☃, MobEffect ☃)
  {
    this.a = ☃;
    this.b = ((byte)(☃.getEffectId() & 0xFF));
    this.c = ((byte)(☃.getAmplifier() & 0xFF));
    if (☃.getDuration() > 32767) {
      this.d = 32767;
    } else {
      this.d = ☃.getDuration();
    }
    this.e = ((byte)(☃.isShowParticles() ? 1 : 0));
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.readByte();
    this.c = ☃.readByte();
    this.d = ☃.e();
    this.e = ☃.readByte();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.writeByte(this.b);
    ☃.writeByte(this.c);
    ☃.b(this.d);
    ☃.writeByte(this.e);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
