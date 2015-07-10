package net.minecraft.server.v1_8_R3;

public class GenLayerTopSoil
  extends GenLayer
{
  public GenLayerTopSoil(long ☃, GenLayer ☃)
  {
    super(☃);
    this.a = ☃;
  }
  
  public int[] a(int ☃, int ☃, int ☃, int ☃)
  {
    int ☃ = ☃ - 1;
    int ☃ = ☃ - 1;
    int ☃ = ☃ + 2;
    int ☃ = ☃ + 2;
    int[] ☃ = this.a.a(☃, ☃, ☃, ☃);
    
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        int ☃ = ☃[(☃ + 1 + (☃ + 1) * ☃)];
        a(☃ + ☃, ☃ + ☃);
        if (☃ == 0)
        {
          ☃[(☃ + ☃ * ☃)] = 0;
        }
        else
        {
          int ☃ = a(6);
          if (☃ == 0) {
            ☃ = 4;
          } else if (☃ <= 1) {
            ☃ = 3;
          } else {
            ☃ = 1;
          }
          ☃[(☃ + ☃ * ☃)] = ☃;
        }
      }
    }
    return ☃;
  }
}
