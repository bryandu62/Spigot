package net.minecraft.server.v1_8_R3;

import java.util.concurrent.Callable;

public abstract class GenLayer
{
  private long c;
  protected GenLayer a;
  private long d;
  protected long b;
  
  public static GenLayer[] a(long ☃, WorldType ☃, String ☃)
  {
    GenLayer ☃ = new LayerIsland(1L);
    ☃ = new GenLayerZoomFuzzy(2000L, ☃);
    ☃ = new GenLayerIsland(1L, ☃);
    ☃ = new GenLayerZoom(2001L, ☃);
    ☃ = new GenLayerIsland(2L, ☃);
    ☃ = new GenLayerIsland(50L, ☃);
    ☃ = new GenLayerIsland(70L, ☃);
    ☃ = new GenLayerIcePlains(2L, ☃);
    ☃ = new GenLayerTopSoil(2L, ☃);
    ☃ = new GenLayerIsland(3L, ☃);
    ☃ = new GenLayerSpecial(2L, ☃, GenLayerSpecial.EnumGenLayerSpecial.COOL_WARM);
    ☃ = new GenLayerSpecial(2L, ☃, GenLayerSpecial.EnumGenLayerSpecial.HEAT_ICE);
    ☃ = new GenLayerSpecial(3L, ☃, GenLayerSpecial.EnumGenLayerSpecial.SPECIAL);
    ☃ = new GenLayerZoom(2002L, ☃);
    ☃ = new GenLayerZoom(2003L, ☃);
    ☃ = new GenLayerIsland(4L, ☃);
    ☃ = new GenLayerMushroomIsland(5L, ☃);
    ☃ = new GenLayerDeepOcean(4L, ☃);
    ☃ = GenLayerZoom.b(1000L, ☃, 0);
    
    CustomWorldSettingsFinal ☃ = null;
    int ☃ = 4;
    int ☃ = ☃;
    if ((☃ == WorldType.CUSTOMIZED) && (☃.length() > 0))
    {
      ☃ = CustomWorldSettingsFinal.CustomWorldSettings.a(☃).b();
      ☃ = ☃.G;
      ☃ = ☃.H;
    }
    if (☃ == WorldType.LARGE_BIOMES) {
      ☃ = 6;
    }
    GenLayer ☃ = ☃;
    ☃ = GenLayerZoom.b(1000L, ☃, 0);
    ☃ = new GenLayerCleaner(100L, ☃);
    
    GenLayer ☃ = ☃;
    ☃ = new GenLayerBiome(200L, ☃, ☃, ☃);
    ☃ = GenLayerZoom.b(1000L, ☃, 2);
    ☃ = new GenLayerDesert(1000L, ☃);
    GenLayer ☃ = ☃;
    ☃ = GenLayerZoom.b(1000L, ☃, 2);
    ☃ = new GenLayerRegionHills(1000L, ☃, ☃);
    
    ☃ = GenLayerZoom.b(1000L, ☃, 2);
    ☃ = GenLayerZoom.b(1000L, ☃, ☃);
    ☃ = new GenLayerRiver(1L, ☃);
    ☃ = new GenLayerSmooth(1000L, ☃);
    
    ☃ = new GenLayerPlains(1001L, ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      ☃ = new GenLayerZoom(1000 + ☃, ☃);
      if (☃ == 0) {
        ☃ = new GenLayerIsland(3L, ☃);
      }
      if ((☃ == 1) || (☃ == 1)) {
        ☃ = new GenLayerMushroomShore(1000L, ☃);
      }
    }
    ☃ = new GenLayerSmooth(1000L, ☃);
    
    ☃ = new GenLayerRiverMix(100L, ☃, ☃);
    
    GenLayer ☃ = ☃;
    GenLayer ☃ = new GenLayerZoomVoronoi(10L, ☃);
    
    ☃.a(☃);
    ☃.a(☃);
    
    return new GenLayer[] { ☃, ☃, ☃ };
  }
  
  public GenLayer(long ☃)
  {
    this.b = ☃;
    this.b *= (this.b * 6364136223846793005L + 1442695040888963407L);
    this.b += ☃;
    this.b *= (this.b * 6364136223846793005L + 1442695040888963407L);
    this.b += ☃;
    this.b *= (this.b * 6364136223846793005L + 1442695040888963407L);
    this.b += ☃;
  }
  
