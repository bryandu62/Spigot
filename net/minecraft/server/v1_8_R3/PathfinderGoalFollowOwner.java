package net.minecraft.server.v1_8_R3;

public class PathfinderGoalFollowOwner
  extends PathfinderGoal
{
  private EntityTameableAnimal d;
  private EntityLiving e;
  World a;
  private double f;
  private NavigationAbstract g;
  private int h;
  float b;
  float c;
  private boolean i;
  
  public PathfinderGoalFollowOwner(EntityTameableAnimal ☃, double ☃, float ☃, float ☃)
  {
    this.d = ☃;
    this.a = ☃.world;
    this.f = ☃;
    this.g = ☃.getNavigation();
    this.c = ☃;
    this.b = ☃;
    a(3);
    if (!(☃.getNavigation() instanceof Navigation)) {
      throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
    }
  }
  
  public boolean a()
  {
    EntityLiving ☃ = this.d.getOwner();
    if (☃ == null) {
      return false;
    }
    if (((☃ instanceof EntityHuman)) && (((EntityHuman)☃).isSpectator())) {
      return false;
    }
    if (this.d.isSitting()) {
      return false;
    }
    if (this.d.h(☃) < this.c * this.c) {
      return false;
    }
    this.e = ☃;
    return true;
  }
  
  public boolean b()
  {
    return (!this.g.m()) && (this.d.h(this.e) > this.b * this.b) && (!this.d.isSitting());
  }
  
  public void c()
  {
    this.h = 0;
    this.i = ((Navigation)this.d.getNavigation()).e();
    ((Navigation)this.d.getNavigation()).a(false);
  }
  
  public void d()
  {
    this.e = null;
    this.g.n();
    ((Navigation)this.d.getNavigation()).a(true);
  }
  
  private boolean a(BlockPosition ☃)
  {
    IBlockData ☃ = this.a.getType(☃);
    Block ☃ = ☃.getBlock();
    if (☃ == Blocks.AIR) {
      return true;
    }
    return !☃.d();
  }
  
  public void e()
  {
    this.d.getControllerLook().a(this.e, 10.0F, this.d.bQ());
    if (this.d.isSitting()) {
      return;
    }
    if (--this.h > 0) {
      return;
    }
    this.h = 10;
    if (this.g.a(this.e, this.f)) {
      return;
    }
    if (this.d.cc()) {
      return;
    }
    if (this.d.h(this.e) < 144.0D) {
      return;
    }
    int ☃ = MathHelper.floor(this.e.locX) - 2;
    int ☃ = MathHelper.floor(this.e.locZ) - 2;
    int ☃ = MathHelper.floor(this.e.getBoundingBox().b);
    for (int ☃ = 0; ☃ <= 4; ☃++) {
      for (int ☃ = 0; ☃ <= 4; ☃++) {
        if ((☃ < 1) || (☃ < 1) || (☃ > 3) || (☃ > 3)) {
          if ((World.a(this.a, new BlockPosition(☃ + ☃, ☃ - 1, ☃ + ☃))) && (a(new BlockPosition(☃ + ☃, ☃, ☃ + ☃))) && (a(new BlockPosition(☃ + ☃, ☃ + 1, ☃ + ☃))))
          {
            this.d.setPositionRotation(☃ + ☃ + 0.5F, ☃, ☃ + ☃ + 0.5F, this.d.yaw, this.d.pitch);
            this.g.n();
            return;
          }
        }
      }
    }
  }
}
