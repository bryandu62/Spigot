package net.minecraft.server.v1_8_R3;

public class GenLayerIsland
  extends GenLayer
{
  public GenLayerIsland(long ☃, GenLayer ☃)
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
        int ☃ = ☃[(☃ + 0 + (☃ + 0) * ☃)];
        int ☃ = ☃[(☃ + 2 + (☃ + 0) * ☃)];
        int ☃ = ☃[(☃ + 0 + (☃ + 2) * ☃)];
        int ☃ = ☃[(☃ + 2 + (☃ + 2) * ☃)];
        int ☃ = ☃[(☃ + 1 + (☃ + 1) * ☃)];
        a(☃ + ☃, ☃ + ☃);
        if ((☃ == 0) && ((☃ != 0) || (☃ != 0) || (☃ != 0) || (☃ != 0)))
        {
          int ☃ = 1;
          int ☃ = 1;
          if ((☃ != 0) && (a(☃++) == 0)) {
            ☃ = ☃;
          }
          if ((☃ != 0) && (a(☃++) == 0)) {
            ☃ = ☃;
          }
          if ((☃ != 0) && (a(☃++) == 0)) {
            ☃ = ☃;
          }
          if ((☃ != 0) && (a(☃++) == 0)) {
            ☃ = ☃;
          }
          if (a(3) == 0) {
            ☃[(☃ + ☃ * ☃)] = ☃;
          } else if (☃ == 4) {
            ☃[(☃ + ☃ * ☃)] = 4;
          } else {
            ☃[(☃ + ☃ * ☃)] = 0;
          }
        }
        else if ((☃ > 0) && ((☃ == 0) || (☃ == 0) || (☃ == 0) || (☃ == 0)))
        {
          if (a(5) == 0)
          {
            if (☃ == 4) {
              ☃[(☃ + ☃ * ☃)] = 4;
            } else {
              ☃[(☃ + ☃ * ☃)] = 0;
            }
          }
          else {
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
}
