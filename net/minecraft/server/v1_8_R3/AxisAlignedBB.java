package net.minecraft.server.v1_8_R3;

public class AxisAlignedBB
{
  public final double a;
  public final double b;
  public final double c;
  public final double d;
  public final double e;
  public final double f;
  
  public AxisAlignedBB(double ☃, double ☃, double ☃, double ☃, double ☃, double ☃)
  {
    this.a = Math.min(☃, ☃);
    this.b = Math.min(☃, ☃);
    this.c = Math.min(☃, ☃);
    this.d = Math.max(☃, ☃);
    this.e = Math.max(☃, ☃);
    this.f = Math.max(☃, ☃);
  }
  
  public AxisAlignedBB(BlockPosition ☃, BlockPosition ☃)
  {
    this.a = ☃.getX();
    this.b = ☃.getY();
    this.c = ☃.getZ();
    this.d = ☃.getX();
    this.e = ☃.getY();
    this.f = ☃.getZ();
  }
  
  public AxisAlignedBB a(double ☃, double ☃, double ☃)
  {
    double ☃ = this.a;
    double ☃ = this.b;
    double ☃ = this.c;
    double ☃ = this.d;
    double ☃ = this.e;
    double ☃ = this.f;
    if (☃ < 0.0D) {
      ☃ += ☃;
    } else if (☃ > 0.0D) {
      ☃ += ☃;
    }
    if (☃ < 0.0D) {
      ☃ += ☃;
    } else if (☃ > 0.0D) {
      ☃ += ☃;
    }
    if (☃ < 0.0D) {
      ☃ += ☃;
    } else if (☃ > 0.0D) {
      ☃ += ☃;
    }
    return new AxisAlignedBB(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  public AxisAlignedBB grow(double ☃, double ☃, double ☃)
  {
    double ☃ = this.a - ☃;
    double ☃ = this.b - ☃;
    double ☃ = this.c - ☃;
    double ☃ = this.d + ☃;
    double ☃ = this.e + ☃;
    double ☃ = this.f + ☃;
    
    return new AxisAlignedBB(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  public AxisAlignedBB a(AxisAlignedBB ☃)
  {
    double ☃ = Math.min(this.a, ☃.a);
    double ☃ = Math.min(this.b, ☃.b);
    double ☃ = Math.min(this.c, ☃.c);
    double ☃ = Math.max(this.d, ☃.d);
    double ☃ = Math.max(this.e, ☃.e);
    double ☃ = Math.max(this.f, ☃.f);
    
    return new AxisAlignedBB(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  public static AxisAlignedBB a(double ☃, double ☃, double ☃, double ☃, double ☃, double ☃)
  {
    double ☃ = Math.min(☃, ☃);
    double ☃ = Math.min(☃, ☃);
    double ☃ = Math.min(☃, ☃);
    double ☃ = Math.max(☃, ☃);
    double ☃ = Math.max(☃, ☃);
    double ☃ = Math.max(☃, ☃);
    
    return new AxisAlignedBB(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  public AxisAlignedBB c(double ☃, double ☃, double ☃)
  {
    return new AxisAlignedBB(this.a + ☃, this.b + ☃, this.c + ☃, this.d + ☃, this.e + ☃, this.f + ☃);
  }
  
  public double a(AxisAlignedBB ☃, double ☃)
  {
    if ((☃.e <= this.b) || (☃.b >= this.e) || (☃.f <= this.c) || (☃.c >= this.f)) {
      return ☃;
    }
    if ((☃ > 0.0D) && (☃.d <= this.a))
    {
      double ☃ = this.a - ☃.d;
      if (☃ < ☃) {
        ☃ = ☃;
      }
    }
    else if ((☃ < 0.0D) && (☃.a >= this.d))
    {
      double ☃ = this.d - ☃.a;
      if (☃ > ☃) {
        ☃ = ☃;
      }
    }
    return ☃;
  }
  
  public double b(AxisAlignedBB ☃, double ☃)
  {
    if ((☃.d <= this.a) || (☃.a >= this.d) || (☃.f <= this.c) || (☃.c >= this.f)) {
      return ☃;
    }
    if ((☃ > 0.0D) && (☃.e <= this.b))
    {
      double ☃ = this.b - ☃.e;
      if (☃ < ☃) {
        ☃ = ☃;
      }
    }
    else if ((☃ < 0.0D) && (☃.b >= this.e))
    {
      double ☃ = this.e - ☃.b;
      if (☃ > ☃) {
        ☃ = ☃;
      }
    }
    return ☃;
  }
  
  public double c(AxisAlignedBB ☃, double ☃)
  {
    if ((☃.d <= this.a) || (☃.a >= this.d) || (☃.e <= this.b) || (☃.b >= this.e)) {
      return ☃;
    }
    if ((☃ > 0.0D) && (☃.f <= this.c))
    {
      double ☃ = this.c - ☃.f;
      if (☃ < ☃) {
        ☃ = ☃;
      }
    }
    else if ((☃ < 0.0D) && (☃.c >= this.f))
    {
      double ☃ = this.f - ☃.c;
      if (☃ > ☃) {
        ☃ = ☃;
      }
    }
    return ☃;
  }
  
  public boolean b(AxisAlignedBB ☃)
  {
    if ((☃.d <= this.a) || (☃.a >= this.d)) {
      return false;
    }
    if ((☃.e <= this.b) || (☃.b >= this.e)) {
      return false;
    }
    if ((☃.f <= this.c) || (☃.c >= this.f)) {
      return false;
    }
    return true;
  }
  
  public boolean a(Vec3D ☃)
  {
    if ((☃.a <= this.a) || (☃.a >= this.d)) {
      return false;
    }
    if ((☃.b <= this.b) || (☃.b >= this.e)) {
      return false;
    }
    if ((☃.c <= this.c) || (☃.c >= this.f)) {
      return false;
    }
    return true;
  }
  
  public double a()
  {
    double ☃ = this.d - this.a;
    double ☃ = this.e - this.b;
    double ☃ = this.f - this.c;
    return (☃ + ☃ + ☃) / 3.0D;
  }
  
  public AxisAlignedBB shrink(double ☃, double ☃, double ☃)
  {
    double ☃ = this.a + ☃;
    double ☃ = this.b + ☃;
    double ☃ = this.c + ☃;
    double ☃ = this.d - ☃;
    double ☃ = this.e - ☃;
    double ☃ = this.f - ☃;
    
    return new AxisAlignedBB(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  public MovingObjectPosition a(Vec3D ☃, Vec3D ☃)
  {
    Vec3D ☃ = ☃.a(☃, this.a);
    Vec3D ☃ = ☃.a(☃, this.d);
    
    Vec3D ☃ = ☃.b(☃, this.b);
    Vec3D ☃ = ☃.b(☃, this.e);
    
    Vec3D ☃ = ☃.c(☃, this.c);
    Vec3D ☃ = ☃.c(☃, this.f);
    if (!b(☃)) {
      ☃ = null;
    }
    if (!b(☃)) {
      ☃ = null;
    }
    if (!c(☃)) {
      ☃ = null;
    }
    if (!c(☃)) {
      ☃ = null;
    }
    if (!d(☃)) {
      ☃ = null;
    }
    if (!d(☃)) {
      ☃ = null;
    }
    Vec3D ☃ = null;
    if (☃ != null) {
      ☃ = ☃;
    }
    if ((☃ != null) && ((☃ == null) || (☃.distanceSquared(☃) < ☃.distanceSquared(☃)))) {
      ☃ = ☃;
    }
    if ((☃ != null) && ((☃ == null) || (☃.distanceSquared(☃) < ☃.distanceSquared(☃)))) {
      ☃ = ☃;
    }
    if ((☃ != null) && ((☃ == null) || (☃.distanceSquared(☃) < ☃.distanceSquared(☃)))) {
      ☃ = ☃;
    }
    if ((☃ != null) && ((☃ == null) || (☃.distanceSquared(☃) < ☃.distanceSquared(☃)))) {
      ☃ = ☃;
    }
    if ((☃ != null) && ((☃ == null) || (☃.distanceSquared(☃) < ☃.distanceSquared(☃)))) {
      ☃ = ☃;
    }
    if (☃ == null) {
      return null;
    }
    EnumDirection ☃ = null;
    if (☃ == ☃) {
      ☃ = EnumDirection.WEST;
    } else if (☃ == ☃) {
      ☃ = EnumDirection.EAST;
    } else if (☃ == ☃) {
      ☃ = EnumDirection.DOWN;
    } else if (☃ == ☃) {
      ☃ = EnumDirection.UP;
    } else if (☃ == ☃) {
      ☃ = EnumDirection.NORTH;
    } else {
      ☃ = EnumDirection.SOUTH;
    }
    return new MovingObjectPosition(☃, ☃);
  }
  
  private boolean b(Vec3D ☃)
  {
    if (☃ == null) {
      return false;
    }
    return (☃.b >= this.b) && (☃.b <= this.e) && (☃.c >= this.c) && (☃.c <= this.f);
  }
  
  private boolean c(Vec3D ☃)
  {
    if (☃ == null) {
      return false;
    }
    return (☃.a >= this.a) && (☃.a <= this.d) && (☃.c >= this.c) && (☃.c <= this.f);
  }
  
  private boolean d(Vec3D ☃)
  {
    if (☃ == null) {
      return false;
    }
    return (☃.a >= this.a) && (☃.a <= this.d) && (☃.b >= this.b) && (☃.b <= this.e);
  }
  
  public String toString()
  {
    return "box[" + this.a + ", " + this.b + ", " + this.c + " -> " + this.d + ", " + this.e + ", " + this.f + "]";
  }
}
