package net.minecraft.server.v1_8_R3;

public class LayerIsland
  extends GenLayer
{
  public LayerIsland(long ☃)
  {
    super(☃);
  }
  
  public int[] a(int ☃, int ☃, int ☃, int ☃)
  {
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        a(☃ + ☃, ☃ + ☃);
        ☃[(☃ + ☃ * ☃)] = (a(10) == 0 ? 1 : 0);
      }
    }
    if ((☃ > -☃) && (☃ <= 0) && (☃ > -☃) && (☃ <= 0)) {
      ☃[(-☃ + -☃ * ☃)] = 1;
    }
    return ☃;
  }
}
