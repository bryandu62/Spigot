package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInEntityAction
  implements Packet<PacketListenerPlayIn>
{
  private int a;
  private EnumPlayerAction animation;
  private int c;
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.animation = ((EnumPlayerAction)☃.a(EnumPlayerAction.class));
    this.c = ☃.e();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.a(this.animation);
    ☃.b(this.c);
  }
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public EnumPlayerAction b()
  {
    return this.animation;
  }
  
  public int c()
  {
    return this.c;
  }
  
  public static enum EnumPlayerAction
  {
    private EnumPlayerAction() {}
  }
}
