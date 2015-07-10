package net.minecraft.server.v1_8_R3;

import java.util.List;

public class PathfinderGoalFollowParent
  extends PathfinderGoal
{
  EntityAnimal a;
  EntityAnimal b;
  double c;
  private int d;
  
  public PathfinderGoalFollowParent(EntityAnimal ☃, double ☃)
  {
    this.a = ☃;
    this.c = ☃;
  }
  
  public boolean a()
  {
    if (this.a.getAge() >= 0) {
      return false;
    }
    List<EntityAnimal> ☃ = this.a.world.a(this.a.getClass(), this.a.getBoundingBox().grow(8.0D, 4.0D, 8.0D));
    
    EntityAnimal ☃ = null;
    double ☃ = Double.MAX_VALUE;
    for (EntityAnimal ☃ : ☃) {
      if (☃.getAge() >= 0)
      {
        double ☃ = this.a.h(☃);
        if (☃ <= ☃)
        {
          ☃ = ☃;
          ☃ = ☃;
        }
      }
    }
    if (☃ == null) {
      return false;
    }
    if (☃ < 9.0D) {
      return false;
    }
    this.b = ☃;
    return true;
  }
  
  public boolean b()
  {
    if (this.a.getAge() >= 0) {
      return false;
    }
    if (!this.b.isAlive()) {
      return false;
    }
    double ☃ = this.a.h(this.b);
    if ((☃ < 9.0D) || (☃ > 256.0D)) {
      return false;
    }
    return true;
  }
  
  public void c()
  {
    this.d = 0;
  }
  
  public void d()
  {
    this.b = null;
  }
  
  public void e()
  {
    if (--this.d > 0) {
      return;
    }
    this.d = 10;
    this.a.getNavigation().a(this.b, this.c);
  }
}
