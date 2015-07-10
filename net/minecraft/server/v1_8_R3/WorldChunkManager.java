package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;

public class WorldChunkManager
{
  private GenLayer b;
  private GenLayer c;
  private BiomeCache d = new BiomeCache(this);
  private List<BiomeBase> e;
  private String f = "";
  
  protected WorldChunkManager()
  {
    this.e = Lists.newArrayList();
    this.e.add(BiomeBase.FOREST);
    this.e.add(BiomeBase.PLAINS);
    this.e.add(BiomeBase.TAIGA);
    this.e.add(BiomeBase.TAIGA_HILLS);
    this.e.add(BiomeBase.FOREST_HILLS);
    this.e.add(BiomeBase.JUNGLE);
    this.e.add(BiomeBase.JUNGLE_HILLS);
  }
  
  public WorldChunkManager(long ☃, WorldType ☃, String ☃)
  {
    this();
    this.f = ☃;
    
    GenLayer[] ☃ = GenLayer.a(☃, ☃, ☃);
    this.b = ☃[0];
    this.c = ☃[1];
  }
  
  public WorldChunkManager(World ☃)
  {
    this(☃.getSeed(), ☃.getWorldData().getType(), ☃.getWorldData().getGeneratorOptions());
  }
  
  public List<BiomeBase> a()
  {
    return this.e;
  }
  
  public BiomeBase getBiome(BlockPosition ☃)
  {
    return getBiome(☃, null);
  }
  
  public BiomeBase getBiome(BlockPosition ☃, BiomeBase ☃)
  {
    return this.d.a(☃.getX(), ☃.getZ(), ☃);
  }
  
  public float[] getWetness(float[] ☃, int ☃, int ☃, int ☃, int ☃)
  {
    
    if ((☃ == null) || (☃.length < ☃ * ☃)) {
      ☃ = new float[☃ * ☃];
    }
    int[] ☃ = this.c.a(☃, ☃, ☃, ☃);
    for (int ☃ = 0; ☃ < ☃ * ☃; ☃++) {
      try
      {
        float ☃ = BiomeBase.getBiome(☃[☃], BiomeBase.ad).h() / 65536.0F;
        if (☃ > 1.0F) {
          ☃ = 1.0F;
        }
        ☃[☃] = ☃;
      }
      catch (Throwable ☃)
      {
        CrashReport ☃ = CrashReport.a(☃, "Invalid Biome id");
        CrashReportSystemDetails ☃ = ☃.a("DownfallBlock");
        ☃.a("biome id", Integer.valueOf(☃));
        ☃.a("downfalls[] size", Integer.valueOf(☃.length));
        ☃.a("x", Integer.valueOf(☃));
        ☃.a("z", Integer.valueOf(☃));
        ☃.a("w", Integer.valueOf(☃));
        ☃.a("h", Integer.valueOf(☃));
        
        throw new ReportedException(☃);
      }
    }
    return ☃;
  }
  
  public BiomeBase[] getBiomes(BiomeBase[] ☃, int ☃, int ☃, int ☃, int ☃)
  {
    
    if ((☃ == null) || (☃.length < ☃ * ☃)) {
      ☃ = new BiomeBase[☃ * ☃];
    }
    int[] ☃ = this.b.a(☃, ☃, ☃, ☃);
    try
    {
      for (int ☃ = 0; ☃ < ☃ * ☃; ☃++) {
        ☃[☃] = BiomeBase.getBiome(☃[☃], BiomeBase.ad);
      }
    }
    catch (Throwable ☃)
    {
      CrashReport ☃ = CrashReport.a(☃, "Invalid Biome id");
      CrashReportSystemDetails ☃ = ☃.a("RawBiomeBlock");
      ☃.a("biomes[] size", Integer.valueOf(☃.length));
      ☃.a("x", Integer.valueOf(☃));
      ☃.a("z", Integer.valueOf(☃));
      ☃.a("w", Integer.valueOf(☃));
      ☃.a("h", Integer.valueOf(☃));
      
      throw new ReportedException(☃);
    }
    return ☃;
  }
  
