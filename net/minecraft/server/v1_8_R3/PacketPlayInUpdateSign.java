package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInUpdateSign
  implements Packet<PacketListenerPlayIn>
{
  private BlockPosition a;
  private IChatBaseComponent[] b;
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c();
    this.b = new IChatBaseComponent[4];
    for (int ☃ = 0; ☃ < 4; ☃++)
    {
      String ☃ = ☃.c(384);
      IChatBaseComponent ☃ = IChatBaseComponent.ChatSerializer.a(☃);
      this.b[☃] = ☃;
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    for (int ☃ = 0; ☃ < 4; ☃++)
    {
      IChatBaseComponent ☃ = this.b[☃];
      String ☃ = IChatBaseComponent.ChatSerializer.a(☃);
      ☃.a(☃);
    }
  }
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public BlockPosition a()
  {
    return this.a;
  }
  
  public IChatBaseComponent[] b()
  {
    return this.b;
  }
}
