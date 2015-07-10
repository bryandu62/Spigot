package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class PathfinderGoalFleeSun
  extends PathfinderGoal
{
  private EntityCreature a;
  private double b;
  private double c;
  private double d;
  private double e;
  private World f;
  
  public PathfinderGoalFleeSun(EntityCreature ☃, double ☃)
  {
    this.a = ☃;
    this.e = ☃;
    this.f = ☃.world;
    a(1);
  }
  
  public boolean a()
  {
    if (!this.f.w()) {
      return false;
    }
    if (!this.a.isBurning()) {
      return false;
    }
    if (!this.f.i(new BlockPosition(this.a.locX, this.a.getBoundingBox().b, this.a.locZ))) {
      return false;
    }
    Vec3D ☃ = f();
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
  
  private Vec3D f()
  {
    Random ☃ = this.a.bc();
    BlockPosition ☃ = new BlockPosition(this.a.locX, this.a.getBoundingBox().b, this.a.locZ);
    for (int ☃ = 0; ☃ < 10; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(20) - 10, ☃.nextInt(6) - 3, ☃.nextInt(20) - 10);
      if ((!this.f.i(☃)) && (this.a.a(☃) < 0.0F)) {
        return new Vec3D(☃.getX(), ☃.getY(), ☃.getZ());
      }
    }
    return null;
  }
}
