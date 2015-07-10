package net.minecraft.server.v1_8_R3;

public class PathEntity
{
  private final PathPoint[] a;
  private int b;
  private int c;
  
  public PathEntity(PathPoint[] ☃)
  {
    this.a = ☃;
    this.c = ☃.length;
  }
  
  public void a()
  {
    this.b += 1;
  }
  
  public boolean b()
  {
    return this.b >= this.c;
  }
  
  public PathPoint c()
  {
    if (this.c > 0) {
      return this.a[(this.c - 1)];
    }
    return null;
  }
  
  public PathPoint a(int ☃)
  {
    return this.a[☃];
  }
  
  public int d()
  {
    return this.c;
  }
  
  public void b(int ☃)
  {
    this.c = ☃;
  }
  
  public int e()
  {
    return this.b;
  }
  
  public void c(int ☃)
  {
    this.b = ☃;
  }
  
  public Vec3D a(Entity ☃, int ☃)
  {
    double ☃ = this.a[☃].a + (int)(☃.width + 1.0F) * 0.5D;
    double ☃ = this.a[☃].b;
    double ☃ = this.a[☃].c + (int)(☃.width + 1.0F) * 0.5D;
    return new Vec3D(☃, ☃, ☃);
  }
  
  public Vec3D a(Entity ☃)
  {
    return a(☃, this.b);
  }
  
  public boolean a(PathEntity ☃)
  {
    if (☃ == null) {
      return false;
    }
    if (☃.a.length != this.a.length) {
      return false;
    }
    for (int ☃ = 0; ☃ < this.a.length; ☃++) {
      if ((this.a[☃].a != ☃.a[☃].a) || (this.a[☃].b != ☃.a[☃].b) || (this.a[☃].c != ☃.a[☃].c)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean b(Vec3D ☃)
  {
    PathPoint ☃ = c();
    if (☃ == null) {
      return false;
    }
    return (☃.a == (int)☃.a) && (☃.c == (int)☃.c);
  }
}
