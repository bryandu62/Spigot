package net.minecraft.server.v1_8_R3;

public class GenLayerCleaner
  extends GenLayer
{
  public GenLayerCleaner(long ☃, GenLayer ☃)
  {
    super(☃);
    this.a = ☃;
  }
  
  public int[] a(int ☃, int ☃, int ☃, int ☃)
  {
    int[] ☃ = this.a.a(☃, ☃, ☃, ☃);
    
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        a(☃ + ☃, ☃ + ☃);
        ☃[(☃ + ☃ * ☃)] = (☃[(☃ + ☃ * ☃)] > 0 ? a(299999) + 2 : 0);
      }
    }
    return ☃;
  }
}
