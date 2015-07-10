package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Map;
import java.util.UUID;

public class GameProfileBanList
  extends JsonList<GameProfile, GameProfileBanEntry>
{
  public GameProfileBanList(File ☃)
  {
    super(☃);
  }
  
  protected JsonListEntry<GameProfile> a(JsonObject ☃)
  {
    return new GameProfileBanEntry(☃);
  }
  
  public boolean isBanned(GameProfile ☃)
  {
    return d(☃);
  }
  
  public String[] getEntries()
  {
    String[] ☃ = new String[e().size()];
    int ☃ = 0;
    for (GameProfileBanEntry ☃ : e().values()) {
      ☃[(☃++)] = ((GameProfile)☃.getKey()).getName();
    }
    return ☃;
  }
  
  protected String b(GameProfile ☃)
  {
    return ☃.getId().toString();
  }
  
  public GameProfile a(String ☃)
  {
    for (GameProfileBanEntry ☃ : e().values()) {
      if (☃.equalsIgnoreCase(((GameProfile)☃.getKey()).getName())) {
        return (GameProfile)☃.getKey();
      }
    }
    return null;
  }
}