  public void a(long ☃)
  {
    this.c = ☃;
    if (this.a != null) {
      this.a.a(☃);
    }
    this.c *= (this.c * 6364136223846793005L + 1442695040888963407L);
    this.c += this.b;
    this.c *= (this.c * 6364136223846793005L + 1442695040888963407L);
    this.c += this.b;
    this.c *= (this.c * 6364136223846793005L + 1442695040888963407L);
    this.c += this.b;
  }
  
  public void a(long ☃, long ☃)
  {
    this.d = this.c;
    this.d *= (this.d * 6364136223846793005L + 1442695040888963407L);
    this.d += ☃;
    this.d *= (this.d * 6364136223846793005L + 1442695040888963407L);
    this.d += ☃;
    this.d *= (this.d * 6364136223846793005L + 1442695040888963407L);
    this.d += ☃;
    this.d *= (this.d * 6364136223846793005L + 1442695040888963407L);
    this.d += ☃;
  }
  
  protected int a(int ☃)
  {
    int ☃ = (int)((this.d >> 24) % ☃);
    if (☃ < 0) {
      ☃ += ☃;
    }
    this.d *= (this.d * 6364136223846793005L + 1442695040888963407L);
    this.d += this.c;
    return ☃;
  }
  
  public abstract int[] a(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  protected static boolean a(int ☃, int ☃)
  {
    if (☃ == ☃) {
      return true;
    }
    if ((☃ == BiomeBase.MESA_PLATEAU_F.id) || (☃ == BiomeBase.MESA_PLATEAU.id)) {
      return (☃ == BiomeBase.MESA_PLATEAU_F.id) || (☃ == BiomeBase.MESA_PLATEAU.id);
    }
    BiomeBase ☃ = BiomeBase.getBiome(☃);
    BiomeBase ☃ = BiomeBase.getBiome(☃);
    try
    {
      if ((☃ != null) && (☃ != null)) {
        return ☃.a(☃);
      }
    }
    catch (Throwable ☃)
    {
      CrashReport ☃ = CrashReport.a(☃, "Comparing biomes");
      CrashReportSystemDetails ☃ = ☃.a("Biomes being compared");
      
      ☃.a("Biome A ID", Integer.valueOf(☃));
      ☃.a("Biome B ID", Integer.valueOf(☃));
      
      ☃.a("Biome A", new Callable()
      {
        public String a()
          throws Exception
        {
          return String.valueOf(this.a);
        }
      });
      ☃.a("Biome B", new Callable()
      {
        public String a()
          throws Exception
        {
          return String.valueOf(this.a);
        }
      });
      throw new ReportedException(☃);
    }
    return false;
  }
  
  protected static boolean b(int ☃)
  {
    return (☃ == BiomeBase.OCEAN.id) || (☃ == BiomeBase.DEEP_OCEAN.id) || (☃ == BiomeBase.FROZEN_OCEAN.id);
  }
  
  protected int a(int... ☃)
  {
    return ☃[a(☃.length)];
  }
  
  protected int b(int ☃, int ☃, int ☃, int ☃)
  {
    if ((☃ == ☃) && (☃ == ☃)) {
      return ☃;
    }
    if ((☃ == ☃) && (☃ == ☃)) {
      return ☃;
    }
    if ((☃ == ☃) && (☃ == ☃)) {
      return ☃;
    }
    if ((☃ == ☃) && (☃ == ☃)) {
      return ☃;
    }
    if ((☃ == ☃) && (☃ != ☃)) {
      return ☃;
    }
    if ((☃ == ☃) && (☃ != ☃)) {
      return ☃;
    }
    if ((☃ == ☃) && (☃ != ☃)) {
      return ☃;
    }
    if ((☃ == ☃) && (☃ != ☃)) {
      return ☃;
    }
    if ((☃ == ☃) && (☃ != ☃)) {
      return ☃;
    }
    if ((☃ == ☃) && (☃ != ☃)) {
      return ☃;
    }
    return a(new int[] { ☃, ☃, ☃, ☃ });
  }
}
