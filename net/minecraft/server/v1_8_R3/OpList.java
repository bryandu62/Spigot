package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Map;
import java.util.UUID;

public class OpList
  extends JsonList<GameProfile, OpListEntry>
{
  public OpList(File ☃)
  {
    super(☃);
  }
  
  protected JsonListEntry<GameProfile> a(JsonObject ☃)
  {
    return new OpListEntry(☃);
  }
  
  public String[] getEntries()
  {
    String[] ☃ = new String[e().size()];
    int ☃ = 0;
    for (OpListEntry ☃ : e().values()) {
      ☃[(☃++)] = ((GameProfile)☃.getKey()).getName();
    }
    return ☃;
  }
  
  public boolean b(GameProfile ☃)
  {
    OpListEntry ☃ = (OpListEntry)get(☃);
    if (☃ != null) {
      return ☃.b();
    }
    return false;
  }
  
  protected String c(GameProfile ☃)
  {
    return ☃.getId().toString();
  }
  
  public GameProfile a(String ☃)
  {
    for (OpListEntry ☃ : e().values()) {
      if (☃.equalsIgnoreCase(((GameProfile)☃.getKey()).getName())) {
        return (GameProfile)☃.getKey();
      }
    }
    return null;
  }
}
