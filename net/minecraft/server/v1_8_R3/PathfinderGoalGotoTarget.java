package net.minecraft.server.v1_8_R3;

import java.util.Random;

public abstract class PathfinderGoalGotoTarget
  extends PathfinderGoal
{
  private final EntityCreature c;
  private final double d;
  protected int a;
  private int e;
  private int f;
  protected BlockPosition b = BlockPosition.ZERO;
  private boolean g;
  private int h;
  
  public PathfinderGoalGotoTarget(EntityCreature ☃, double ☃, int ☃)
  {
    this.c = ☃;
    this.d = ☃;
    this.h = ☃;
    a(5);
  }
  
  public boolean a()
  {
    if (this.a > 0)
    {
      this.a -= 1;
      return false;
    }
    this.a = (200 + this.c.bc().nextInt(200));
    return g();
  }
  
  public boolean b()
  {
    return (this.e >= -this.f) && (this.e <= 1200) && (a(this.c.world, this.b));
  }
  
  public void c()
  {
    this.c.getNavigation().a(this.b.getX() + 0.5D, this.b.getY() + 1, this.b.getZ() + 0.5D, this.d);
    this.e = 0;
    this.f = (this.c.bc().nextInt(this.c.bc().nextInt(1200) + 1200) + 1200);
  }
  
  public void d() {}
  
  public void e()
  {
    if (this.c.c(this.b.up()) > 1.0D)
    {
      this.g = false;
      this.e += 1;
      if (this.e % 40 == 0) {
        this.c.getNavigation().a(this.b.getX() + 0.5D, this.b.getY() + 1, this.b.getZ() + 0.5D, this.d);
      }
    }
    else
    {
      this.g = true;
      this.e -= 1;
    }
  }
  
  protected boolean f()
  {
    return this.g;
  }
  
  private boolean g()
  {
    int ☃ = this.h;
    int ☃ = 1;
    BlockPosition ☃ = new BlockPosition(this.c);
    for (int ☃ = 0; ☃ <= 1; ☃ = ☃ > 0 ? -☃ : 1 - ☃) {
      for (int ☃ = 0; ☃ < ☃; ☃++) {
        for (int ☃ = 0; ☃ <= ☃; ☃ = ☃ > 0 ? -☃ : 1 - ☃) {
          for (int ☃ = (☃ < ☃) && (☃ > -☃) ? ☃ : 0; ☃ <= ☃; ☃ = ☃ > 0 ? -☃ : 1 - ☃)
          {
            BlockPosition ☃ = ☃.a(☃, ☃ - 1, ☃);
            if ((this.c.e(☃)) && (a(this.c.world, ☃)))
            {
              this.b = ☃;
              return true;
            }
          }
        }
      }
    }
    return false;
  }
  
  protected abstract boolean a(World paramWorld, BlockPosition paramBlockPosition);
}
