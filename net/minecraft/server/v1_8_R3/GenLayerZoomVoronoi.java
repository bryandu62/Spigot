package net.minecraft.server.v1_8_R3;

public class GenLayerZoomVoronoi
  extends GenLayer
{
  public GenLayerZoomVoronoi(long ☃, GenLayer ☃)
  {
    super(☃);
    this.a = ☃;
  }
  
  public int[] a(int ☃, int ☃, int ☃, int ☃)
  {
    ☃ -= 2;
    ☃ -= 2;
    int ☃ = ☃ >> 2;
    int ☃ = ☃ >> 2;
    int ☃ = (☃ >> 2) + 2;
    int ☃ = (☃ >> 2) + 2;
    int[] ☃ = this.a.a(☃, ☃, ☃, ☃);
    
    int ☃ = ☃ - 1 << 2;
    int ☃ = ☃ - 1 << 2;
    
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃ - 1; ☃++)
    {
      int ☃ = 0;
      int ☃ = ☃[(☃ + 0 + (☃ + 0) * ☃)];
      int ☃ = ☃[(☃ + 0 + (☃ + 1) * ☃)];
      for (; ☃ < ☃ - 1; ☃++)
      {
        double ☃ = 3.6D;
        a(☃ + ☃ << 2, ☃ + ☃ << 2);
        double ☃ = (a(1024) / 1024.0D - 0.5D) * 3.6D;
        double ☃ = (a(1024) / 1024.0D - 0.5D) * 3.6D;
        
        a(☃ + ☃ + 1 << 2, ☃ + ☃ << 2);
        double ☃ = (a(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        double ☃ = (a(1024) / 1024.0D - 0.5D) * 3.6D;
        
        a(☃ + ☃ << 2, ☃ + ☃ + 1 << 2);
        double ☃ = (a(1024) / 1024.0D - 0.5D) * 3.6D;
        double ☃ = (a(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        
        a(☃ + ☃ + 1 << 2, ☃ + ☃ + 1 << 2);
        double ☃ = (a(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        double ☃ = (a(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        
        int ☃ = ☃[(☃ + 1 + (☃ + 0) * ☃)] & 0xFF;
        int ☃ = ☃[(☃ + 1 + (☃ + 1) * ☃)] & 0xFF;
        for (int ☃ = 0; ☃ < 4; ☃++)
        {
          int ☃ = ((☃ << 2) + ☃) * ☃ + (☃ << 2);
          for (int ☃ = 0; ☃ < 4; ☃++)
          {
            double ☃ = (☃ - ☃) * (☃ - ☃) + (☃ - ☃) * (☃ - ☃);
            double ☃ = (☃ - ☃) * (☃ - ☃) + (☃ - ☃) * (☃ - ☃);
            double ☃ = (☃ - ☃) * (☃ - ☃) + (☃ - ☃) * (☃ - ☃);
            double ☃ = (☃ - ☃) * (☃ - ☃) + (☃ - ☃) * (☃ - ☃);
            if ((☃ < ☃) && (☃ < ☃) && (☃ < ☃)) {
              ☃[(☃++)] = ☃;
            } else if ((☃ < ☃) && (☃ < ☃) && (☃ < ☃)) {
              ☃[(☃++)] = ☃;
            } else if ((☃ < ☃) && (☃ < ☃) && (☃ < ☃)) {
              ☃[(☃++)] = ☃;
            } else {
              ☃[(☃++)] = ☃;
            }
          }
        }
        ☃ = ☃;
        ☃ = ☃;
      }
    }
    int[] ☃ = IntCache.a(☃ * ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      System.arraycopy(☃, (☃ + (☃ & 0x3)) * ☃ + (☃ & 0x3), ☃, ☃ * ☃, ☃);
    }
    return ☃;
  }
}
