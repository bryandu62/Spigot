package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInSettings
  implements Packet<PacketListenerPlayIn>
{
  private String a;
  private int b;
  private EntityHuman.EnumChatVisibility c;
  private boolean d;
  private int e;
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c(7);
    this.b = ☃.readByte();
    
    this.c = EntityHuman.EnumChatVisibility.a(☃.readByte());
    this.d = ☃.readBoolean();
    
    this.e = ☃.readUnsignedByte();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.writeByte(this.b);
    ☃.writeByte(this.c.a());
    ☃.writeBoolean(this.d);
    ☃.writeByte(this.e);
  }
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public String a()
  {
    return this.a;
  }
  
  public EntityHuman.EnumChatVisibility c()
  {
    return this.c;
  }
  
  public boolean d()
  {
    return this.d;
  }
  
  public int e()
  {
    return this.e;
  }
}
