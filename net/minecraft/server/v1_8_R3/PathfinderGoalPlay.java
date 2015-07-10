package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class PathfinderGoalPlay
  extends PathfinderGoal
{
  private EntityVillager a;
  private EntityLiving b;
  private double c;
  private int d;
  
  public PathfinderGoalPlay(EntityVillager ☃, double ☃)
  {
    this.a = ☃;
    this.c = ☃;
    a(1);
  }
  
  public boolean a()
  {
    if (this.a.getAge() >= 0) {
      return false;
    }
    if (this.a.bc().nextInt(400) != 0) {
      return false;
    }
    List<EntityVillager> ☃ = this.a.world.a(EntityVillager.class, this.a.getBoundingBox().grow(6.0D, 3.0D, 6.0D));
    double ☃ = Double.MAX_VALUE;
    for (EntityVillager ☃ : ☃) {
      if ((☃ != this.a) && 
      
        (!☃.cn()) && 
        
        (☃.getAge() < 0))
      {
        double ☃ = ☃.h(this.a);
        if (☃ <= ☃)
        {
          ☃ = ☃;
          this.b = ☃;
        }
      }
    }
    if (this.b == null)
    {
      Vec3D ☃ = RandomPositionGenerator.a(this.a, 16, 3);
      if (☃ == null) {
        return false;
      }
    }
    return true;
  }
  
  public boolean b()
  {
    return this.d > 0;
  }
  
  public void c()
  {
    if (this.b != null) {
      this.a.m(true);
    }
    this.d = 1000;
  }
  
  public void d()
  {
    this.a.m(false);
    this.b = null;
  }
  
  public void e()
  {
    this.d -= 1;
    if (this.b != null)
    {
      if (this.a.h(this.b) > 4.0D) {
        this.a.getNavigation().a(this.b, this.c);
      }
    }
    else if (this.a.getNavigation().m())
    {
      Vec3D ☃ = RandomPositionGenerator.a(this.a, 16, 3);
      if (☃ == null) {
        return;
      }
      this.a.getNavigation().a(☃.a, ☃.b, ☃.c, this.c);
    }
  }
}
