package net.minecraft.server.v1_8_R3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EULA
{
  private static final Logger a = ;
  private final File b;
  private final boolean c;
  
  public EULA(File ☃)
  {
    this.b = ☃;
    this.c = a(☃);
  }
  
  private boolean a(File ☃)
  {
    FileInputStream ☃ = null;
    boolean ☃ = false;
    try
    {
      Properties ☃ = new Properties();
      ☃ = new FileInputStream(☃);
      ☃.load(☃);
      ☃ = Boolean.parseBoolean(☃.getProperty("eula", "false"));
    }
    catch (Exception ☃)
    {
      a.warn("Failed to load " + ☃);
      b();
    }
    finally
    {
      IOUtils.closeQuietly(☃);
    }
    return ☃;
  }
  
  public boolean a()
  {
    return this.c;
  }
  
  public void b()
  {
    FileOutputStream ☃ = null;
    try
    {
      Properties ☃ = new Properties();
      ☃ = new FileOutputStream(this.b);
      ☃.setProperty("eula", "false");
      ☃.store(☃, "By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
    }
    catch (Exception ☃)
    {
      a.warn("Failed to save " + this.b, ☃);
    }
    finally
    {
      IOUtils.closeQuietly(☃);
    }
  }
}
