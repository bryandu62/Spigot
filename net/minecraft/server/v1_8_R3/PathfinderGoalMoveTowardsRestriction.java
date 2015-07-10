package net.minecraft.server.v1_8_R3;

public class PathfinderGoalMoveTowardsRestriction
  extends PathfinderGoal
{
  private EntityCreature a;
  private double b;
  private double c;
  private double d;
  private double e;
  
  public PathfinderGoalMoveTowardsRestriction(EntityCreature ☃, double ☃)
  {
    this.a = ☃;
    this.e = ☃;
    a(1);
  }
  
  public boolean a()
  {
    if (this.a.cg()) {
      return false;
    }
    BlockPosition ☃ = this.a.ch();
    Vec3D ☃ = RandomPositionGenerator.a(this.a, 16, 7, new Vec3D(☃.getX(), ☃.getY(), ☃.getZ()));
    if (☃ == null) {
      return false;
    }
    this.b = ☃.a;
    this.c = ☃.b;
    this.d = ☃.c;
    return true;
  }
  
  public boolean b()
  {
    return !this.a.getNavigation().m();
  }
  
  public void c()
  {
    this.a.getNavigation().a(this.b, this.c, this.d, this.e);
  }
}
