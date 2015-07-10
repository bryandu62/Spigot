package net.minecraft.server.v1_8_R3;

public class PathPoint
{
  public final int a;
  public final int b;
  public final int c;
  private final int j;
  int d = -1;
  float e;
  float f;
  float g;
  PathPoint h;
  public boolean i;
  
  public PathPoint(int ☃, int ☃, int ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    
    this.j = a(☃, ☃, ☃);
  }
  
  public static int a(int ☃, int ☃, int ☃)
  {
    return ☃ & 0xFF | (☃ & 0x7FFF) << 8 | (☃ & 0x7FFF) << 24 | (☃ < 0 ? Integer.MIN_VALUE : 0) | (☃ < 0 ? 32768 : 0);
  }
  
  public float a(PathPoint ☃)
  {
    float ☃ = ☃.a - this.a;
    float ☃ = ☃.b - this.b;
    float ☃ = ☃.c - this.c;
    return MathHelper.c(☃ * ☃ + ☃ * ☃ + ☃ * ☃);
  }
  
  public float b(PathPoint ☃)
  {
    float ☃ = ☃.a - this.a;
    float ☃ = ☃.b - this.b;
    float ☃ = ☃.c - this.c;
    return ☃ * ☃ + ☃ * ☃ + ☃ * ☃;
  }
  
  public boolean equals(Object ☃)
  {
    if ((☃ instanceof PathPoint))
    {
      PathPoint ☃ = (PathPoint)☃;
      return (this.j == ☃.j) && (this.a == ☃.a) && (this.b == ☃.b) && (this.c == ☃.c);
    }
    return false;
  }
  
  public int hashCode()
  {
    return this.j;
  }
  
  public boolean a()
  {
    return this.d >= 0;
  }
  
  public String toString()
  {
    return this.a + ", " + this.b + ", " + this.c;
  }
}
