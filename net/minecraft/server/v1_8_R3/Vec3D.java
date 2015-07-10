package net.minecraft.server.v1_8_R3;

public class Vec3D
{
  public final double a;
  public final double b;
  public final double c;
  
  public Vec3D(double ☃, double ☃, double ☃)
  {
    if (☃ == -0.0D) {
      ☃ = 0.0D;
    }
    if (☃ == -0.0D) {
      ☃ = 0.0D;
    }
    if (☃ == -0.0D) {
      ☃ = 0.0D;
    }
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
  }
  
  public Vec3D(BaseBlockPosition ☃)
  {
    this(☃.getX(), ☃.getY(), ☃.getZ());
  }
  
  public Vec3D a()
  {
    double ☃ = MathHelper.sqrt(this.a * this.a + this.b * this.b + this.c * this.c);
    if (☃ < 1.0E-4D) {
      return new Vec3D(0.0D, 0.0D, 0.0D);
    }
    return new Vec3D(this.a / ☃, this.b / ☃, this.c / ☃);
  }
  
  public double b(Vec3D ☃)
  {
    return this.a * ☃.a + this.b * ☃.b + this.c * ☃.c;
  }
  
  public Vec3D d(Vec3D ☃)
  {
    return a(☃.a, ☃.b, ☃.c);
  }
  
  public Vec3D a(double ☃, double ☃, double ☃)
  {
    return add(-☃, -☃, -☃);
  }
  
  public Vec3D e(Vec3D ☃)
  {
    return add(☃.a, ☃.b, ☃.c);
  }
  
  public Vec3D add(double ☃, double ☃, double ☃)
  {
    return new Vec3D(this.a + ☃, this.b + ☃, this.c + ☃);
  }
  
  public double distanceSquared(Vec3D ☃)
  {
    double ☃ = ☃.a - this.a;
    double ☃ = ☃.b - this.b;
    double ☃ = ☃.c - this.c;
    return ☃ * ☃ + ☃ * ☃ + ☃ * ☃;
  }
  
  public double b()
  {
    return MathHelper.sqrt(this.a * this.a + this.b * this.b + this.c * this.c);
  }
  
  public Vec3D a(Vec3D ☃, double ☃)
  {
    double ☃ = ☃.a - this.a;
    double ☃ = ☃.b - this.b;
    double ☃ = ☃.c - this.c;
    if (☃ * ☃ < 1.0000000116860974E-7D) {
      return null;
    }
    double ☃ = (☃ - this.a) / ☃;
    if ((☃ < 0.0D) || (☃ > 1.0D)) {
      return null;
    }
    return new Vec3D(this.a + ☃ * ☃, this.b + ☃ * ☃, this.c + ☃ * ☃);
  }
  
  public Vec3D b(Vec3D ☃, double ☃)
  {
    double ☃ = ☃.a - this.a;
    double ☃ = ☃.b - this.b;
    double ☃ = ☃.c - this.c;
    if (☃ * ☃ < 1.0000000116860974E-7D) {
      return null;
    }
    double ☃ = (☃ - this.b) / ☃;
    if ((☃ < 0.0D) || (☃ > 1.0D)) {
      return null;
    }
    return new Vec3D(this.a + ☃ * ☃, this.b + ☃ * ☃, this.c + ☃ * ☃);
  }
  
  public Vec3D c(Vec3D ☃, double ☃)
  {
    double ☃ = ☃.a - this.a;
    double ☃ = ☃.b - this.b;
    double ☃ = ☃.c - this.c;
    if (☃ * ☃ < 1.0000000116860974E-7D) {
      return null;
    }
    double ☃ = (☃ - this.c) / ☃;
    if ((☃ < 0.0D) || (☃ > 1.0D)) {
      return null;
    }
    return new Vec3D(this.a + ☃ * ☃, this.b + ☃ * ☃, this.c + ☃ * ☃);
  }
  
  public String toString()
  {
    return "(" + this.a + ", " + this.b + ", " + this.c + ")";
  }
  
  public Vec3D a(float ☃)
  {
    float ☃ = MathHelper.cos(☃);
    float ☃ = MathHelper.sin(☃);
    
    double ☃ = this.a;
    double ☃ = this.b * ☃ + this.c * ☃;
    double ☃ = this.c * ☃ - this.b * ☃;
    
    return new Vec3D(☃, ☃, ☃);
  }
  
  public Vec3D b(float ☃)
  {
    float ☃ = MathHelper.cos(☃);
    float ☃ = MathHelper.sin(☃);
    
    double ☃ = this.a * ☃ + this.c * ☃;
    double ☃ = this.b;
    double ☃ = this.c * ☃ - this.a * ☃;
    
    return new Vec3D(☃, ☃, ☃);
  }
}
