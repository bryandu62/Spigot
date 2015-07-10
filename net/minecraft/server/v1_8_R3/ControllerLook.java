package net.minecraft.server.v1_8_R3;

public class ControllerLook
{
  private EntityInsentient a;
  private float b;
  private float c;
  private boolean d;
  private double e;
  private double f;
  private double g;
  
  public ControllerLook(EntityInsentient ☃)
  {
    this.a = ☃;
  }
  
  public void a(Entity ☃, float ☃, float ☃)
  {
    this.e = ☃.locX;
    if ((☃ instanceof EntityLiving)) {
      this.f = (☃.locY + ☃.getHeadHeight());
    } else {
      this.f = ((☃.getBoundingBox().b + ☃.getBoundingBox().e) / 2.0D);
    }
    this.g = ☃.locZ;
    this.b = ☃;
    this.c = ☃;
    this.d = true;
  }
  
  public void a(double ☃, double ☃, double ☃, float ☃, float ☃)
  {
    this.e = ☃;
    this.f = ☃;
    this.g = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = true;
  }
  
  public void a()
  {
    this.a.pitch = 0.0F;
    if (this.d)
    {
      this.d = false;
      
      double ☃ = this.e - this.a.locX;
      double ☃ = this.f - (this.a.locY + this.a.getHeadHeight());
      double ☃ = this.g - this.a.locZ;
      double ☃ = MathHelper.sqrt(☃ * ☃ + ☃ * ☃);
      
      float ☃ = (float)(MathHelper.b(☃, ☃) * 180.0D / 3.1415927410125732D) - 90.0F;
      float ☃ = (float)-(MathHelper.b(☃, ☃) * 180.0D / 3.1415927410125732D);
      this.a.pitch = a(this.a.pitch, ☃, this.c);
      this.a.aK = a(this.a.aK, ☃, this.b);
    }
    else
    {
      this.a.aK = a(this.a.aK, this.a.aI, 10.0F);
    }
    float ☃ = MathHelper.g(this.a.aK - this.a.aI);
    if (!this.a.getNavigation().m())
    {
      if (☃ < -75.0F) {
        this.a.aK = (this.a.aI - 75.0F);
      }
      if (☃ > 75.0F) {
        this.a.aK = (this.a.aI + 75.0F);
      }
    }
  }
  
  private float a(float ☃, float ☃, float ☃)
  {
    float ☃ = MathHelper.g(☃ - ☃);
    if (☃ > ☃) {
      ☃ = ☃;
    }
    if (☃ < -☃) {
      ☃ = -☃;
    }
    return ☃ + ☃;
  }
  
  public boolean b()
  {
    return this.d;
  }
  
  public double e()
  {
    return this.e;
  }
  
  public double f()
  {
    return this.f;
  }
  
  public double g()
  {
    return this.g;
  }
}
