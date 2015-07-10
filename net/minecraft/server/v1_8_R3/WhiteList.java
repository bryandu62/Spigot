package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Map;
import java.util.UUID;

public class WhiteList
  extends JsonList<GameProfile, WhiteListEntry>
{
  public WhiteList(File ☃)
  {
    super(☃);
  }
  
  protected JsonListEntry<GameProfile> a(JsonObject ☃)
  {
    return new WhiteListEntry(☃);
  }
  
  public boolean isWhitelisted(GameProfile ☃)
  {
    return d(☃);
  }
  
  public String[] getEntries()
  {
    String[] ☃ = new String[e().size()];
    int ☃ = 0;
    for (WhiteListEntry ☃ : e().values()) {
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
    for (WhiteListEntry ☃ : e().values()) {
      if (☃.equalsIgnoreCase(((GameProfile)☃.getKey()).getName())) {
        return (GameProfile)☃.getKey();
      }
    }
    return null;
  }
}
