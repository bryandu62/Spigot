package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class PathfinderGoalBeg
  extends PathfinderGoal
{
  private EntityWolf a;
  private EntityHuman b;
  private World c;
  private float d;
  private int e;
  
  public PathfinderGoalBeg(EntityWolf ☃, float ☃)
  {
    this.a = ☃;
    this.c = ☃.world;
    this.d = ☃;
    a(2);
  }
  
  public boolean a()
  {
    this.b = this.c.findNearbyPlayer(this.a, this.d);
    if (this.b == null) {
      return false;
    }
    return a(this.b);
  }
  
  public boolean b()
  {
    if (!this.b.isAlive()) {
      return false;
    }
    if (this.a.h(this.b) > this.d * this.d) {
      return false;
    }
    return (this.e > 0) && (a(this.b));
  }
  
  public void c()
  {
    this.a.p(true);
    this.e = (40 + this.a.bc().nextInt(40));
  }
  
  public void d()
  {
    this.a.p(false);
    this.b = null;
  }
  
  public void e()
  {
    this.a.getControllerLook().a(this.b.locX, this.b.locY + this.b.getHeadHeight(), this.b.locZ, 10.0F, this.a.bQ());
    this.e -= 1;
  }
  
  private boolean a(EntityHuman ☃)
  {
    ItemStack ☃ = ☃.inventory.getItemInHand();
    if (☃ == null) {
      return false;
    }
    if ((!this.a.isTamed()) && (☃.getItem() == Items.BONE)) {
      return true;
    }
    return this.a.d(☃);
  }
}
