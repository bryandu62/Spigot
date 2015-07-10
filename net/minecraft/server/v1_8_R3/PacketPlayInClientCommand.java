package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInClientCommand
  implements Packet<PacketListenerPlayIn>
{
  private EnumClientCommand a;
  
  public PacketPlayInClientCommand() {}
  
  public PacketPlayInClientCommand(EnumClientCommand ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ((EnumClientCommand)☃.a(EnumClientCommand.class));
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
  }
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public EnumClientCommand a()
  {
    return this.a;
  }
  
  public static enum EnumClientCommand
  {
    private EnumClientCommand() {}
  }
}
