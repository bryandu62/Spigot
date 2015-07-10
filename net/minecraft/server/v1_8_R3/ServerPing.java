package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;
import java.util.UUID;

public class ServerPing
{
  private IChatBaseComponent a;
  private ServerPingPlayerSample b;
  private ServerData c;
  private String d;
  
  public IChatBaseComponent a()
  {
    return this.a;
  }
  
  public void setMOTD(IChatBaseComponent ☃)
  {
    this.a = ☃;
  }
  
  public ServerPingPlayerSample b()
  {
    return this.b;
  }
  
  public void setPlayerSample(ServerPingPlayerSample ☃)
  {
    this.b = ☃;
  }
  
  public ServerData c()
  {
    return this.c;
  }
  
  public void setServerInfo(ServerData ☃)
  {
    this.c = ☃;
  }
  
  public void setFavicon(String ☃)
  {
    this.d = ☃;
  }
  
  public String d()
  {
    return this.d;
  }
  
  public static class ServerPingPlayerSample
  {
    private final int a;
    private final int b;
    private GameProfile[] c;
    
    public ServerPingPlayerSample(int ☃, int ☃)
    {
      this.a = ☃;
      this.b = ☃;
    }
    
    public int a()
    {
      return this.a;
    }
    
    public int b()
    {
      return this.b;
    }
    
    public GameProfile[] c()
    {
      return this.c;
    }
    
    public void a(GameProfile[] ☃)
    {
      this.c = ☃;
    }
    
    public static class Serializer
      implements JsonDeserializer<ServerPing.ServerPingPlayerSample>, JsonSerializer<ServerPing.ServerPingPlayerSample>
    {
      public ServerPing.ServerPingPlayerSample a(JsonElement ☃, Type ☃, JsonDeserializationContext ☃)
        throws JsonParseException
      {
        JsonObject ☃ = ChatDeserializer.l(☃, "players");
        ServerPing.ServerPingPlayerSample ☃ = new ServerPing.ServerPingPlayerSample(ChatDeserializer.m(☃, "max"), ChatDeserializer.m(☃, "online"));
        if (ChatDeserializer.d(☃, "sample"))
        {
          JsonArray ☃ = ChatDeserializer.t(☃, "sample");
          if (☃.size() > 0)
          {
            GameProfile[] ☃ = new GameProfile[☃.size()];
            for (int ☃ = 0; ☃ < ☃.length; ☃++)
            {
              JsonObject ☃ = ChatDeserializer.l(☃.get(☃), "player[" + ☃ + "]");
              String ☃ = ChatDeserializer.h(☃, "id");
              ☃[☃] = new GameProfile(UUID.fromString(☃), ChatDeserializer.h(☃, "name"));
            }
            ☃.a(☃);
          }
        }
        return ☃;
      }
      
      public JsonElement a(ServerPing.ServerPingPlayerSample ☃, Type ☃, JsonSerializationContext ☃)
      {
        JsonObject ☃ = new JsonObject();
        
        ☃.addProperty("max", Integer.valueOf(☃.a()));
        ☃.addProperty("online", Integer.valueOf(☃.b()));
        if ((☃.c() != null) && (☃.c().length > 0))
        {
          JsonArray ☃ = new JsonArray();
          for (int ☃ = 0; ☃ < ☃.c().length; ☃++)
          {
            JsonObject ☃ = new JsonObject();
            UUID ☃ = ☃.c()[☃].getId();
            ☃.addProperty("id", ☃ == null ? "" : ☃.toString());
            ☃.addProperty("name", ☃.c()[☃].getName());
            ☃.add(☃);
          }
          ☃.add("sample", ☃);
        }
        return ☃;
      }
    }
  }
  
  public static class ServerData
  {
    private final String a;
    private final int b;
    
    public ServerData(String ☃, int ☃)
    {
      this.a = ☃;
      this.b = ☃;
    }
    
    public String a()
    {
      return this.a;
    }
    
    public int b()
    {
      return this.b;
    }
    
    public static class Serializer
      implements JsonDeserializer<ServerPing.ServerData>, JsonSerializer<ServerPing.ServerData>
    {
      public ServerPing.ServerData a(JsonElement ☃, Type ☃, JsonDeserializationContext ☃)
        throws JsonParseException
      {
        JsonObject ☃ = ChatDeserializer.l(☃, "version");
        return new ServerPing.ServerData(ChatDeserializer.h(☃, "name"), ChatDeserializer.m(☃, "protocol"));
      }
      
      public JsonElement a(ServerPing.ServerData ☃, Type ☃, JsonSerializationContext ☃)
      {
        JsonObject ☃ = new JsonObject();
        ☃.addProperty("name", ☃.a());
        ☃.addProperty("protocol", Integer.valueOf(☃.b()));
        return ☃;
      }
    }
  }
  
  public static class Serializer
    implements JsonDeserializer<ServerPing>, JsonSerializer<ServerPing>
  {
    public ServerPing a(JsonElement ☃, Type ☃, JsonDeserializationContext ☃)
      throws JsonParseException
    {
      JsonObject ☃ = ChatDeserializer.l(☃, "status");
      ServerPing ☃ = new ServerPing();
      if (☃.has("description")) {
        ☃.setMOTD((IChatBaseComponent)☃.deserialize(☃.get("description"), IChatBaseComponent.class));
      }
      if (☃.has("players")) {
        ☃.setPlayerSample((ServerPing.ServerPingPlayerSample)☃.deserialize(☃.get("players"), ServerPing.ServerPingPlayerSample.class));
      }
      if (☃.has("version")) {
        ☃.setServerInfo((ServerPing.ServerData)☃.deserialize(☃.get("version"), ServerPing.ServerData.class));
      }
      if (☃.has("favicon")) {
        ☃.setFavicon(ChatDeserializer.h(☃, "favicon"));
      }
      return ☃;
    }
    
    public JsonElement a(ServerPing ☃, Type ☃, JsonSerializationContext ☃)
    {
      JsonObject ☃ = new JsonObject();
      if (☃.a() != null) {
        ☃.add("description", ☃.serialize(☃.a()));
      }
      if (☃.b() != null) {
        ☃.add("players", ☃.serialize(☃.b()));
      }
      if (☃.c() != null) {
        ☃.add("version", ☃.serialize(☃.c()));
      }
      if (☃.d() != null) {
        ☃.addProperty("favicon", ☃.d());
      }
      return ☃;
    }
  }
}
