package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutSpawnEntityWeather
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private int b;
  private int c;
  private int d;
  private int e;
  
  public PacketPlayOutSpawnEntityWeather() {}
  
  public PacketPlayOutSpawnEntityWeather(Entity ☃)
  {
    this.a = ☃.getId();
    this.b = MathHelper.floor(☃.locX * 32.0D);
    this.c = MathHelper.floor(☃.locY * 32.0D);
    this.d = MathHelper.floor(☃.locZ * 32.0D);
    if ((☃ instanceof EntityLightning)) {
      this.e = 1;
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.e = ☃.readByte();
    this.b = ☃.readInt();
    this.c = ☃.readInt();
    this.d = ☃.readInt();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.writeByte(this.e);
    ☃.writeInt(this.b);
    ☃.writeInt(this.c);
    ☃.writeInt(this.d);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
