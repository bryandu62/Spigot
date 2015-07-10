package net.minecraft.server.v1_8_R3;

public class GenLayerDesert
  extends GenLayer
{
  public GenLayerDesert(long ☃, GenLayer ☃)
  {
    super(☃);
    this.a = ☃;
  }
  
  public int[] a(int ☃, int ☃, int ☃, int ☃)
  {
    int[] ☃ = this.a.a(☃ - 1, ☃ - 1, ☃ + 2, ☃ + 2);
    
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        a(☃ + ☃, ☃ + ☃);
        int ☃ = ☃[(☃ + 1 + (☃ + 1) * (☃ + 2))];
        if ((!a(☃, ☃, ☃, ☃, ☃, ☃, BiomeBase.EXTREME_HILLS.id, BiomeBase.SMALL_MOUNTAINS.id)) && 
          (!b(☃, ☃, ☃, ☃, ☃, ☃, BiomeBase.MESA_PLATEAU_F.id, BiomeBase.MESA.id)) && 
          (!b(☃, ☃, ☃, ☃, ☃, ☃, BiomeBase.MESA_PLATEAU.id, BiomeBase.MESA.id)) && 
          (!b(☃, ☃, ☃, ☃, ☃, ☃, BiomeBase.MEGA_TAIGA.id, BiomeBase.TAIGA.id))) {
          if (☃ == BiomeBase.DESERT.id)
          {
            int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * (☃ + 2))];
            int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * (☃ + 2))];
            int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * (☃ + 2))];
            int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * (☃ + 2))];
            if ((☃ == BiomeBase.ICE_PLAINS.id) || (☃ == BiomeBase.ICE_PLAINS.id) || (☃ == BiomeBase.ICE_PLAINS.id) || (☃ == BiomeBase.ICE_PLAINS.id)) {
              ☃[(☃ + ☃ * ☃)] = BiomeBase.EXTREME_HILLS_PLUS.id;
            } else {
              ☃[(☃ + ☃ * ☃)] = ☃;
            }
          }
          else if (☃ == BiomeBase.SWAMPLAND.id)
          {
            int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * (☃ + 2))];
            int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * (☃ + 2))];
            int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * (☃ + 2))];
            int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * (☃ + 2))];
            if ((☃ == BiomeBase.DESERT.id) || (☃ == BiomeBase.DESERT.id) || (☃ == BiomeBase.DESERT.id) || (☃ == BiomeBase.DESERT.id) || (☃ == BiomeBase.COLD_TAIGA.id) || (☃ == BiomeBase.COLD_TAIGA.id) || (☃ == BiomeBase.COLD_TAIGA.id) || (☃ == BiomeBase.COLD_TAIGA.id) || (☃ == BiomeBase.ICE_PLAINS.id) || (☃ == BiomeBase.ICE_PLAINS.id) || (☃ == BiomeBase.ICE_PLAINS.id) || (☃ == BiomeBase.ICE_PLAINS.id)) {
              ☃[(☃ + ☃ * ☃)] = BiomeBase.PLAINS.id;
            } else if ((☃ == BiomeBase.JUNGLE.id) || (☃ == BiomeBase.JUNGLE.id) || (☃ == BiomeBase.JUNGLE.id) || (☃ == BiomeBase.JUNGLE.id)) {
              ☃[(☃ + ☃ * ☃)] = BiomeBase.JUNGLE_EDGE.id;
            } else {
              ☃[(☃ + ☃ * ☃)] = ☃;
            }
          }
          else
          {
            ☃[(☃ + ☃ * ☃)] = ☃;
          }
        }
      }
    }
    return ☃;
  }
  
  private boolean a(int[] ☃, int[] ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃)
  {
    if (a(☃, ☃))
    {
      int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * (☃ + 2))];
      int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * (☃ + 2))];
      int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * (☃ + 2))];
      int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * (☃ + 2))];
      if ((!b(☃, ☃)) || (!b(☃, ☃)) || (!b(☃, ☃)) || (!b(☃, ☃))) {
        ☃[(☃ + ☃ * ☃)] = ☃;
      } else {
        ☃[(☃ + ☃ * ☃)] = ☃;
      }
      return true;
    }
    return false;
  }
  
  private boolean b(int[] ☃, int[] ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃)
  {
    if (☃ == ☃)
    {
      int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * (☃ + 2))];
      int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * (☃ + 2))];
      int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * (☃ + 2))];
      int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * (☃ + 2))];
      if ((!a(☃, ☃)) || (!a(☃, ☃)) || (!a(☃, ☃)) || (!a(☃, ☃))) {
        ☃[(☃ + ☃ * ☃)] = ☃;
      } else {
        ☃[(☃ + ☃ * ☃)] = ☃;
      }
      return true;
    }
    return false;
  }
  
  private boolean b(int ☃, int ☃)
  {
    if (a(☃, ☃)) {
      return true;
    }
    BiomeBase ☃ = BiomeBase.getBiome(☃);
    BiomeBase ☃ = BiomeBase.getBiome(☃);
    if ((☃ != null) && (☃ != null))
    {
      BiomeBase.EnumTemperature ☃ = ☃.m();
      BiomeBase.EnumTemperature ☃ = ☃.m();
      return (☃ == ☃) || (☃ == BiomeBase.EnumTemperature.MEDIUM) || (☃ == BiomeBase.EnumTemperature.MEDIUM);
    }
    return false;
  }
}
