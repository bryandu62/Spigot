package net.minecraft.server.v1_8_R3;

public class ChunkCoordIntPair
{
  public final int x;
  public final int z;
  
  public ChunkCoordIntPair(int ☃, int ☃)
  {
    this.x = ☃;
    this.z = ☃;
  }
  
  public static long a(int ☃, int ☃)
  {
    return ☃ & 0xFFFFFFFF | (☃ & 0xFFFFFFFF) << 32;
  }
  
  public int hashCode()
  {
    int ☃ = 1664525 * this.x + 1013904223;
    int ☃ = 1664525 * (this.z ^ 0xDEADBEEF) + 1013904223;
    return ☃ ^ ☃;
  }
  
  public boolean equals(Object ☃)
  {
    if (this == ☃) {
      return true;
    }
    if ((☃ instanceof ChunkCoordIntPair))
    {
      ChunkCoordIntPair ☃ = (ChunkCoordIntPair)☃;
      
      return (this.x == ☃.x) && (this.z == ☃.z);
    }
    return false;
  }
  
  public int a()
  {
    return (this.x << 4) + 8;
  }
  
  public int b()
  {
    return (this.z << 4) + 8;
  }
  
  public int c()
  {
    return this.x << 4;
  }
  
  public int d()
  {
    return this.z << 4;
  }
  
  public int e()
  {
    return (this.x << 4) + 15;
  }
  
  public int f()
  {
    return (this.z << 4) + 15;
  }
  
  public BlockPosition a(int ☃, int ☃, int ☃)
  {
    return new BlockPosition((this.x << 4) + ☃, ☃, (this.z << 4) + ☃);
  }
  
  public BlockPosition a(int ☃)
  {
    return new BlockPosition(a(), ☃, b());
  }
  
  public String toString()
  {
    return "[" + this.x + ", " + this.z + "]";
  }
}
