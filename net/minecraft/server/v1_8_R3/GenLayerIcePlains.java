package net.minecraft.server.v1_8_R3;

public class GenLayerIcePlains
  extends GenLayer
{
  public GenLayerIcePlains(long ☃, GenLayer ☃)
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
        int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * (☃ + 2))];
        int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * (☃ + 2))];
        int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * (☃ + 2))];
        int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * (☃ + 2))];
        
        int ☃ = ☃[(☃ + 1 + (☃ + 1) * ☃)];
        ☃[(☃ + ☃ * ☃)] = ☃;
        a(☃ + ☃, ☃ + ☃);
        if ((☃ == 0) && (☃ == 0) && (☃ == 0) && (☃ == 0) && (☃ == 0) && (a(2) == 0)) {
          ☃[(☃ + ☃ * ☃)] = 1;
        }
      }
    }
    return ☃;
  }
}
