package net.minecraft.server.v1_8_R3;

public class PathfinderGoalPanic
  extends PathfinderGoal
{
  private EntityCreature b;
  protected double a;
  private double c;
  private double d;
  private double e;
  
  public PathfinderGoalPanic(EntityCreature entitycreature, double d0)
  {
    this.b = entitycreature;
    this.a = d0;
    a(1);
  }
  
  public boolean a()
  {
    if ((this.b.getLastDamager() == null) && (!this.b.isBurning())) {
      return false;
    }
    Vec3D vec3d = RandomPositionGenerator.a(this.b, 5, 4);
    if (vec3d == null) {
      return false;
    }
    this.c = vec3d.a;
    this.d = vec3d.b;
    this.e = vec3d.c;
    return true;
  }
  
  public void c()
  {
    this.b.getNavigation().a(this.c, this.d, this.e, this.a);
  }
  
  public boolean b()
  {
    if (this.b.ticksLived - this.b.hurtTimestamp > 100)
    {
      this.b.b(null);
      return false;
    }
    return !this.b.getNavigation().m();
  }
}
