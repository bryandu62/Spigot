package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class PathfinderGoalLeapAtTarget
  extends PathfinderGoal
{
  EntityInsentient a;
  EntityLiving b;
  float c;
  
  public PathfinderGoalLeapAtTarget(EntityInsentient ☃, float ☃)
  {
    this.a = ☃;
    this.c = ☃;
    a(5);
  }
  
  public boolean a()
  {
    this.b = this.a.getGoalTarget();
    if (this.b == null) {
      return false;
    }
    double ☃ = this.a.h(this.b);
    if ((☃ < 4.0D) || (☃ > 16.0D)) {
      return false;
    }
    if (!this.a.onGround) {
      return false;
    }
    if (this.a.bc().nextInt(5) != 0) {
      return false;
    }
    return true;
  }
  
  public boolean b()
  {
    return !this.a.onGround;
  }
  
  public void c()
  {
    double ☃ = this.b.locX - this.a.locX;
    double ☃ = this.b.locZ - this.a.locZ;
    float ☃ = MathHelper.sqrt(☃ * ☃ + ☃ * ☃);
    this.a.motX += ☃ / ☃ * 0.5D * 0.800000011920929D + this.a.motX * 0.20000000298023224D;
    this.a.motZ += ☃ / ☃ * 0.5D * 0.800000011920929D + this.a.motZ * 0.20000000298023224D;
    this.a.motY = this.c;
  }
}
