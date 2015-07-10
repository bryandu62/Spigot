package net.minecraft.server.v1_8_R3;

public class PathfinderGoalTradeWithPlayer
  extends PathfinderGoal
{
  private EntityVillager a;
  
  public PathfinderGoalTradeWithPlayer(EntityVillager ☃)
  {
    this.a = ☃;
    a(5);
  }
  
  public boolean a()
  {
    if (!this.a.isAlive()) {
      return false;
    }
    if (this.a.V()) {
      return false;
    }
    if (!this.a.onGround) {
      return false;
    }
    if (this.a.velocityChanged) {
      return false;
    }
    EntityHuman ☃ = this.a.v_();
    if (☃ == null) {
      return false;
    }
    if (this.a.h(☃) > 16.0D) {
      return false;
    }
    if (!(☃.activeContainer instanceof Container)) {
      return false;
    }
    return true;
  }
  
  public void c()
  {
    this.a.getNavigation().n();
  }
  
  public void d()
  {
    this.a.a_(null);
  }
}
