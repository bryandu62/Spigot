package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInBlockDig
  implements Packet<PacketListenerPlayIn>
{
  private BlockPosition a;
  private EnumDirection b;
  private EnumPlayerDigType c;
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.c = ((EnumPlayerDigType)☃.a(EnumPlayerDigType.class));
    this.a = ☃.c();
    this.b = EnumDirection.fromType1(☃.readUnsignedByte());
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.c);
    ☃.a(this.a);
    ☃.writeByte(this.b.a());
  }
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public BlockPosition a()
  {
    return this.a;
  }
  
  public EnumDirection b()
  {
    return this.b;
  }
  
  public EnumPlayerDigType c()
  {
    return this.c;
  }
  
  public static enum EnumPlayerDigType
  {
    private EnumPlayerDigType() {}
  }
}
