package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldLoaderServer
  extends WorldLoader
{
  private static final Logger b = ;
  
  public WorldLoaderServer(File ☃)
  {
    super(☃);
  }
  
  protected int c()
  {
    return 19133;
  }
  
  public void d() {}
  
  public IDataManager a(String ☃, boolean ☃)
  {
    return new ServerNBTManager(this.a, ☃, ☃);
  }
  
  public boolean isConvertable(String ☃)
  {
    WorldData ☃ = c(☃);
    if ((☃ == null) || (☃.l() == c())) {
      return false;
    }
    return true;
  }
  
  public boolean convert(String ☃, IProgressUpdate ☃)
  {
    ☃.a(0);
    
    List<File> ☃ = Lists.newArrayList();
    List<File> ☃ = Lists.newArrayList();
    List<File> ☃ = Lists.newArrayList();
    File ☃ = new File(this.a, ☃);
    File ☃ = new File(☃, "DIM-1");
    File ☃ = new File(☃, "DIM1");
    
    b.info("Scanning folders...");
    
    a(☃, ☃);
    if (☃.exists()) {
      a(☃, ☃);
    }
    if (☃.exists()) {
      a(☃, ☃);
    }
    int ☃ = ☃.size() + ☃.size() + ☃.size();
    b.info("Total conversion count is " + ☃);
    
    WorldData ☃ = c(☃);
    
    WorldChunkManager ☃ = null;
    if (☃.getType() == WorldType.FLAT) {
      ☃ = new WorldChunkManagerHell(BiomeBase.PLAINS, 0.5F);
    } else {
      ☃ = new WorldChunkManager(☃.getSeed(), ☃.getType(), ☃.getGeneratorOptions());
    }
    a(new File(☃, "region"), ☃, ☃, 0, ☃, ☃);
    
    a(new File(☃, "region"), ☃, new WorldChunkManagerHell(BiomeBase.HELL, 0.0F), ☃.size(), ☃, ☃);
    
    a(new File(☃, "region"), ☃, new WorldChunkManagerHell(BiomeBase.SKY, 0.0F), ☃.size() + ☃.size(), ☃, ☃);
    
    ☃.e(19133);
    if (☃.getType() == WorldType.NORMAL_1_1) {
      ☃.a(WorldType.NORMAL);
    }
    g(☃);
    
    IDataManager ☃ = a(☃, false);
    ☃.saveWorldData(☃);
    
    return true;
  }
  
  private void g(String ☃)
  {
    File ☃ = new File(this.a, ☃);
    if (!☃.exists())
    {
      b.warn("Unable to create level.dat_mcr backup");
      return;
    }
    File ☃ = new File(☃, "level.dat");
    if (!☃.exists())
    {
      b.warn("Unable to create level.dat_mcr backup");
      return;
    }
    File ☃ = new File(☃, "level.dat_mcr");
    if (!☃.renameTo(☃)) {
      b.warn("Unable to create level.dat_mcr backup");
    }
  }
  
  private void a(File ☃, Iterable<File> ☃, WorldChunkManager ☃, int ☃, int ☃, IProgressUpdate ☃)
  {
    for (File ☃ : ☃)
    {
      a(☃, ☃, ☃, ☃, ☃, ☃);
      
      ☃++;
      int ☃ = (int)Math.round(100.0D * ☃ / ☃);
      ☃.a(☃);
    }
  }
  
  private void a(File ☃, File ☃, WorldChunkManager ☃, int ☃, int ☃, IProgressUpdate ☃)
  {
    try
    {
      String ☃ = ☃.getName();
      
      RegionFile ☃ = new RegionFile(☃);
      RegionFile ☃ = new RegionFile(new File(☃, ☃.substring(0, ☃.length() - ".mcr".length()) + ".mca"));
      for (int ☃ = 0; ☃ < 32; ☃++)
      {
        for (int ☃ = 0; ☃ < 32; ☃++) {
          if ((☃.c(☃, ☃)) && (!☃.c(☃, ☃)))
          {
            DataInputStream ☃ = ☃.a(☃, ☃);
            if (☃ == null)
            {
              b.warn("Failed to fetch input stream");
            }
            else
            {
              NBTTagCompound ☃ = NBTCompressedStreamTools.a(☃);
              ☃.close();
              
              NBTTagCompound ☃ = ☃.getCompound("Level");
              OldChunkLoader.OldChunk ☃ = OldChunkLoader.a(☃);
              
              NBTTagCompound ☃ = new NBTTagCompound();
              NBTTagCompound ☃ = new NBTTagCompound();
              ☃.set("Level", ☃);
              OldChunkLoader.a(☃, ☃, ☃);
              
              DataOutputStream ☃ = ☃.b(☃, ☃);
              NBTCompressedStreamTools.a(☃, ☃);
              ☃.close();
            }
          }
        }
        int ☃ = (int)Math.round(100.0D * (☃ * 1024) / (☃ * 1024));
        int ☃ = (int)Math.round(100.0D * ((☃ + 1) * 32 + ☃ * 1024) / (☃ * 1024));
        if (☃ > ☃) {
          ☃.a(☃);
        }
      }
      ☃.c();
      ☃.c();
    }
    catch (IOException ☃)
    {
      ☃.printStackTrace();
    }
  }
  
  class ChunkFilenameFilter
    implements FilenameFilter
  {
    ChunkFilenameFilter() {}
    
    public boolean accept(File ☃, String ☃)
    {
      return ☃.endsWith(".mcr");
    }
  }
  
  private void a(File ☃, Collection<File> ☃)
  {
    File ☃ = new File(☃, "region");
    File[] ☃ = ☃.listFiles(new ChunkFilenameFilter());
    if (☃ != null) {
      Collections.addAll(☃, ☃);
    }
  }
}
