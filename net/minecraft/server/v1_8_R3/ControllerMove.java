package net.minecraft.server.v1_8_R3;

public class ControllerMove
{
  protected EntityInsentient a;
  protected double b;
  protected double c;
  protected double d;
  protected double e;
  protected boolean f;
  
  public ControllerMove(EntityInsentient ☃)
  {
    this.a = ☃;
    this.b = ☃.locX;
    this.c = ☃.locY;
    this.d = ☃.locZ;
  }
  
  public boolean a()
  {
    return this.f;
  }
  
  public double b()
  {
    return this.e;
  }
  
  public void a(double ☃, double ☃, double ☃, double ☃)
  {
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
    this.e = ☃;
    this.f = true;
  }
  
  public void c()
  {
    this.a.n(0.0F);
    if (!this.f) {
      return;
    }
    this.f = false;
    
    int ☃ = MathHelper.floor(this.a.getBoundingBox().b + 0.5D);
    
    double ☃ = this.b - this.a.locX;
    double ☃ = this.d - this.a.locZ;
    double ☃ = this.c - ☃;
    double ☃ = ☃ * ☃ + ☃ * ☃ + ☃ * ☃;
    if (☃ < 2.500000277905201E-7D) {
      return;
    }
    float ☃ = (float)(MathHelper.b(☃, ☃) * 180.0D / 3.1415927410125732D) - 90.0F;
    
    this.a.yaw = a(this.a.yaw, ☃, 30.0F);
    this.a.k((float)(this.e * this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
    if ((☃ > 0.0D) && (☃ * ☃ + ☃ * ☃ < 1.0D)) {
      this.a.getControllerJump().a();
    }
  }
  
  protected float a(float ☃, float ☃, float ☃)
  {
    float ☃ = MathHelper.g(☃ - ☃);
    if (☃ > ☃) {
      ☃ = ☃;
    }
    if (☃ < -☃) {
      ☃ = -☃;
    }
    float ☃ = ☃ + ☃;
    if (☃ < 0.0F) {
      ☃ += 360.0F;
    } else if (☃ > 360.0F) {
      ☃ -= 360.0F;
    }
    return ☃;
  }
  
  public double d()
  {
    return this.b;
  }
  
  public double e()
  {
    return this.c;
  }
  
  public double f()
  {
    return this.d;
  }
}
