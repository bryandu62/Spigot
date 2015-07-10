package net.minecraft.server.v1_8_R3;

public class GenLayerBiome
  extends GenLayer
{
  private BiomeBase[] c = { BiomeBase.DESERT, BiomeBase.DESERT, BiomeBase.DESERT, BiomeBase.SAVANNA, BiomeBase.SAVANNA, BiomeBase.PLAINS };
  private BiomeBase[] d = { BiomeBase.FOREST, BiomeBase.ROOFED_FOREST, BiomeBase.EXTREME_HILLS, BiomeBase.PLAINS, BiomeBase.BIRCH_FOREST, BiomeBase.SWAMPLAND };
  private BiomeBase[] e = { BiomeBase.FOREST, BiomeBase.EXTREME_HILLS, BiomeBase.TAIGA, BiomeBase.PLAINS };
  private BiomeBase[] f = { BiomeBase.ICE_PLAINS, BiomeBase.ICE_PLAINS, BiomeBase.ICE_PLAINS, BiomeBase.COLD_TAIGA };
  private final CustomWorldSettingsFinal g;
  
  public GenLayerBiome(long ☃, GenLayer ☃, WorldType ☃, String ☃)
  {
    super(☃);
    this.a = ☃;
    if (☃ == WorldType.NORMAL_1_1)
    {
      this.c = new BiomeBase[] { BiomeBase.DESERT, BiomeBase.FOREST, BiomeBase.EXTREME_HILLS, BiomeBase.SWAMPLAND, BiomeBase.PLAINS, BiomeBase.TAIGA };
      
      this.g = null;
    }
    else if (☃ == WorldType.CUSTOMIZED)
    {
      this.g = CustomWorldSettingsFinal.CustomWorldSettings.a(☃).b();
    }
    else
    {
      this.g = null;
    }
  }
  
  public int[] a(int ☃, int ☃, int ☃, int ☃)
  {
    int[] ☃ = this.a.a(☃, ☃, ☃, ☃);
    
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        a(☃ + ☃, ☃ + ☃);
        int ☃ = ☃[(☃ + ☃ * ☃)];
        int ☃ = (☃ & 0xF00) >> 8;
        ☃ &= 0xF0FF;
        if ((this.g != null) && (this.g.F >= 0)) {
          ☃[(☃ + ☃ * ☃)] = this.g.F;
        } else if (b(☃)) {
          ☃[(☃ + ☃ * ☃)] = ☃;
        } else if (☃ == BiomeBase.MUSHROOM_ISLAND.id) {
          ☃[(☃ + ☃ * ☃)] = ☃;
        } else if (☃ == 1)
        {
          if (☃ > 0)
          {
            if (a(3) == 0) {
              ☃[(☃ + ☃ * ☃)] = BiomeBase.MESA_PLATEAU.id;
            } else {
              ☃[(☃ + ☃ * ☃)] = BiomeBase.MESA_PLATEAU_F.id;
            }
          }
          else {
            ☃[(☃ + ☃ * ☃)] = this.c[a(this.c.length)].id;
          }
        }
        else if (☃ == 2)
        {
          if (☃ > 0) {
            ☃[(☃ + ☃ * ☃)] = BiomeBase.JUNGLE.id;
          } else {
            ☃[(☃ + ☃ * ☃)] = this.d[a(this.d.length)].id;
          }
        }
        else if (☃ == 3)
        {
          if (☃ > 0) {
            ☃[(☃ + ☃ * ☃)] = BiomeBase.MEGA_TAIGA.id;
          } else {
            ☃[(☃ + ☃ * ☃)] = this.e[a(this.e.length)].id;
          }
        }
        else if (☃ == 4) {
          ☃[(☃ + ☃ * ☃)] = this.f[a(this.f.length)].id;
        } else {
          ☃[(☃ + ☃ * ☃)] = BiomeBase.MUSHROOM_ISLAND.id;
        }
      }
    }
    return ☃;
  }
}
