package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class WhiteListEntry
  extends JsonListEntry<GameProfile>
{
  public WhiteListEntry(GameProfile ☃)
  {
    super(☃);
  }
  
  public WhiteListEntry(JsonObject ☃)
  {
    super(b(☃), ☃);
  }
  
  protected void a(JsonObject ☃)
  {
    if (getKey() == null) {
      return;
    }
    ☃.addProperty("uuid", ((GameProfile)getKey()).getId() == null ? "" : ((GameProfile)getKey()).getId().toString());
    ☃.addProperty("name", ((GameProfile)getKey()).getName());
    super.a(☃);
  }
  
  private static GameProfile b(JsonObject ☃)
  {
    if ((!☃.has("uuid")) || (!☃.has("name"))) {
      return null;
    }
    String ☃ = ☃.get("uuid").getAsString();
    UUID ☃;
    try
    {
      ☃ = UUID.fromString(☃);
    }
    catch (Throwable ☃)
    {
      return null;
    }
    return new GameProfile(☃, ☃.get("name").getAsString());
  }
}
