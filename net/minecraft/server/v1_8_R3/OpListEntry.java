package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class OpListEntry
  extends JsonListEntry<GameProfile>
{
  private final int a;
  private final boolean b;
  
  public OpListEntry(GameProfile ☃, int ☃, boolean ☃)
  {
    super(☃);
    this.a = ☃;
    this.b = ☃;
  }
  
  public OpListEntry(JsonObject ☃)
  {
    super(b(☃), ☃);
    this.a = (☃.has("level") ? ☃.get("level").getAsInt() : 0);
    this.b = ((☃.has("bypassesPlayerLimit")) && (☃.get("bypassesPlayerLimit").getAsBoolean()));
  }
  
  public int a()
  {
    return this.a;
  }
  
  public boolean b()
  {
    return this.b;
  }
  
  protected void a(JsonObject ☃)
  {
    if (getKey() == null) {
      return;
    }
    ☃.addProperty("uuid", ((GameProfile)getKey()).getId() == null ? "" : ((GameProfile)getKey()).getId().toString());
    ☃.addProperty("name", ((GameProfile)getKey()).getName());
    super.a(☃);
    ☃.addProperty("level", Integer.valueOf(this.a));
    ☃.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.b));
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
