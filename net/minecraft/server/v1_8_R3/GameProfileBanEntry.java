package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.Date;
import java.util.UUID;

public class GameProfileBanEntry
  extends ExpirableListEntry<GameProfile>
{
  public GameProfileBanEntry(GameProfile gameprofile)
  {
    this(gameprofile, null, null, null, null);
  }
  
  public GameProfileBanEntry(GameProfile gameprofile, Date date, String s, Date date1, String s1)
  {
    super(gameprofile, date, s, date1, s1);
  }
  
  public GameProfileBanEntry(JsonObject jsonobject)
  {
    super(b(jsonobject), jsonobject);
  }
  
  protected void a(JsonObject jsonobject)
  {
    if (getKey() != null)
    {
      jsonobject.addProperty("uuid", ((GameProfile)getKey()).getId() == null ? "" : ((GameProfile)getKey()).getId().toString());
      jsonobject.addProperty("name", ((GameProfile)getKey()).getName());
      super.a(jsonobject);
    }
  }
  
  private static GameProfile b(JsonObject jsonobject)
  {
    UUID uuid = null;
    String name = null;
    if (jsonobject.has("uuid"))
    {
      String s = jsonobject.get("uuid").getAsString();
      try
      {
        uuid = UUID.fromString(s);
      }
      catch (Throwable localThrowable) {}
    }
    if (jsonobject.has("name")) {
      name = jsonobject.get("name").getAsString();
    }
    if ((uuid != null) || (name != null)) {
      return new GameProfile(uuid, name);
    }
    return null;
  }
}
