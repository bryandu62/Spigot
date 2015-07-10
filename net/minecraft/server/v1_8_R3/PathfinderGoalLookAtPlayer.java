package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class PathfinderGoalLookAtPlayer
  extends PathfinderGoal
{
  protected EntityInsentient a;
  protected Entity b;
  protected float c;
  private int e;
  private float f;
  protected Class<? extends Entity> d;
  
  public PathfinderGoalLookAtPlayer(EntityInsentient ☃, Class<? extends Entity> ☃, float ☃)
  {
    this.a = ☃;
    this.d = ☃;
    this.c = ☃;
    this.f = 0.02F;
    a(2);
  }
  
  public PathfinderGoalLookAtPlayer(EntityInsentient ☃, Class<? extends Entity> ☃, float ☃, float ☃)
  {
    this.a = ☃;
    this.d = ☃;
    this.c = ☃;
    this.f = ☃;
    a(2);
  }
  
  public boolean a()
  {
    if (this.a.bc().nextFloat() >= this.f) {
      return false;
    }
    if (this.a.getGoalTarget() != null) {
      this.b = this.a.getGoalTarget();
    }
    if (this.d == EntityHuman.class) {
      this.b = this.a.world.findNearbyPlayer(this.a, this.c);
    } else {
      this.b = this.a.world.a(this.d, this.a.getBoundingBox().grow(this.c, 3.0D, this.c), this.a);
    }
    return this.b != null;
  }
  
  public boolean b()
  {
    if (!this.b.isAlive()) {
      return false;
    }
    if (this.a.h(this.b) > this.c * this.c) {
      return false;
    }
    return this.e > 0;
  }
  
  public void c()
  {
    this.e = (40 + this.a.bc().nextInt(40));
  }
  
  public void d()
  {
    this.b = null;
  }
  
  public void e()
  {
    this.a.getControllerLook().a(this.b.locX, this.b.locY + this.b.getHeadHeight(), this.b.locZ, 10.0F, this.a.bQ());
    this.e -= 1;
  }
}
