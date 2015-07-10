package net.minecraft.server.v1_8_R3;

public class GenLayerRiverMix
  extends GenLayer
{
  private GenLayer c;
  private GenLayer d;
  
  public GenLayerRiverMix(long ☃, GenLayer ☃, GenLayer ☃)
  {
    super(☃);
    this.c = ☃;
    this.d = ☃;
  }
  
  public void a(long ☃)
  {
    this.c.a(☃);
    this.d.a(☃);
    super.a(☃);
  }
  
  public int[] a(int ☃, int ☃, int ☃, int ☃)
  {
    int[] ☃ = this.c.a(☃, ☃, ☃, ☃);
    int[] ☃ = this.d.a(☃, ☃, ☃, ☃);
    
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃ * ☃; ☃++) {
      if ((☃[☃] == BiomeBase.OCEAN.id) || (☃[☃] == BiomeBase.DEEP_OCEAN.id)) {
        ☃[☃] = ☃[☃];
      } else if (☃[☃] == BiomeBase.RIVER.id)
      {
        if (☃[☃] == BiomeBase.ICE_PLAINS.id) {
          ☃[☃] = BiomeBase.FROZEN_RIVER.id;
        } else if ((☃[☃] == BiomeBase.MUSHROOM_ISLAND.id) || (☃[☃] == BiomeBase.MUSHROOM_SHORE.id)) {
          ☃[☃] = BiomeBase.MUSHROOM_SHORE.id;
        } else {
          ☃[☃] &= 0xFF;
        }
      }
      else {
        ☃[☃] = ☃[☃];
      }
    }
    return ☃;
  }
}
