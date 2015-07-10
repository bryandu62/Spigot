package net.minecraft.server.v1_8_R3;

import java.io.File;
import java.io.FileInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldLoader
  implements Convertable
{
  private static final Logger b = ;
  protected final File a;
  
  public WorldLoader(File ☃)
  {
    if (!☃.exists()) {
      ☃.mkdirs();
    }
    this.a = ☃;
  }
  
  public void d() {}
  
  public WorldData c(String ☃)
  {
    File ☃ = new File(this.a, ☃);
    if (!☃.exists()) {
      return null;
    }
    File ☃ = new File(☃, "level.dat");
    if (☃.exists()) {
      try
      {
        NBTTagCompound ☃ = NBTCompressedStreamTools.a(new FileInputStream(☃));
        NBTTagCompound ☃ = ☃.getCompound("Data");
        return new WorldData(☃);
      }
      catch (Exception ☃)
      {
        b.error("Exception reading " + ☃, ☃);
      }
    }
    ☃ = new File(☃, "level.dat_old");
    if (☃.exists()) {
      try
      {
        NBTTagCompound ☃ = NBTCompressedStreamTools.a(new FileInputStream(☃));
        NBTTagCompound ☃ = ☃.getCompound("Data");
        return new WorldData(☃);
      }
      catch (Exception ☃)
      {
        b.error("Exception reading " + ☃, ☃);
      }
    }
    return null;
  }
  
  public boolean e(String ☃)
  {
    File ☃ = new File(this.a, ☃);
    if (!☃.exists()) {
      return true;
    }
    b.info("Deleting level " + ☃);
    for (int ☃ = 1; ☃ <= 5; ☃++)
    {
      b.info("Attempt " + ☃ + "...");
      if (a(☃.listFiles())) {
        break;
      }
      b.warn("Unsuccessful in deleting contents.");
      if (☃ < 5) {
        try
        {
          Thread.sleep(500L);
        }
        catch (InterruptedException localInterruptedException) {}
      }
    }
    return ☃.delete();
  }
  
  protected static boolean a(File[] ☃)
  {
    for (int ☃ = 0; ☃ < ☃.length; ☃++)
    {
      File ☃ = ☃[☃];
      b.debug("Deleting " + ☃);
      if ((☃.isDirectory()) && 
        (!a(☃.listFiles())))
      {
        b.warn("Couldn't delete directory " + ☃);
        return false;
      }
      if (!☃.delete())
      {
        b.warn("Couldn't delete file " + ☃);
        return false;
      }
    }
    return true;
  }
  
  public IDataManager a(String ☃, boolean ☃)
  {
    return new WorldNBTStorage(this.a, ☃, ☃);
  }
  
  public boolean isConvertable(String ☃)
  {
    return false;
  }
  
  public boolean convert(String ☃, IProgressUpdate ☃)
  {
    return false;
  }
}
