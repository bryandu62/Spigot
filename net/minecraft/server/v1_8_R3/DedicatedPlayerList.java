package net.minecraft.server.v1_8_R3;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DedicatedPlayerList
  extends PlayerList
{
  private static final Logger f = ;
  
  public DedicatedPlayerList(DedicatedServer ☃)
  {
    super(☃);
    
    a(☃.a("view-distance", 10));
    this.maxPlayers = ☃.a("max-players", 20);
    setHasWhitelist(☃.a("white-list", false));
    if (!☃.T())
    {
      getProfileBans().a(true);
      getIPBans().a(true);
    }
    z();
    x();
    y();
    w();
    A();
    C();
    B();
    if (!getWhitelist().c().exists()) {
      D();
    }
  }
  
  public void setHasWhitelist(boolean ☃)
  {
    super.setHasWhitelist(☃);
    getServer().a("white-list", Boolean.valueOf(☃));
    getServer().a();
  }
  
  public void addOp(GameProfile ☃)
  {
    super.addOp(☃);
    B();
  }
  
  public void removeOp(GameProfile ☃)
  {
    super.removeOp(☃);
    B();
  }
  
  public void removeWhitelist(GameProfile ☃)
  {
    super.removeWhitelist(☃);
    D();
  }
  
  public void addWhitelist(GameProfile ☃)
  {
    super.addWhitelist(☃);
    D();
  }
  
  public void reloadWhitelist()
  {
    C();
  }
  
  private void w()
  {
    try
    {
      getIPBans().save();
    }
    catch (IOException ☃)
    {
      f.warn("Failed to save ip banlist: ", ☃);
    }
  }
  
  private void x()
  {
    try
    {
      getProfileBans().save();
    }
    catch (IOException ☃)
    {
      f.warn("Failed to save user banlist: ", ☃);
    }
  }
  
  private void y()
  {
    try
    {
      getIPBans().load();
    }
    catch (IOException ☃)
    {
      f.warn("Failed to load ip banlist: ", ☃);
    }
  }
  
  private void z()
  {
    try
    {
      getProfileBans().load();
    }
    catch (IOException ☃)
    {
      f.warn("Failed to load user banlist: ", ☃);
    }
  }
  
  private void A()
  {
    try
    {
      getOPs().load();
    }
    catch (Exception ☃)
    {
      f.warn("Failed to load operators list: ", ☃);
    }
  }
  
  private void B()
  {
    try
    {
      getOPs().save();
    }
    catch (Exception ☃)
    {
      f.warn("Failed to save operators list: ", ☃);
    }
  }
  
  private void C()
  {
    try
    {
      getWhitelist().load();
    }
    catch (Exception ☃)
    {
      f.warn("Failed to load white-list: ", ☃);
    }
  }
  
  private void D()
  {
    try
    {
      getWhitelist().save();
    }
    catch (Exception ☃)
    {
      f.warn("Failed to save white-list: ", ☃);
    }
  }
  
  public boolean isWhitelisted(GameProfile ☃)
  {
    return (!getHasWhitelist()) || (isOp(☃)) || (getWhitelist().isWhitelisted(☃));
  }
  
  public DedicatedServer getServer()
  {
    return (DedicatedServer)super.getServer();
  }
  
  public boolean f(GameProfile ☃)
  {
    return getOPs().b(☃);
  }
}
