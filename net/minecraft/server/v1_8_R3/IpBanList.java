package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonObject;
import java.io.File;
import java.net.SocketAddress;

public class IpBanList
  extends JsonList<String, IpBanEntry>
{
  public IpBanList(File ☃)
  {
    super(☃);
  }
  
  protected JsonListEntry<String> a(JsonObject ☃)
  {
    return new IpBanEntry(☃);
  }
  
  public boolean isBanned(SocketAddress ☃)
  {
    String ☃ = c(☃);
    return d(☃);
  }
  
  public IpBanEntry get(SocketAddress ☃)
  {
    String ☃ = c(☃);
    return (IpBanEntry)get(☃);
  }
  
  private String c(SocketAddress ☃)
  {
    String ☃ = ☃.toString();
    if (☃.contains("/")) {
      ☃ = ☃.substring(☃.indexOf('/') + 1);
    }
    if (☃.contains(":")) {
      ☃ = ☃.substring(0, ☃.indexOf(':'));
    }
    return ☃;
  }
}