  public BiomeBase[] getBiomeBlock(BiomeBase[] ☃, int ☃, int ☃, int ☃, int ☃)
  {
    return a(☃, ☃, ☃, ☃, ☃, true);
  }
  
  public BiomeBase[] a(BiomeBase[] ☃, int ☃, int ☃, int ☃, int ☃, boolean ☃)
  {
    
    if ((☃ == null) || (☃.length < ☃ * ☃)) {
      ☃ = new BiomeBase[☃ * ☃];
    }
    if ((☃) && (☃ == 16) && (☃ == 16) && ((☃ & 0xF) == 0) && ((☃ & 0xF) == 0))
    {
      BiomeBase[] ☃ = this.d.c(☃, ☃);
      System.arraycopy(☃, 0, ☃, 0, ☃ * ☃);
      return ☃;
    }
    int[] ☃ = this.c.a(☃, ☃, ☃, ☃);
    for (int ☃ = 0; ☃ < ☃ * ☃; ☃++) {
      ☃[☃] = BiomeBase.getBiome(☃[☃], BiomeBase.ad);
    }
    return ☃;
  }
  
  public boolean a(int ☃, int ☃, int ☃, List<BiomeBase> ☃)
  {
    IntCache.a();
    int ☃ = ☃ - ☃ >> 2;
    int ☃ = ☃ - ☃ >> 2;
    int ☃ = ☃ + ☃ >> 2;
    int ☃ = ☃ + ☃ >> 2;
    
    int ☃ = ☃ - ☃ + 1;
    int ☃ = ☃ - ☃ + 1;
    
    int[] ☃ = this.b.a(☃, ☃, ☃, ☃);
    try
    {
      for (int ☃ = 0; ☃ < ☃ * ☃; ☃++)
      {
        BiomeBase ☃ = BiomeBase.getBiome(☃[☃]);
        if (!☃.contains(☃)) {
          return false;
        }
      }
    }
    catch (Throwable ☃)
    {
      CrashReport ☃ = CrashReport.a(☃, "Invalid Biome id");
      CrashReportSystemDetails ☃ = ☃.a("Layer");
      ☃.a("Layer", this.b.toString());
      ☃.a("x", Integer.valueOf(☃));
      ☃.a("z", Integer.valueOf(☃));
      ☃.a("radius", Integer.valueOf(☃));
      ☃.a("allowed", ☃);
      
      throw new ReportedException(☃);
    }
    return true;
  }
  
  public BlockPosition a(int ☃, int ☃, int ☃, List<BiomeBase> ☃, Random ☃)
  {
    IntCache.a();
    int ☃ = ☃ - ☃ >> 2;
    int ☃ = ☃ - ☃ >> 2;
    int ☃ = ☃ + ☃ >> 2;
    int ☃ = ☃ + ☃ >> 2;
    
    int ☃ = ☃ - ☃ + 1;
    int ☃ = ☃ - ☃ + 1;
    int[] ☃ = this.b.a(☃, ☃, ☃, ☃);
    BlockPosition ☃ = null;
    int ☃ = 0;
    for (int ☃ = 0; ☃ < ☃ * ☃; ☃++)
    {
      int ☃ = ☃ + ☃ % ☃ << 2;
      int ☃ = ☃ + ☃ / ☃ << 2;
      BiomeBase ☃ = BiomeBase.getBiome(☃[☃]);
      if ((☃.contains(☃)) && (
        (☃ == null) || (☃.nextInt(☃ + 1) == 0)))
      {
        ☃ = new BlockPosition(☃, 0, ☃);
        ☃++;
      }
    }
    return ☃;
  }
  
  public void b()
  {
    this.d.a();
  }
}
