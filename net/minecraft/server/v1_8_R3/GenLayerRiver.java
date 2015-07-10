package net.minecraft.server.v1_8_R3;

public class GenLayerRiver
  extends GenLayer
{
  public GenLayerRiver(long ☃, GenLayer ☃)
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
        int ☃ = c(☃[(☃ + 0 + (☃ + 1) * ☃)]);
        int ☃ = c(☃[(☃ + 2 + (☃ + 1) * ☃)]);
        int ☃ = c(☃[(☃ + 1 + (☃ + 0) * ☃)]);
        int ☃ = c(☃[(☃ + 1 + (☃ + 2) * ☃)]);
        int ☃ = c(☃[(☃ + 1 + (☃ + 1) * ☃)]);
        if ((☃ != ☃) || (☃ != ☃) || (☃ != ☃) || (☃ != ☃)) {
          ☃[(☃ + ☃ * ☃)] = BiomeBase.RIVER.id;
        } else {
          ☃[(☃ + ☃ * ☃)] = -1;
        }
      }
    }
    return ☃;
  }
  
  private int c(int ☃)
  {
    if (☃ >= 2) {
      return 2 + (☃ & 0x1);
    }
    return ☃;
  }
}
