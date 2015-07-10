package net.minecraft.server.v1_8_R3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;

public class PacketStatusOutServerInfo
  implements Packet<PacketStatusOutListener>
{
  private static final Gson a = new GsonBuilder().registerTypeAdapter(ServerPing.ServerData.class, new ServerPing.ServerData.Serializer()).registerTypeAdapter(ServerPing.ServerPingPlayerSample.class, new ServerPing.ServerPingPlayerSample.Serializer()).registerTypeAdapter(ServerPing.class, new ServerPing.Serializer()).registerTypeHierarchyAdapter(IChatBaseComponent.class, new IChatBaseComponent.ChatSerializer()).registerTypeHierarchyAdapter(ChatModifier.class, new ChatModifier.ChatModifierSerializer()).registerTypeAdapterFactory(new ChatTypeAdapterFactory()).create();
  private ServerPing b;
  
  public PacketStatusOutServerInfo() {}
  
  public PacketStatusOutServerInfo(ServerPing ☃)
  {
    this.b = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.b = ((ServerPing)a.fromJson(☃.c(32767), ServerPing.class));
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(a.toJson(this.b));
  }
  
  public void a(PacketStatusOutListener ☃)
  {
    ☃.a(this);
  }
}
