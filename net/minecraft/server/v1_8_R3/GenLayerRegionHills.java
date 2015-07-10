package net.minecraft.server.v1_8_R3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenLayerRegionHills
  extends GenLayer
{
  private static final Logger c = ;
  private GenLayer d;
  
  public GenLayerRegionHills(long ☃, GenLayer ☃, GenLayer ☃)
  {
    super(☃);
    this.a = ☃;
    this.d = ☃;
  }
  
  public int[] a(int ☃, int ☃, int ☃, int ☃)
  {
    int[] ☃ = this.a.a(☃ - 1, ☃ - 1, ☃ + 2, ☃ + 2);
    int[] ☃ = this.d.a(☃ - 1, ☃ - 1, ☃ + 2, ☃ + 2);
    
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        a(☃ + ☃, ☃ + ☃);
        int ☃ = ☃[(☃ + 1 + (☃ + 1) * (☃ + 2))];
        int ☃ = ☃[(☃ + 1 + (☃ + 1) * (☃ + 2))];
        boolean ☃ = (☃ - 2) % 29 == 0;
        if (☃ > 255) {
          c.debug("old! " + ☃);
        }
        if ((☃ != 0) && (☃ >= 2) && ((☃ - 2) % 29 == 1) && (☃ < 128))
        {
          if (BiomeBase.getBiome(☃ + 128) != null) {
            ☃[(☃ + ☃ * ☃)] = (☃ + 128);
          } else {
            ☃[(☃ + ☃ * ☃)] = ☃;
          }
        }
        else if ((a(3) == 0) || (☃))
        {
          int ☃ = ☃;
          if (☃ == BiomeBase.DESERT.id)
          {
            ☃ = BiomeBase.DESERT_HILLS.id;
          }
          else if (☃ == BiomeBase.FOREST.id)
          {
            ☃ = BiomeBase.FOREST_HILLS.id;
          }
          else if (☃ == BiomeBase.BIRCH_FOREST.id)
          {
            ☃ = BiomeBase.BIRCH_FOREST_HILLS.id;
          }
          else if (☃ == BiomeBase.ROOFED_FOREST.id)
          {
            ☃ = BiomeBase.PLAINS.id;
          }
          else if (☃ == BiomeBase.TAIGA.id)
          {
            ☃ = BiomeBase.TAIGA_HILLS.id;
          }
          else if (☃ == BiomeBase.MEGA_TAIGA.id)
          {
            ☃ = BiomeBase.MEGA_TAIGA_HILLS.id;
          }
          else if (☃ == BiomeBase.COLD_TAIGA.id)
          {
            ☃ = BiomeBase.COLD_TAIGA_HILLS.id;
          }
          else if (☃ == BiomeBase.PLAINS.id)
          {
            if (a(3) == 0) {
              ☃ = BiomeBase.FOREST_HILLS.id;
            } else {
              ☃ = BiomeBase.FOREST.id;
            }
          }
          else if (☃ == BiomeBase.ICE_PLAINS.id)
          {
            ☃ = BiomeBase.ICE_MOUNTAINS.id;
          }
          else if (☃ == BiomeBase.JUNGLE.id)
          {
            ☃ = BiomeBase.JUNGLE_HILLS.id;
          }
          else if (☃ == BiomeBase.OCEAN.id)
          {
            ☃ = BiomeBase.DEEP_OCEAN.id;
          }
          else if (☃ == BiomeBase.EXTREME_HILLS.id)
          {
            ☃ = BiomeBase.EXTREME_HILLS_PLUS.id;
          }
          else if (☃ == BiomeBase.SAVANNA.id)
          {
            ☃ = BiomeBase.SAVANNA_PLATEAU.id;
          }
          else if (a(☃, BiomeBase.MESA_PLATEAU_F.id))
          {
            ☃ = BiomeBase.MESA.id;
          }
          else if ((☃ == BiomeBase.DEEP_OCEAN.id) && 
            (a(3) == 0))
          {
            int ☃ = a(2);
            if (☃ == 0) {
              ☃ = BiomeBase.PLAINS.id;
            } else {
              ☃ = BiomeBase.FOREST.id;
            }
          }
          if ((☃) && (☃ != ☃)) {
            if (BiomeBase.getBiome(☃ + 128) != null) {
              ☃ += 128;
            } else {
              ☃ = ☃;
            }
          }
          if (☃ == ☃)
          {
            ☃[(☃ + ☃ * ☃)] = ☃;
          }
          else
          {
            int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * (☃ + 2))];
            int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * (☃ + 2))];
            int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * (☃ + 2))];
            int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * (☃ + 2))];
            int ☃ = 0;
            if (a(☃, ☃)) {
              ☃++;
            }
            if (a(☃, ☃)) {
              ☃++;
            }
            if (a(☃, ☃)) {
              ☃++;
            }
            if (a(☃, ☃)) {
              ☃++;
            }
            if (☃ >= 3) {
              ☃[(☃ + ☃ * ☃)] = ☃;
            } else {
              ☃[(☃ + ☃ * ☃)] = ☃;
            }
          }
        }
        else
        {
          ☃[(☃ + ☃ * ☃)] = ☃;
        }
      }
    }
    return ☃;
  }
}
