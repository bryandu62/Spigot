package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutEntityTeleport
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private int b;
  private int c;
  private int d;
  private byte e;
  private byte f;
  private boolean g;
  
  public PacketPlayOutEntityTeleport() {}
  
  public PacketPlayOutEntityTeleport(Entity ☃)
  {
    this.a = ☃.getId();
    this.b = MathHelper.floor(☃.locX * 32.0D);
    this.c = MathHelper.floor(☃.locY * 32.0D);
    this.d = MathHelper.floor(☃.locZ * 32.0D);
    this.e = ((byte)(int)(☃.yaw * 256.0F / 360.0F));
    this.f = ((byte)(int)(☃.pitch * 256.0F / 360.0F));
    this.g = ☃.onGround;
  }
  
  public PacketPlayOutEntityTeleport(int ☃, int ☃, int ☃, int ☃, byte ☃, byte ☃, boolean ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
    this.e = ☃;
    this.f = ☃;
    this.g = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.readInt();
    this.c = ☃.readInt();
    this.d = ☃.readInt();
    this.e = ☃.readByte();
    this.f = ☃.readByte();
    this.g = ☃.readBoolean();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.writeInt(this.b);
    ☃.writeInt(this.c);
    ☃.writeInt(this.d);
    ☃.writeByte(this.e);
    ☃.writeByte(this.f);
    ☃.writeBoolean(this.g);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
