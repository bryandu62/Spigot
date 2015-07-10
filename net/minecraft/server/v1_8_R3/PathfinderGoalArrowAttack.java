package net.minecraft.server.v1_8_R3;

public class PathfinderGoalArrowAttack
  extends PathfinderGoal
{
  private final EntityInsentient a;
  private final IRangedEntity b;
  private EntityLiving c;
  private int d = -1;
  private double e;
  private int f;
  private int g;
  private int h;
  private float i;
  private float j;
  
  public PathfinderGoalArrowAttack(IRangedEntity ☃, double ☃, int ☃, float ☃)
  {
    this(☃, ☃, ☃, ☃, ☃);
  }
  
  public PathfinderGoalArrowAttack(IRangedEntity ☃, double ☃, int ☃, int ☃, float ☃)
  {
    if (!(☃ instanceof EntityLiving)) {
      throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
    }
    this.b = ☃;
    this.a = ((EntityInsentient)☃);
    this.e = ☃;
    this.g = ☃;
    this.h = ☃;
    this.i = ☃;
    this.j = (☃ * ☃);
    a(3);
  }
  
  public boolean a()
  {
    EntityLiving ☃ = this.a.getGoalTarget();
    if (☃ == null) {
      return false;
    }
    this.c = ☃;
    return true;
  }
  
  public boolean b()
  {
    return (a()) || (!this.a.getNavigation().m());
  }
  
  public void d()
  {
    this.c = null;
    this.f = 0;
    this.d = -1;
  }
  
  public void e()
  {
    double ☃ = this.a.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ);
    boolean ☃ = this.a.getEntitySenses().a(this.c);
    if (☃) {
      this.f += 1;
    } else {
      this.f = 0;
    }
    if ((☃ > this.j) || (this.f < 20)) {
      this.a.getNavigation().a(this.c, this.e);
    } else {
      this.a.getNavigation().n();
    }
    this.a.getControllerLook().a(this.c, 30.0F, 30.0F);
    if (--this.d == 0)
    {
      if ((☃ > this.j) || (!☃)) {
        return;
      }
      float ☃ = MathHelper.sqrt(☃) / this.i;
      float ☃ = ☃;
      ☃ = MathHelper.a(☃, 0.1F, 1.0F);
      
      this.b.a(this.c, ☃);
      this.d = MathHelper.d(☃ * (this.h - this.g) + this.g);
    }
    else if (this.d < 0)
    {
      float ☃ = MathHelper.sqrt(☃) / this.i;
      this.d = MathHelper.d(☃ * (this.h - this.g) + this.g);
    }
  }
}
