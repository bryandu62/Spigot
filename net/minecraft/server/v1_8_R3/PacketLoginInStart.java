package net.minecraft.server.v1_8_R3;

import com.mojang.authlib.GameProfile;
import java.io.IOException;

public class PacketLoginInStart
  implements Packet<PacketLoginInListener>
{
  private GameProfile a;
  
  public PacketLoginInStart() {}
  
  public PacketLoginInStart(GameProfile ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = new GameProfile(null, ☃.c(16));
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a.getName());
  }
  
  public void a(PacketLoginInListener ☃)
  {
    ☃.a(this);
  }
  
  public GameProfile a()
  {
    return this.a;
  }
}
