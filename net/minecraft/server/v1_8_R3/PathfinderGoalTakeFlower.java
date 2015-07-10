package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class PathfinderGoalTakeFlower
  extends PathfinderGoal
{
  private EntityVillager a;
  private EntityIronGolem b;
  private int c;
  private boolean d;
  
  public PathfinderGoalTakeFlower(EntityVillager ☃)
  {
    this.a = ☃;
    a(3);
  }
  
  public boolean a()
  {
    if (this.a.getAge() >= 0) {
      return false;
    }
    if (!this.a.world.w()) {
      return false;
    }
    List<EntityIronGolem> ☃ = this.a.world.a(EntityIronGolem.class, this.a.getBoundingBox().grow(6.0D, 2.0D, 6.0D));
    if (☃.isEmpty()) {
      return false;
    }
    for (EntityIronGolem ☃ : ☃) {
      if (☃.cm() > 0)
      {
        this.b = ☃;
        break;
      }
    }
    return this.b != null;
  }
  
  public boolean b()
  {
    return this.b.cm() > 0;
  }
  
  public void c()
  {
    this.c = this.a.bc().nextInt(320);
    this.d = false;
    this.b.getNavigation().n();
  }
  
  public void d()
  {
    this.b = null;
    this.a.getNavigation().n();
  }
  
  public void e()
  {
    this.a.getControllerLook().a(this.b, 30.0F, 30.0F);
    if (this.b.cm() == this.c)
    {
      this.a.getNavigation().a(this.b, 0.5D);
      this.d = true;
    }
    if ((this.d) && 
      (this.a.h(this.b) < 4.0D))
    {
      this.b.a(false);
      this.a.getNavigation().n();
    }
  }
}
