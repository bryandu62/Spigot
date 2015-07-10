package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutSpawnEntityExperienceOrb
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private int b;
  private int c;
  private int d;
  private int e;
  
  public PacketPlayOutSpawnEntityExperienceOrb() {}
  
  public PacketPlayOutSpawnEntityExperienceOrb(EntityExperienceOrb ☃)
  {
    this.a = ☃.getId();
    this.b = MathHelper.floor(☃.locX * 32.0D);
    this.c = MathHelper.floor(☃.locY * 32.0D);
    this.d = MathHelper.floor(☃.locZ * 32.0D);
    this.e = ☃.j();
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.readInt();
    this.c = ☃.readInt();
    this.d = ☃.readInt();
    this.e = ☃.readShort();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.writeInt(this.b);
    ☃.writeInt(this.c);
    ☃.writeInt(this.d);
    ☃.writeShort(this.e);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
