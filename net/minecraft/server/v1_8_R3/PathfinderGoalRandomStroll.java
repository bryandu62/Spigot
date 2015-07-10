package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class PathfinderGoalRandomStroll
  extends PathfinderGoal
{
  private EntityCreature a;
  private double b;
  private double c;
  private double d;
  private double e;
  private int f;
  private boolean g;
  
  public PathfinderGoalRandomStroll(EntityCreature ☃, double ☃)
  {
    this(☃, ☃, 120);
  }
  
  public PathfinderGoalRandomStroll(EntityCreature ☃, double ☃, int ☃)
  {
    this.a = ☃;
    this.e = ☃;
    this.f = ☃;
    a(1);
  }
  
  public boolean a()
  {
    if (!this.g)
    {
      if (this.a.bh() >= 100) {
        return false;
      }
      if (this.a.bc().nextInt(this.f) != 0) {
        return false;
      }
    }
    Vec3D ☃ = RandomPositionGenerator.a(this.a, 10, 7);
    if (☃ == null) {
      return false;
    }
    this.b = ☃.a;
    this.c = ☃.b;
    this.d = ☃.c;
    this.g = false;
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
  
  public void f()
  {
    this.g = true;
  }
  
  public void setTimeBetweenMovement(int ☃)
  {
    this.f = ☃;
  }
}
