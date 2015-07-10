package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class PathfinderGoalFloat
  extends PathfinderGoal
{
  private EntityInsentient a;
  
  public PathfinderGoalFloat(EntityInsentient ☃)
  {
    this.a = ☃;
    a(4);
    ((Navigation)☃.getNavigation()).d(true);
  }
  
  public boolean a()
  {
    return (this.a.V()) || (this.a.ab());
  }
  
  public void e()
  {
    if (this.a.bc().nextFloat() < 0.8F) {
      this.a.getControllerJump().a();
    }
  }
}
