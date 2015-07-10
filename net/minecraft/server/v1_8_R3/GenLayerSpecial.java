package net.minecraft.server.v1_8_R3;

public class GenLayerSpecial
  extends GenLayer
{
  private final EnumGenLayerSpecial c;
  
  public GenLayerSpecial(long ☃, GenLayer ☃, EnumGenLayerSpecial ☃)
  {
    super(☃);
    this.a = ☃;
    this.c = ☃;
  }
  
  public int[] a(int ☃, int ☃, int ☃, int ☃)
  {
    switch (1.a[this.c.ordinal()])
    {
    case 1: 
    default: 
      return c(☃, ☃, ☃, ☃);
    case 2: 
      return d(☃, ☃, ☃, ☃);
    }
    return e(☃, ☃, ☃, ☃);
  }
  
  private int[] c(int ☃, int ☃, int ☃, int ☃)
  {
    int ☃ = ☃ - 1;
    int ☃ = ☃ - 1;
    int ☃ = 1 + ☃ + 1;
    int ☃ = 1 + ☃ + 1;
    int[] ☃ = this.a.a(☃, ☃, ☃, ☃);
    
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        a(☃ + ☃, ☃ + ☃);
        
        int ☃ = ☃[(☃ + 1 + (☃ + 1) * ☃)];
        if (☃ == 1)
        {
          int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * ☃)];
          int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * ☃)];
          int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * ☃)];
          int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * ☃)];
          
          boolean ☃ = (☃ == 3) || (☃ == 3) || (☃ == 3) || (☃ == 3);
          boolean ☃ = (☃ == 4) || (☃ == 4) || (☃ == 4) || (☃ == 4);
          if ((☃) || (☃)) {
            ☃ = 2;
          }
        }
        ☃[(☃ + ☃ * ☃)] = ☃;
      }
    }
    return ☃;
  }
  
  private int[] d(int ☃, int ☃, int ☃, int ☃)
  {
    int ☃ = ☃ - 1;
    int ☃ = ☃ - 1;
    int ☃ = 1 + ☃ + 1;
    int ☃ = 1 + ☃ + 1;
    int[] ☃ = this.a.a(☃, ☃, ☃, ☃);
    
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        int ☃ = ☃[(☃ + 1 + (☃ + 1) * ☃)];
        if (☃ == 4)
        {
          int ☃ = ☃[(☃ + 1 + (☃ + 1 - 1) * ☃)];
          int ☃ = ☃[(☃ + 1 + 1 + (☃ + 1) * ☃)];
          int ☃ = ☃[(☃ + 1 - 1 + (☃ + 1) * ☃)];
          int ☃ = ☃[(☃ + 1 + (☃ + 1 + 1) * ☃)];
          
          boolean ☃ = (☃ == 2) || (☃ == 2) || (☃ == 2) || (☃ == 2);
          boolean ☃ = (☃ == 1) || (☃ == 1) || (☃ == 1) || (☃ == 1);
          if ((☃) || (☃)) {
            ☃ = 3;
          }
        }
        ☃[(☃ + ☃ * ☃)] = ☃;
      }
    }
    return ☃;
  }
  
  private int[] e(int ☃, int ☃, int ☃, int ☃)
  {
    int[] ☃ = this.a.a(☃, ☃, ☃, ☃);
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        a(☃ + ☃, ☃ + ☃);
        
        int ☃ = ☃[(☃ + ☃ * ☃)];
        if ((☃ != 0) && (a(13) == 0)) {
          ☃ |= 1 + a(15) << 8 & 0xF00;
        }
        ☃[(☃ + ☃ * ☃)] = ☃;
      }
    }
    return ☃;
  }
  
  public static enum EnumGenLayerSpecial
  {
    private EnumGenLayerSpecial() {}
  }
}
