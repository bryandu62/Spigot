package net.minecraft.server.v1_8_R3;

public class GenLayerMushroomShore
  extends GenLayer
{
  public GenLayerMushroomShore(long ☃, GenLayer ☃)
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
        BiomeBase ☃ = BiomeBase.getBiome(☃);
        if (☃ == BiomeBase.MUSHROOM_ISLAND.id)
        {
          int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * (☃ + 2))];
          if ((☃ == BiomeBase.OCEAN.id) || (☃ == BiomeBase.OCEAN.id) || (☃ == BiomeBase.OCEAN.id) || (☃ == BiomeBase.OCEAN.id)) {
            ☃[(☃ + ☃ * ☃)] = BiomeBase.MUSHROOM_SHORE.id;
          } else {
            ☃[(☃ + ☃ * ☃)] = ☃;
          }
        }
        else if ((☃ != null) && (☃.l() == BiomeJungle.class))
        {
          int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * (☃ + 2))];
          if ((!c(☃)) || (!c(☃)) || (!c(☃)) || (!c(☃))) {
            ☃[(☃ + ☃ * ☃)] = BiomeBase.JUNGLE_EDGE.id;
          } else if ((b(☃)) || (b(☃)) || (b(☃)) || (b(☃))) {
            ☃[(☃ + ☃ * ☃)] = BiomeBase.BEACH.id;
          } else {
            ☃[(☃ + ☃ * ☃)] = ☃;
          }
        }
        else if ((☃ == BiomeBase.EXTREME_HILLS.id) || (☃ == BiomeBase.EXTREME_HILLS_PLUS.id) || (☃ == BiomeBase.SMALL_MOUNTAINS.id))
        {
          a(☃, ☃, ☃, ☃, ☃, ☃, BiomeBase.STONE_BEACH.id);
        }
        else if ((☃ != null) && (☃.j()))
        {
          a(☃, ☃, ☃, ☃, ☃, ☃, BiomeBase.COLD_BEACH.id);
        }
        else if ((☃ == BiomeBase.MESA.id) || (☃ == BiomeBase.MESA_PLATEAU_F.id))
        {
          int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * (☃ + 2))];
          if ((b(☃)) || (b(☃)) || (b(☃)) || (b(☃))) {
            ☃[(☃ + ☃ * ☃)] = ☃;
          } else if ((!d(☃)) || (!d(☃)) || (!d(☃)) || (!d(☃))) {
            ☃[(☃ + ☃ * ☃)] = BiomeBase.DESERT.id;
          } else {
            ☃[(☃ + ☃ * ☃)] = ☃;
          }
        }
        else if ((☃ != BiomeBase.OCEAN.id) && (☃ != BiomeBase.DEEP_OCEAN.id) && (☃ != BiomeBase.RIVER.id) && (☃ != BiomeBase.SWAMPLAND.id))
        {
          int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * (☃ + 2))];
          int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * (☃ + 2))];
          if ((b(☃)) || (b(☃)) || (b(☃)) || (b(☃))) {
            ☃[(☃ + ☃ * ☃)] = BiomeBase.BEACH.id;
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
    return ☃;
  }
  
  private void a(int[] ☃, int[] ☃, int ☃, int ☃, int ☃, int ☃, int ☃)
  {
    if (b(☃))
    {
      ☃[(☃ + ☃ * ☃)] = ☃;
      return;
    }
    int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * (☃ + 2))];
    int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * (☃ + 2))];
    int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * (☃ + 2))];
    int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * (☃ + 2))];
    if ((b(☃)) || (b(☃)) || (b(☃)) || (b(☃))) {
      ☃[(☃ + ☃ * ☃)] = ☃;
    } else {
      ☃[(☃ + ☃ * ☃)] = ☃;
    }
  }
  
  private boolean c(int ☃)
  {
    if ((BiomeBase.getBiome(☃) != null) && (BiomeBase.getBiome(☃).l() == BiomeJungle.class)) {
      return true;
    }
    return (☃ == BiomeBase.JUNGLE_EDGE.id) || (☃ == BiomeBase.JUNGLE.id) || (☃ == BiomeBase.JUNGLE_HILLS.id) || (☃ == BiomeBase.FOREST.id) || (☃ == BiomeBase.TAIGA.id) || (b(☃));
  }
  
  private boolean d(int ☃)
  {
    return BiomeBase.getBiome(☃) instanceof BiomeMesa;
  }
}
