package net.minecraft.server.v1_8_R3;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;

public class PacketLoginOutSuccess
  implements Packet<PacketLoginOutListener>
{
  private GameProfile a;
  
  public PacketLoginOutSuccess() {}
  
  public PacketLoginOutSuccess(GameProfile ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    String ☃ = ☃.c(36);
    String ☃ = ☃.c(16);
    UUID ☃ = UUID.fromString(☃);
    this.a = new GameProfile(☃, ☃);
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    UUID ☃ = this.a.getId();
    ☃.a(☃ == null ? "" : ☃.toString());
    ☃.a(this.a.getName());
  }
  
  public void a(PacketLoginOutListener ☃)
  {
    ☃.a(this);
  }
}
