package net.minecraft.server.v1_8_R3;

public class GenLayerSmooth
  extends GenLayer
{
  public GenLayerSmooth(long ☃, GenLayer ☃)
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
        int ☃ = ☃[(☃ + 0 + (☃ + 1) * ☃)];
        int ☃ = ☃[(☃ + 2 + (☃ + 1) * ☃)];
        int ☃ = ☃[(☃ + 1 + (☃ + 0) * ☃)];
        int ☃ = ☃[(☃ + 1 + (☃ + 2) * ☃)];
        int ☃ = ☃[(☃ + 1 + (☃ + 1) * ☃)];
        if ((☃ == ☃) && (☃ == ☃))
        {
          a(☃ + ☃, ☃ + ☃);
          if (a(2) == 0) {
            ☃ = ☃;
          } else {
            ☃ = ☃;
          }
        }
        else
        {
          if (☃ == ☃) {
            ☃ = ☃;
          }
          if (☃ == ☃) {
            ☃ = ☃;
          }
        }
        ☃[(☃ + ☃ * ☃)] = ☃;
      }
    }
    return ☃;
  }
}
