package net.minecraft.server.v1_8_R3;

public class GenLayerMushroomIsland
  extends GenLayer
{
  public GenLayerMushroomIsland(long ☃, GenLayer ☃)
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
        if ((☃ == 0) && (☃ == 0) && (☃ == 0) && (☃ == 0) && (☃ == 0) && (a(100) == 0)) {
          ☃[(☃ + ☃ * ☃)] = BiomeBase.MUSHROOM_ISLAND.id;
        } else {
          ☃[(☃ + ☃ * ☃)] = ☃;
        }
      }
    }
    return ☃;
  }
}
