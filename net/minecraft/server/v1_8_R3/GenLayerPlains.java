package net.minecraft.server.v1_8_R3;

public class GenLayerPlains
  extends GenLayer
{
  public GenLayerPlains(long ☃, GenLayer ☃)
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
        if (a(57) == 0)
        {
          if (☃ == BiomeBase.PLAINS.id) {
            ☃[(☃ + ☃ * ☃)] = (BiomeBase.PLAINS.id + 128);
          } else {
            ☃[(☃ + ☃ * ☃)] = ☃;
          }
        }
        else {
          ☃[(☃ + ☃ * ☃)] = ☃;
        }
      }
    }
    return ☃;
  }
}
