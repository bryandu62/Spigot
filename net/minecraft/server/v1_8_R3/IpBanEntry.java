package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;

public class IpBanEntry
  extends ExpirableListEntry<String>
{
  public IpBanEntry(String ☃)
  {
    this(☃, null, null, null, null);
  }
  
  public IpBanEntry(String ☃, Date ☃, String ☃, Date ☃, String ☃)
  {
    super(☃, ☃, ☃, ☃, ☃);
  }
  
  public IpBanEntry(JsonObject ☃)
  {
    super(b(☃), ☃);
  }
  
  private static String b(JsonObject ☃)
  {
    return ☃.has("ip") ? ☃.get("ip").getAsString() : null;
  }
  
  protected void a(JsonObject ☃)
  {
    if (getKey() == null) {
      return;
    }
    ☃.addProperty("ip", (String)getKey());
    super.a(☃);
  }
}
