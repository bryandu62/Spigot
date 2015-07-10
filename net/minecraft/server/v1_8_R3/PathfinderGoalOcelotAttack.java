package net.minecraft.server.v1_8_R3;

public class PathfinderGoalOcelotAttack
  extends PathfinderGoal
{
  World a;
  EntityInsentient b;
  EntityLiving c;
  int d;
  
  public PathfinderGoalOcelotAttack(EntityInsentient ☃)
  {
    this.b = ☃;
    this.a = ☃.world;
    a(3);
  }
  
  public boolean a()
  {
    EntityLiving ☃ = this.b.getGoalTarget();
    if (☃ == null) {
      return false;
    }
    this.c = ☃;
    return true;
  }
  
  public boolean b()
  {
    if (!this.c.isAlive()) {
      return false;
    }
    if (this.b.h(this.c) > 225.0D) {
      return false;
    }
    return (!this.b.getNavigation().m()) || (a());
  }
  
  public void d()
  {
    this.c = null;
    this.b.getNavigation().n();
  }
  
  public void e()
  {
    this.b.getControllerLook().a(this.c, 30.0F, 30.0F);
    
    double ☃ = this.b.width * 2.0F * (this.b.width * 2.0F);
    double ☃ = this.b.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ);
    
    double ☃ = 0.8D;
    if ((☃ > ☃) && (☃ < 16.0D)) {
      ☃ = 1.33D;
    } else if (☃ < 225.0D) {
      ☃ = 0.6D;
    }
    this.b.getNavigation().a(this.c, ☃);
    
    this.d = Math.max(this.d - 1, 0);
    if (☃ > ☃) {
      return;
    }
    if (this.d > 0) {
      return;
    }
    this.d = 20;
    this.b.r(this.c);
  }
}
