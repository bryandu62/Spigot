package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import org.apache.commons.lang3.Validate;

public class PacketPlayOutNamedSoundEffect
  implements Packet<PacketListenerPlayOut>
{
  private String a;
  private int b;
  private int c = Integer.MAX_VALUE;
  private int d;
  private float e;
  private int f;
  
  public PacketPlayOutNamedSoundEffect() {}
  
  public PacketPlayOutNamedSoundEffect(String ☃, double ☃, double ☃, double ☃, float ☃, float ☃)
  {
    Validate.notNull(☃, "name", new Object[0]);
    this.a = ☃;
    this.b = ((int)(☃ * 8.0D));
    this.c = ((int)(☃ * 8.0D));
    this.d = ((int)(☃ * 8.0D));
    this.e = ☃;
    this.f = ((int)(☃ * 63.0F));
    
    ☃ = MathHelper.a(☃, 0.0F, 255.0F);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c(256);
    this.b = ☃.readInt();
    this.c = ☃.readInt();
    this.d = ☃.readInt();
    this.e = ☃.readFloat();
    this.f = ☃.readUnsignedByte();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.writeInt(this.b);
    ☃.writeInt(this.c);
    ☃.writeInt(this.d);
    ☃.writeFloat(this.e);
    ☃.writeByte(this.f);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
