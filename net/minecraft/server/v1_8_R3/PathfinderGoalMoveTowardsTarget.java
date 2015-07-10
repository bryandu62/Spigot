package net.minecraft.server.v1_8_R3;

public class PathfinderGoalMoveTowardsTarget
  extends PathfinderGoal
{
  private EntityCreature a;
  private EntityLiving b;
  private double c;
  private double d;
  private double e;
  private double f;
  private float g;
  
  public PathfinderGoalMoveTowardsTarget(EntityCreature ☃, double ☃, float ☃)
  {
    this.a = ☃;
    this.f = ☃;
    this.g = ☃;
    a(1);
  }
  
  public boolean a()
  {
    this.b = this.a.getGoalTarget();
    if (this.b == null) {
      return false;
    }
    if (this.b.h(this.a) > this.g * this.g) {
      return false;
    }
    Vec3D ☃ = RandomPositionGenerator.a(this.a, 16, 7, new Vec3D(this.b.locX, this.b.locY, this.b.locZ));
    if (☃ == null) {
      return false;
    }
    this.c = ☃.a;
    this.d = ☃.b;
    this.e = ☃.c;
    return true;
  }
  
  public boolean b()
  {
    return (!this.a.getNavigation().m()) && (this.b.isAlive()) && (this.b.h(this.a) < this.g * this.g);
  }
  
  public void d()
  {
    this.b = null;
  }
  
  public void c()
  {
    this.a.getNavigation().a(this.c, this.d, this.e, this.f);
  }
}
