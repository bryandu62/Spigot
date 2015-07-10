package net.minecraft.server.v1_8_R3;

public class GenLayerZoom
  extends GenLayer
{
  public GenLayerZoom(long ☃, GenLayer ☃)
  {
    super(☃);
    this.a = ☃;
  }
  
  public int[] a(int ☃, int ☃, int ☃, int ☃)
  {
    int ☃ = ☃ >> 1;
    int ☃ = ☃ >> 1;
    int ☃ = (☃ >> 1) + 2;
    int ☃ = (☃ >> 1) + 2;
    int[] ☃ = this.a.a(☃, ☃, ☃, ☃);
    
    int ☃ = ☃ - 1 << 1;
    int ☃ = ☃ - 1 << 1;
    
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃ - 1; ☃++)
    {
      int ☃ = (☃ << 1) * ☃;
      
      int ☃ = 0;
      int ☃ = ☃[(☃ + 0 + (☃ + 0) * ☃)];
      int ☃ = ☃[(☃ + 0 + (☃ + 1) * ☃)];
      for (; ☃ < ☃ - 1; ☃++)
      {
        a(☃ + ☃ << 1, ☃ + ☃ << 1);
        
        int ☃ = ☃[(☃ + 1 + (☃ + 0) * ☃)];
        int ☃ = ☃[(☃ + 1 + (☃ + 1) * ☃)];
        
        ☃[☃] = ☃;
        ☃[(☃++ + ☃)] = a(new int[] { ☃, ☃ });
        ☃[☃] = a(new int[] { ☃, ☃ });
        ☃[(☃++ + ☃)] = b(☃, ☃, ☃, ☃);
        
        ☃ = ☃;
        ☃ = ☃;
      }
    }
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      System.arraycopy(☃, (☃ + (☃ & 0x1)) * ☃ + (☃ & 0x1), ☃, ☃ * ☃, ☃);
    }
    return ☃;
  }
  
  public static GenLayer b(long ☃, GenLayer ☃, int ☃)
  {
    GenLayer ☃ = ☃;
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      ☃ = new GenLayerZoom(☃ + ☃, ☃);
    }
    return ☃;
  }
}
