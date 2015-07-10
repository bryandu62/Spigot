package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class PathfinderGoalMeleeAttack
  extends PathfinderGoal
{
  World a;
  protected EntityCreature b;
  int c;
  double d;
  boolean e;
  PathEntity f;
  Class<? extends Entity> g;
  private int h;
  private double i;
  private double j;
  private double k;
  
  public PathfinderGoalMeleeAttack(EntityCreature ☃, Class<? extends Entity> ☃, double ☃, boolean ☃)
  {
    this(☃, ☃, ☃);
    this.g = ☃;
  }
  
  public PathfinderGoalMeleeAttack(EntityCreature ☃, double ☃, boolean ☃)
  {
    this.b = ☃;
    this.a = ☃.world;
    this.d = ☃;
    this.e = ☃;
    a(3);
  }
  
  public boolean a()
  {
    EntityLiving ☃ = this.b.getGoalTarget();
    if (☃ == null) {
      return false;
    }
    if (!☃.isAlive()) {
      return false;
    }
    if ((this.g != null) && (!this.g.isAssignableFrom(☃.getClass()))) {
      return false;
    }
    this.f = this.b.getNavigation().a(☃);
    return this.f != null;
  }
  
  public boolean b()
  {
    EntityLiving ☃ = this.b.getGoalTarget();
    if (☃ == null) {
      return false;
    }
    if (!☃.isAlive()) {
      return false;
    }
    if (!this.e)
    {
      if (this.b.getNavigation().m()) {
        return false;
      }
      return true;
    }
    if (!this.b.e(new BlockPosition(☃))) {
      return false;
    }
    return true;
  }
  
  public void c()
  {
    this.b.getNavigation().a(this.f, this.d);
    this.h = 0;
  }
  
  public void d()
  {
    this.b.getNavigation().n();
  }
  
  public void e()
  {
    EntityLiving ☃ = this.b.getGoalTarget();
    this.b.getControllerLook().a(☃, 30.0F, 30.0F);
    double ☃ = this.b.e(☃.locX, ☃.getBoundingBox().b, ☃.locZ);
    double ☃ = a(☃);
    this.h -= 1;
    if (((this.e) || (this.b.getEntitySenses().a(☃))) && 
      (this.h <= 0) && (
      ((this.i == 0.0D) && (this.j == 0.0D) && (this.k == 0.0D)) || (☃.e(this.i, this.j, this.k) >= 1.0D) || (this.b.bc().nextFloat() < 0.05F)))
    {
      this.i = ☃.locX;
      this.j = ☃.getBoundingBox().b;
      this.k = ☃.locZ;
      this.h = (4 + this.b.bc().nextInt(7));
      if (☃ > 1024.0D) {
        this.h += 10;
      } else if (☃ > 256.0D) {
        this.h += 5;
      }
      if (!this.b.getNavigation().a(☃, this.d)) {
        this.h += 15;
      }
    }
    this.c = Math.max(this.c - 1, 0);
    if ((☃ <= ☃) && (this.c <= 0))
    {
      this.c = 20;
      if (this.b.bA() != null) {
        this.b.bw();
      }
      this.b.r(☃);
    }
  }
  
  protected double a(EntityLiving ☃)
  {
    return this.b.width * 2.0F * (this.b.width * 2.0F) + ☃.width;
  }
}
