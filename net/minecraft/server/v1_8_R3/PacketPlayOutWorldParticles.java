package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutWorldParticles
  implements Packet<PacketListenerPlayOut>
{
  private EnumParticle a;
  private float b;
  private float c;
  private float d;
  private float e;
  private float f;
  private float g;
  private float h;
  private int i;
  private boolean j;
  private int[] k;
  
  public PacketPlayOutWorldParticles() {}
  
  public PacketPlayOutWorldParticles(EnumParticle ☃, boolean ☃, float ☃, float ☃, float ☃, float ☃, float ☃, float ☃, float ☃, int ☃, int... ☃)
  {
    this.a = ☃;
    this.j = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
    this.e = ☃;
    this.f = ☃;
    this.g = ☃;
    this.h = ☃;
    this.i = ☃;
    this.k = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = EnumParticle.a(☃.readInt());
    if (this.a == null) {
      this.a = EnumParticle.BARRIER;
    }
    this.j = ☃.readBoolean();
    this.b = ☃.readFloat();
    this.c = ☃.readFloat();
    this.d = ☃.readFloat();
    this.e = ☃.readFloat();
    this.f = ☃.readFloat();
    this.g = ☃.readFloat();
    this.h = ☃.readFloat();
    this.i = ☃.readInt();
    int ☃ = this.a.d();
    this.k = new int[☃];
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      this.k[☃] = ☃.e();
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeInt(this.a.c());
    ☃.writeBoolean(this.j);
    ☃.writeFloat(this.b);
    ☃.writeFloat(this.c);
    ☃.writeFloat(this.d);
    ☃.writeFloat(this.e);
    ☃.writeFloat(this.f);
    ☃.writeFloat(this.g);
    ☃.writeFloat(this.h);
    ☃.writeInt(this.i);
    int ☃ = this.a.d();
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      ☃.b(this.k[☃]);
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
